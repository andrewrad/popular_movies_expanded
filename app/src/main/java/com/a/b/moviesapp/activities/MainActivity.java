package com.a.b.moviesapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.other.MainInterface;
import com.a.b.moviesapp.pojo.Movie;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.fragments.MovieListFragment;
import com.a.b.moviesapp.fragments.MovieDetailsFragment;

/**
 * Starting place for the app. This is the base Activity, so fragment-to-fragment communication goes through this
 * class. All views are displayed and manipulated through their particular fragment.
 * The app retrieves movie data from the themoviedb.com through two network calls. The first one is for a general
 * overview of all movies in either popular movies or highest rated. Once the user selects a movie, reviews and
 * movie trailers are pulled from a second network call.
 * The first network call is an AsyncTask, the second uses Retrofit. Ideally both should be the same and using
 * Retrofit but this app shows I can do both if needed.
 * Created by Andrew on 1/1/2016.
 */

public class MainActivity extends AppCompatActivity implements MainInterface.MovieInterface{
    String TAG="MainActivity";
    Boolean backPressedToExitOnce=false;
    FragmentManager fragmentManager = getSupportFragmentManager();
    MovieListFragment mMainFragment;
    CharSequence mToolBarTitle;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBarTitle=getResources().getString(R.string.action_popular_sort);
        mToolbar= (Toolbar) findViewById(R.id.toolbar_main);
        mToolbar.setTitle(mToolBarTitle);
        setSupportActionBar(mToolbar);

        if(savedInstanceState==null) {
            mMainFragment=new MovieListFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, mMainFragment).commit();
        }else{
            mMainFragment=(MovieListFragment) getSupportFragmentManager().getFragment(savedInstanceState,"mMainFragment");
        }
    }

    /**
     * This function saves the MovieListFragment when the device is rotated
     * @param outState provided by Android
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mMainFragment", (MovieListFragment) mMainFragment);
    }

    /**
     * Opens the detail view either in a new window (phone) or as one half of the screen (tablet) in a master-
     * detail view type.
     * @param movie Movie
     */
    @Override
    public void openDetailFragment(Movie movie) {
        MovieDetailsFragment movieDetailsFragment=new MovieDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DETAILS_BUNDLE, movie);
        movieDetailsFragment.setArguments(bundle);

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(getResources().getBoolean(R.bool.isTablet)){
            ft.replace(R.id.fragment_container2, movieDetailsFragment);
            ft.commit();
        }else{
            ft.replace(R.id.fragment_container, movieDetailsFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    /**
     * Retrieves the title from the toolbar to give the user an accurate title for what they are viewing.
     */
    @Override
    public void holdOldTitle() {
        mToolBarTitle=getSupportActionBar().getTitle();
    }

    /**
     * Interface function; Movie was deleted from the favorites database. If the toolbar is showing the
     * favorites screen, then update the favorites screen by querying the database through MovieListFragment.
     * Otherwise the favorites screen will be automatically updated next time the user visits their list
     * of favorite movies.
     */
    @Override
    public void updateFavorites() {
        if(mToolBarTitle.equals(getString(R.string.action_favorited))){
            mMainFragment.getFavorites();
        }
    }

    /**
     * Overrides onBackPressed by asking the user if they really want to exit the app. The app will be put in
     * background if the back button is pressed twice in 2 seconds.
     */
    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        }
        Integer endBackPressed=getResources().getBoolean(R.bool.isTablet)==Boolean.TRUE?1:0;

        if (fragmentManager.getBackStackEntryCount() > endBackPressed) {
            fragmentManager.popBackStack();
            mToolbar.setTitle(mToolBarTitle);

        } else {
            Toast.makeText(this, R.string.backpress, Toast.LENGTH_SHORT).show();
            this.backPressedToExitOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }
}