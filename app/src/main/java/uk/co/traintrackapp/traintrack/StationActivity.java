package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.fragment.StationArrivalsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDeparturesFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDetailsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationUndergroundFragment;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.StationBoard;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationActivity extends AppCompatActivity {

    private Station station = new Station();
    private StationBoard departuresBoard;
    private StationBoard arrivalsBoard;
    private StationBoard undergroundBoard;

    public Station getStation() {
        return station;
    }

    public StationBoard getDeparturesBoard() {
        return departuresBoard;
    }

    public void setDeparturesBoard(StationBoard departuresBoard) {
        this.departuresBoard = departuresBoard;
    }

    public StationBoard getArrivalsBoard() {
        return arrivalsBoard;
    }

    public void setArrivalsBoard(StationBoard arrivalsBoard) {
        this.arrivalsBoard = arrivalsBoard;
    }

    public StationBoard getUndergroundBoard() {
        return undergroundBoard;
    }

    public void setUndergroundBoard(StationBoard undergroundBoard) {
        this.undergroundBoard = undergroundBoard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        final Intent intent = getIntent();
        station = (Station) intent.getExtras().getSerializable(Utils.ARGS_STATION);

        if (station == null) {
            finish();
            return;
        }

        TrainTrack app = (TrainTrack) getApplication();
        User user = app.getUser();
        user.addRecentStation(station);
        user.save(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(station.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        private PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(StationDetailsFragment.newInstance());
            if (station.isNationalRail()) {
                fragments.add(StationDeparturesFragment.newInstance());
                fragments.add(StationArrivalsFragment.newInstance());
            }
            if (station.isUnderground()) {
                fragments.add(StationUndergroundFragment.newInstance());
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
