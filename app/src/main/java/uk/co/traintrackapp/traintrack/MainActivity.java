package uk.co.traintrackapp.traintrack;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

import uk.co.traintrackapp.traintrack.fragment.AccountManagerFragment;
import uk.co.traintrackapp.traintrack.fragment.DashboardFragment;
import uk.co.traintrackapp.traintrack.fragment.JourneysFragment;
import uk.co.traintrackapp.traintrack.fragment.NavigationDrawerFragment;
import uk.co.traintrackapp.traintrack.fragment.SettingsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationsFragment;
import uk.co.traintrackapp.traintrack.utils.BaseGameUtils;
import uk.co.traintrackapp.traintrack.utils.Utils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static int RC_SIGN_IN = 9001;
    private static int RC_ACHIEVEMENTS = 9002;

    private CharSequence mTitle;
    private TrainTrack app;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (TrainTrack) getApplication();
        app.googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

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
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            app.googleApiClient.connect();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case -1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AccountManagerFragment.newInstance()).commit();
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

    public void googleSignIn(View v) {
        Utils.log("GOOGLE SIGN IN");
        mSignInClicked = true;
        app.googleApiClient.connect();
    }

    public void googleSignOut(View v) {
        Utils.log("GOOGLE SIGN OUT");
        mExplicitSignOut = true;
        if (app.googleApiClient != null && app.googleApiClient.isConnected()) {
            Games.signOut(app.googleApiClient);
            app.googleApiClient.disconnect();
        }
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

    @Override
    public void onConnected(Bundle connectionHint) {
        Utils.log("CONNECTION SUCCESS");
        startActivityForResult(Games.Achievements.getAchievementsIntent(app.googleApiClient), RC_ACHIEVEMENTS);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utils.log("CONNECTION SUSPENDED");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Utils.log("CONNECTION FAILED");
        if (mResolvingConnectionFailure) {
            Utils.log("TRYING TO RESOLVE FAILURE");
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.unknown_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    app.googleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.unknown_error))) {
                mResolvingConnectionFailure = false;
            }
        }

    }

}
