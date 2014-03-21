package dyl.anjon.es.traintrack;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;

import dyl.anjon.es.traintrack.fragments.JourneysFragment;
import dyl.anjon.es.traintrack.fragments.StationsFragment;
import dyl.anjon.es.traintrack.fragments.UsersFragment;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem login = menu.findItem(R.id.login);
		MenuItem logout = menu.findItem(R.id.logout);
		if (Session.getActiveSession() == null) {
			login.setEnabled(true).setVisible(true);
			logout.setEnabled(false).setVisible(false);
		} else {
			logout.setEnabled(true).setVisible(true);
			login.setEnabled(false).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_journey:
			mViewPager.setCurrentItem(0);
			Toast.makeText(getApplicationContext(),
					"Choose departing station...", Toast.LENGTH_LONG).show();
			return true;
		case R.id.settings:
			Intent intent = new Intent().setClass(getApplicationContext(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.login:
			facebookLogin();
			return true;
		case R.id.logout:
			facebookLogout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new StationsFragment();
			case 1:
				return new JourneysFragment();
			case 2:
				return new UsersFragment();
			}
			return new StationsFragment();
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
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

}
