package dyl.anjon.es.traintrack;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dyl.anjon.es.traintrack.fragments.JourneysFragment;
import dyl.anjon.es.traintrack.fragments.StationsFragment;
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.utils.Utils;

public class MainActivity extends FragmentActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		ParseUser user = ParseUser.getCurrentUser();
		if (ParseAnonymousUtils.isLinked(user)) {
			Utils.log("User is anon");
		} else {
			Utils.log("User is " + user.getUsername());
		}

		ParseQuery<Operator> operatorQuery = ParseQuery
				.getQuery(Operator.class);
		operatorQuery.fromLocalDatastore();
		try {
			if (operatorQuery.count() == 0) {
				operatorQuery = ParseQuery.getQuery(Operator.class);
				operatorQuery.findInBackground(new FindCallback<Operator>() {
					@Override
					public void done(List<Operator> results, ParseException e) {
						if (e == null) {
							Operator.pinAllInBackground(results);
						} else {
							Utils.log("Getting operators: " + e.getMessage());
						}
					}
				});
			}
		} catch (ParseException e) {
			Utils.log("Counting local operators:: " + e.getMessage());
		}

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
			}
			return new StationsFragment();
		}

		@Override
		public int getCount() {
			return 2;
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
