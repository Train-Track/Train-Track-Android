package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

import dyl.anjon.es.traintrack.utils.BaseGameUtils;
import dyl.anjon.es.traintrack.utils.Utils;

public class GoogleLoginActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

	private GoogleApiClient mGoogleApiClient;
	private static int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google);

		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API)
				.addScope(Games.SCOPE_GAMES).build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Utils.log("CONNECTION FAILED: " + result.toString());
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
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		prefs.edit().putBoolean("linked_google_account", true).apply();
	}

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences prefs = getSharedPreferences("current_user",
				MODE_PRIVATE);
		boolean linkedGoogleAccount = prefs.getBoolean("linked_google_account",
				false);
		if (linkedGoogleAccount) {
			mGoogleApiClient.connect();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Utils.log("onActivityResult called!");
		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			} else {
				BaseGameUtils.showActivityResultError(this, requestCode,
						resultCode, R.string.signin_failure,
						R.string.signin_other_error);
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			mGoogleApiClient.connect();
		} else if (view.getId() == R.id.sign_out_button) {
			Games.signOut(mGoogleApiClient);
			SharedPreferences prefs = getSharedPreferences("current_user",
					MODE_PRIVATE);
			prefs.edit().putBoolean("linked_google_account", false).apply();
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		}

	}

}
