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
import org.json.JSONObject;

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
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String NEW_USER = "New User";
    private static final String ONLINE_USER = "Online User";
    private static final String OFFLINE_USER = "Offline User";
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
     * Load the JSON from a file
     * @param filename the file to open
     * @param from either assets folder or the file system
     * @return JSON String
     */
    private String loadJSON(String filename, int from) {
        String jsonString = "";
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is;
            if (from == Utils.ASSETS) {
                is = getAssets().open(filename);
            } else if (from == Utils.FILESYSTEM) {
                is = openFileInput(filename);
            } else {
                return jsonString;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            jsonString = sb.toString();
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }
        Utils.log("OPENED: " + jsonString);
        return jsonString;
    }

    class LoadAssets extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... asset) {
            TrainTrack app = (TrainTrack) getApplication();
            String returnCode = NEW_USER;

            ArrayList<Station> stations = new ArrayList<>();
            try {
                JSONArray jsonStations = new JSONArray(loadJSON(Station.FILENAME, Utils.ASSETS));
                for (int i = 0; i < jsonStations.length(); i++) {
                    Station station = new Station(jsonStations.getJSONObject(i));
                    stations.add(station);
                }
            } catch (JSONException e) {
               Utils.log(e.getMessage());
            }
            app.setStations(stations);

            User user = new User();
            try {
                JSONObject jsonUser = new JSONObject(loadJSON(User.FILENAME, Utils.FILESYSTEM));
                user = new User(jsonUser);
                if (user.getId() > 0) {
                    returnCode = ONLINE_USER;
                } else if (user.getId() == 0) {
                    returnCode = OFFLINE_USER;
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
                returnCode = NEW_USER;
            }
            app.setUser(user);

            return returnCode;
        }

        @Override
        protected void onPostExecute(String returnCode) {
            super.onPostExecute(returnCode);
            Utils.log("The return code is: " + returnCode);
        }
    }

}
