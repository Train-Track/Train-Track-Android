package dyl.anjon.es.traintrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import dyl.anjon.es.traintrack.fragments.JourneysFragment;
import dyl.anjon.es.traintrack.fragments.StationsFragment;
import dyl.anjon.es.traintrack.fragments.UsersFragment;
import dyl.anjon.es.traintrack.models.User;
import dyl.anjon.es.traintrack.utils.Utils;

public class MainActivity extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences settings = getSharedPreferences("current_user",
				MODE_PRIVATE);
		int userId = settings.getInt("user_id", 1);
		Utils session = Utils.getSession();
		session.setContext(getApplicationContext());
		User user = User.get(userId);
		session.setUser(user);
		if (session.getUser() != null) {
			Utils.log("The current logged in user is: "
					+ session.getUser().getName());
		} else {
			Utils.log("No one is logged in!");
		}

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
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.add_journey:
			mViewPager.setCurrentItem(0);
			Toast.makeText(getApplicationContext(),
					"Choose departing station...", Toast.LENGTH_LONG).show();
			return true;
		case R.id.settings:
			intent = new Intent().setClass(getApplicationContext(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.linked_accounts:
			intent = new Intent().setClass(getApplicationContext(),
					AccountManagerActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
			switch (position) {
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
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
