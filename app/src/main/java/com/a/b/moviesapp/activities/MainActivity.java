package com.a.b.moviesapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.a.b.moviesapp.Constants;
import com.a.b.moviesapp.MainInterface;
import com.a.b.moviesapp.R;
import com.a.b.moviesapp.fragments.MovieListFragment;
import com.a.b.moviesapp.fragments.MovieDetailsFragment;


public class MainActivity extends AppCompatActivity implements MainInterface.MovieInterface{
    String TAG="MainActivity";
    Boolean backPressedToExitOnce=false;
    FragmentManager fragmentManager = getSupportFragmentManager();
    MovieListFragment mMainFragment;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar= (Toolbar) findViewById(R.id.toolbar_main);
//        mToolbar.setTitle("Movies");
        setSupportActionBar(mToolbar);

        mMainFragment=new MovieListFragment();
//        mMainFragment.getMovies(Constants.MOST_POPULAR);

        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, mMainFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular_sort) {
            mMainFragment.getMovies(Constants.MOST_POPULAR);
            getSupportActionBar().setTitle("Movies: Most Popular");

        }else if(id==R.id.action_highest_rated){
            mMainFragment.getMovies(Constants.HIGHEST_RATED);
            getSupportActionBar().setTitle("Movies: Highest Rated");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openDetailFragment(Bundle detail) {
        MovieDetailsFragment movieDetailsFragment=new MovieDetailsFragment();

        movieDetailsFragment.setArguments(detail);

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, movieDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        }
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();

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