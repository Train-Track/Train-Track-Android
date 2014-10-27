package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
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

		// Create the Google Api Client with access to Plus and Games
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API)
				.addScope(Games.SCOPE_GAMES)
				// add other APIs and scopes here as needed
				.build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Utils.log("CONNECTION FAILED: " + result.toString());
		// Attempt to resolve the connection failure using BaseGameUtils.
		// The R.string.signin_other_error value should reference a generic
		// error string in your strings.xml file, such as "There was
		// an issue with sign-in, please try again later."
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
		Utils.log("Connected!");

		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
		Utils.log("Player: " + p.getDisplayName());
		Games.Achievements.unlock(mGoogleApiClient,
				getString(R.string.achievement_first_train));
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Utils.log("onActivityResult called!");
		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			} else {
				// Bring up an error dialog to alert the user that sign-in
				// failed. The R.string.signin_failure should reference an error
				// string in your strings.xml file that tells the user they
				// could not be signed in, such as "Unable to sign in."
				BaseGameUtils.showActivityResultError(this, requestCode,
						resultCode, R.string.signin_failure,
						R.string.signin_other_error);
				Utils.log("ERROR: " + resultCode);
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			mGoogleApiClient.connect();
		} else if (view.getId() == R.id.sign_out_button) {
			// sign out.
			Games.signOut(mGoogleApiClient);

			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		}

	}

}
