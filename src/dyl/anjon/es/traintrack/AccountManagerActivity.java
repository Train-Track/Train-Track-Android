package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.plus.Plus;

import dyl.anjon.es.traintrack.utils.BaseGameUtils;
import dyl.anjon.es.traintrack.utils.Utils;

public class AccountManagerActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

	private GoogleApiClient mGoogleApiClient;
	private static int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_manager);

		findViewById(R.id.google_sign_in_button).setOnClickListener(this);
		findViewById(R.id.google_sign_out_button).setOnClickListener(this);
		findViewById(R.id.fb_sign_in_button).setOnClickListener(this);
		findViewById(R.id.fb_sign_out_button).setOnClickListener(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API)
				.addScope(Games.SCOPE_GAMES).build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
				result, RC_SIGN_IN, getString(R.string.signin_other_error))) {
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Utils.log("onConnected called");
		findViewById(R.id.google_sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.google_logged_in).setVisibility(View.VISIBLE);
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		prefs.edit().putBoolean("linked_google_account", true).apply();
		Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
		TextView name = (TextView) findViewById(R.id.google_name);
		name.setText(p.getDisplayName());
	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		boolean linkedGoogleAccount = prefs.getBoolean("linked_google_account",
				false);
		if (linkedGoogleAccount) {
			mGoogleApiClient.reconnect();
		}
		boolean linkedFbAccount = prefs.getBoolean("linked_fb_account", false);
		if (linkedFbAccount) {
			facebookLogin();
		}

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			} else {
				BaseGameUtils.showActivityResultError(this, requestCode,
						resultCode, R.string.signin_failure,
						R.string.signin_other_error);
			}
		} else {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, intent);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.google_sign_in_button:
			googleLogin();
			break;

		case R.id.google_sign_out_button:
			googleLogout();
			break;

		case R.id.fb_sign_in_button:
			facebookLogin();
			break;

		case R.id.fb_sign_out_button:
			facebookLogout();
			break;
		}

	}

	/**
	 * Login to Facebook
	 */
	private void facebookLogin() {
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					loadFacebookData(session);
				}
			}
		});
	}

	/**
	 * Logout of current Facebook session
	 */
	private void facebookLogout() {
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation();
		}
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		prefs.edit().putBoolean("linked_fb_account", false).apply();
		findViewById(R.id.fb_sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.fb_logged_in).setVisibility(View.GONE);
	}

	/**
	 * Login to Facebook
	 */
	private void googleLogin() {
		mGoogleApiClient.reconnect();
	}

	/**
	 * Logout of current Google session
	 */
	private void googleLogout() {
		Games.signOut(mGoogleApiClient);
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		prefs.edit().putBoolean("linked_google_account", false).apply();
		findViewById(R.id.google_sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.google_logged_in).setVisibility(View.GONE);
	}

	private void loadFacebookData(Session session) {
		Request.newGraphPathRequest(session, "me", new Callback() {
			@Override
			public void onCompleted(Response response) {
				GraphObject me = response.getGraphObject();
				if (me != null) {
					findViewById(R.id.fb_sign_in_button).setVisibility(
							View.GONE);
					findViewById(R.id.fb_logged_in).setVisibility(View.VISIBLE);
					TextView name = (TextView) findViewById(R.id.fb_name);
					name.setText(me.getProperty("name").toString());
					SharedPreferences prefs = getSharedPreferences(
							"current_user", MODE_PRIVATE);
					prefs.edit().putBoolean("linked_fb_account", true).apply();
				}
			}
		}).executeAsync();
	}

}
