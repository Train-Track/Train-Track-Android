package uk.co.traintrackapp.traintrack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class MainActivity extends ActionBarActivity {

    private static final String NEW_USER = "New User";
    private static final String ONLINE_USER = "Online User";
    private static final String OFFLINE_USER = "Offline User";
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Fragment newFragment;
    private String newTitle;
    private Toolbar toolbar;
    private boolean backToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newFragment = DashboardFragment.newInstance();
        newTitle = "Dashboard";
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                toggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                toggle.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                toggle.onDrawerClosed(drawerView);
                updateFragment();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                toggle.onDrawerStateChanged(newState);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        listView.setAdapter(new ArrayAdapter<>(
                getSupportActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_dashboard_fragment),
                        getString(R.string.title_stations_fragment),
                        getString(R.string.title_journeys_fragment),
                        getString(R.string.title_settings_fragment),
                }));
        listView.setItemChecked(0, true);
        Button accountButton = (Button) findViewById(R.id.button);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(-1);
            }
        });
        new LoadAssets().execute();
        updateFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public void selectItem(int position) {
        switch (position) {
            case -1:
                newFragment = AccountManagerFragment.newInstance();
                newTitle = "Account";
                break;
            case 0:
                newFragment = DashboardFragment.newInstance();
                newTitle = "Dashboard";
                break;
            case 1:
                newFragment = StationsFragment.newInstance();
                newTitle = "Stations";
                break;
            case 2:
                newFragment = JourneysFragment.newInstance();
                newTitle = "Your Journeys";
                break;
            case 3:
                newFragment = SettingsFragment.newInstance();
                newTitle = "Settings";
                break;
        }
        drawerLayout.closeDrawers();
    }

    /**
     * Commits the new fragment and updates the title
     */
    private void updateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment).commit();
        toolbar.setTitle(newTitle);
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
