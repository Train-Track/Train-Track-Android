package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;

import dyl.anjon.es.traintrack.fragments.SettingsFragment;
import dyl.anjon.es.traintrack.models.User;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_settings);
		
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

	}

	/**
	 * Logout of current Facebook session
	 */
	private void facebookLogout() {
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation();
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

	private void loadFacebookData(Session session) {
		Request.newGraphPathRequest(session, "me", new Callback() {
			@Override
			public void onCompleted(Response response) {
				GraphObject me = response.getGraphObject();
				if (me != null) {
					String name = me.getProperty("name").toString();
					Log.i("Facebook User", name);
					User user = new User(name);
					user.setFacebookId(me.getProperty("id").toString());
					user = user.save(getApplicationContext());
					SharedPreferences settings = getSharedPreferences(
							"current_user", MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("user_id", user.getId());
					editor.commit();
				}
			}
		}).executeAsync();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
