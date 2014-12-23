package uk.co.traintrackapp.traintrack;

import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;

import uk.co.traintrackapp.traintrack.fragment.DashboardFragment;

import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;
import uk.co.traintrackapp.traintrack.fragment.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
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
                        .replace(R.id.container, StationsFragment.newInstance()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance()).commit();
                break;
        }
        restoreActionBar();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_dashboard_fragment);
                break;
            case 2:
                mTitle = getString(R.string.title_stations_fragment);
                break;
            case 3:
                mTitle = getString(R.string.title_journeys_fragment);
                break;
            case 4:
                mTitle = getString(R.string.title_settings_fragment);
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

}
