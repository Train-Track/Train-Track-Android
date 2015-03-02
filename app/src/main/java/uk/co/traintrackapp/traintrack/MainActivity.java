package uk.co.traintrackapp.traintrack;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.fragment.AccountManagerFragment;
import uk.co.traintrackapp.traintrack.fragment.DashboardFragment;
import uk.co.traintrackapp.traintrack.fragment.JourneysFragment;
import uk.co.traintrackapp.traintrack.fragment.NavigationDrawerFragment;
import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private CharSequence title;
    private Fragment newFragment;

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
        title = getString(R.string.app_name);
        newFragment = DashboardFragment.newInstance();
        restoreActionBar();
        updateFragment();
        Utils.log("Loading assets...");
        new LoadAssets().execute();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case -1:
                newFragment = AccountManagerFragment.newInstance();
                break;
            case 0:
                newFragment = DashboardFragment.newInstance();
                break;
            case 1:
                newFragment = StationsFragment.newInstance();
                break;
            case 2:
                newFragment = JourneysFragment.newInstance();
                break;
            case 3:
                newFragment = SettingsFragment.newInstance();
                break;
        }
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerClosed() {
        updateFragment();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
    }

    private void updateFragment() {
        if (newFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, newFragment).commit();
            newFragment = null;
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    /**
     * Load the JSONArray from a file
     * @param filename the file to open
     * @param from either assets folder or the file system
     * @return JSON Array
     */
    private JSONArray loadJSON(String filename, int from) {
        JSONArray json = new JSONArray();
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is;
            if (from == Utils.ASSETS) {
                is = getAssets().open(filename);
            } else if (from == Utils.FILESYSTEM) {
                is = openFileInput(filename);
            } else {
                return json;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            json = new JSONArray(sb.toString());
        } catch (IOException | JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

    class LoadAssets extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... asset) {
            TrainTrack app = (TrainTrack) getApplication();

            ArrayList<Station> stations = new ArrayList<>();
            JSONArray jsonStations = loadJSON(Station.FILENAME, Utils.ASSETS);
            try {
                for (int i = 0; i < jsonStations.length(); i++) {
                    Station station = new Station(jsonStations.getJSONObject(i));
                    stations.add(station);
                }
                app.setStations(stations);
            } catch (JSONException e) {
               Utils.log(e.getMessage());
            }

            ArrayList<Journey> journeys = new ArrayList<>();
            JSONArray jsonJourneys = loadJSON(Journey.FILENAME, Utils.FILESYSTEM);
            try {
                for (int i = 0; i < jsonJourneys.length(); i++) {
                    Journey journey = new Journey(jsonJourneys.getJSONObject(i));
                    journeys.add(journey);
                }
                app.setJourneys(journeys);
                Utils.log(journeys.toString());
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }

            return "Everything is loaded";
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Utils.log(message);
        }
    }

}
