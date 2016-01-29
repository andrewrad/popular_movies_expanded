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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mMainFragment", (MovieListFragment) mMainFragment);
    }

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

    @Override
    public void holdOldTitle() {
        mToolBarTitle=getSupportActionBar().getTitle();
    }


    @Override
    public void deleteMovie() {
        if(mToolBarTitle.equals(getString(R.string.action_favorited))){
            mMainFragment.getFavorites();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        }
        Integer endBackPressed=getResources().getBoolean(R.bool.isTablet)==Boolean.TRUE?1:0;
//        Log.e(TAG,"backpressed istablet?: "+endBackPressed+", fragmentManager.getBackStackEntryCount(): "+fragmentManager.getBackStackEntryCount());

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