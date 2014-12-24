package uk.co.traintrackapp.traintrack;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;

import uk.co.traintrackapp.traintrack.fragment.AccountFragment;
import uk.co.traintrackapp.traintrack.fragment.DashboardFragment;

import uk.co.traintrackapp.traintrack.fragment.JourneysFragment;
import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;
import uk.co.traintrackapp.traintrack.fragment.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
        mTitle = getString(R.string.app_name);
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case -1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance()).commit();
                break;
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DashboardFragment.newInstance()).commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, StationsFragment.newInstance()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, JourneysFragment.newInstance()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance()).commit();
                break;
        }
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mTitle);
    }

}
