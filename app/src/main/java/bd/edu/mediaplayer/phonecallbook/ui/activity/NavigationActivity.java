package bd.edu.mediaplayer.phonecallbook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import bd.edu.mediaplayer.phonecallbook.R;
import bd.edu.mediaplayer.phonecallbook.model.WeatherSample;
import bd.edu.mediaplayer.phonecallbook.ui.fragment.AddBookFragment;
import bd.edu.mediaplayer.phonecallbook.ui.fragment.BookListFragment;
import bd.edu.mediaplayer.phonecallbook.ui.fragment.BrowseBookFragment;

public class NavigationActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private Fragment fragment;
    private FragmentManager fragmentManager;
/*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Book List");
                    fragment = new BookListFragment();
                    loadFragment(fragment);
                case R.id.navigation_dashboard:
                    toolbar.setTitle("Add Book");
                    fragment = new AddBookFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Browse Books");
                    fragment = new BrowseBookFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar.setTitle("Book List");
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, new BookListFragment()).commit();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        toolbar.setTitle("Book List");
                        fragment = new BookListFragment();
                        break;
                    case R.id.navigation_dashboard:
                        toolbar.setTitle("Add Book");
                        fragment = new AddBookFragment();
                        break;
                    case R.id.navigation_notifications:
                        toolbar.setTitle("Browse Books");
                        fragment = new BrowseBookFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, fragment).commit();
                transaction.addToBackStack(null);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
