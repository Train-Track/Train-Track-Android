package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import uk.co.traintrackapp.traintrack.adapter.ServiceItemRowAdapter;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.fragment.StationArrivalsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDeparturesFragment;
import uk.co.traintrackapp.traintrack.fragment.StationDetailsFragment;
import uk.co.traintrackapp.traintrack.fragment.StationUndergroundFragment;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationActivity extends ActionBarActivity {

    private Station station;
    private ServiceItemRowAdapter adapter;
    private ArrayList<ServiceItem> serviceItems;
    private ProgressBar progress;
    private TextView nrccMessage;
    static final int TAKE_PHOTO = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        final Intent intent = getIntent();
        final String journeyUuid = intent.getStringExtra("journey_uuid");
        final String stationUuid = intent.getStringExtra("station_uuid");
        TrainTrack app = (TrainTrack) getApplication();
        station = app.getStation(stationUuid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(station.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);


/*


        progress = (ProgressBar) findViewById(R.id.progress);
        nrccMessage = (TextView) findViewById(R.id.nrcc_messages);
        serviceItems = new ArrayList<>();
        adapter = new ServiceItemRowAdapter(serviceItems, station, this);

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                ServiceItem serviceItem = adapter.getItem(index);
                Intent intent = new Intent().setClass(getApplicationContext(),
                        ServiceActivity.class);
                intent.putExtra("journey_uuid", journeyUuid);
                intent.putExtra("service_id", serviceItem.getServiceId());
                intent.putExtra("origin_name", serviceItem.getOrigin().getName());
                intent.putExtra("station_uuid", station.getUuid());
                intent.putExtra("station_name", station.getName());
                intent.putExtra("destination_name", serviceItem.getDestination().getName());
                intent.putExtra("operator_code", serviceItem.getOperator().getCode());
                intent.putExtra("operator_name", serviceItem.getOperator().getName());
                intent.putExtra("platform", serviceItem.getPlatform());
                intent.putExtra("time", serviceItem.getTime());
                startActivityForResult(intent, 1);
            }

        });

        new GetBoardRequest().execute(station.getUuid());
*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station, menu);
        TrainTrack app = (TrainTrack) getApplication();
        if (app.getUser().getFavouriteStations().contains(station)) {
            menu.findItem(R.id.favourite).setIcon(
                    android.R.drawable.btn_star_big_on);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TrainTrack app = (TrainTrack) getApplication();
        switch (item.getItemId()) {
            case R.id.favourite:
                if (app.getUser().getFavouriteStations().contains(station)) {
                    item.setIcon(android.R.drawable.btn_star_big_off);
                    app.getUser().getFavouriteStations().remove(station);
                } else {
                    item.setIcon(android.R.drawable.btn_star_big_on);
                    app.getUser().getFavouriteStations().add(station);
                }
                app.getUser().save(this);
                return true;
            case R.id.refresh:
                new GetBoardRequest().execute(station.getUuid());
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
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //TODO do something with the bytes
        } else if (resultCode == Activity.RESULT_OK) {
            if (getParent() == null) {
                setResult(Activity.RESULT_OK);
            } else {
                getParent().setResult(Activity.RESULT_OK);
            }
            finish();
        }
    }

    class GetBoardRequest extends AsyncTask<String, String, StationBoard> {

        @Override
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting station board...");
            if (station.isUnderground()) {
                return StationBoard.getUnderground(uuid[0]);
            } else if (station.isNationalRail()) {
                return StationBoard.getDepartures(uuid[0]);
            }
            return new StationBoard();
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got station board.");
            progress.setVisibility(View.GONE);
            serviceItems.clear();
            if (!station.isUnderground()) {
                serviceItems.addAll(board.getTrainServices());
            } else {
                Utils.log(board.getTubeLines().toString());
                serviceItems.addAll(board.getTubeLines().get(0).getServices());
            }
            adapter.notifyDataSetChanged();

            ArrayList<String> nrccMessages = board.getNrccMessages();
            if (nrccMessages.size() > 0) {
                nrccMessage.setVisibility(View.VISIBLE);
                nrccMessage.setText(nrccMessages.get(0));
            }

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
