package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.fragment.StationArrivalsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDeparturesFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDetailsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationUndergroundFragment;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationActivity extends ActionBarActivity {

    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        final Intent intent = getIntent();
        final String stationUuid = intent.getStringExtra("station_uuid");
        TrainTrack app = (TrainTrack) getApplication();
        station = app.getStation(stationUuid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(station.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                return true;
            case R.id.map:
                Intent intent = new Intent().setClass(this, MapActivity.class);
                intent.putExtra("station_uuid", station.getUuid());
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (getParent() == null) {
                setResult(Activity.RESULT_OK);
            } else {
                getParent().setResult(Activity.RESULT_OK);
            }
            finish();
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(StationDetailsFragment.newInstance(station.getUuid()));
            if (station.isNationalRail()) {
                fragments.add(StationDeparturesFragment.newInstance(station.getUuid()));
                fragments.add(StationArrivalsFragment.newInstance(station.getUuid()));
            }
            if (station.isUnderground()) {
                fragments.add(StationUndergroundFragment.newInstance(station.getUuid()));
            }
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getArguments().getString(Utils.ARGS_PAGE_TITLE);
        }
    }

}
