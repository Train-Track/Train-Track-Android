package uk.co.traintrackapp.traintrack.fragment;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.StationRowAdapter;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationsListFragment extends Fragment {

    private static final String A_Z = "A-Z";
    private static final String NEARBY = "Nearby";
    private static final String FAVOURITES = "Favourites";
    private static final String RECENT = "Recent";
    private StationRowAdapter adapter;
    private ArrayList<Station> stations;
    private PullRefreshLayout refresh;
    private String tab;

    public static ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        String[] tabs = {
            A_Z,
            NEARBY,
            FAVOURITES,
            RECENT
        };
        for (String tab : tabs) {
            StationsListFragment fragment = new StationsListFragment();
            Bundle args = new Bundle();
            args.putString(Utils.ARGS_PAGE_TITLE, tab);
            fragment.setArguments(args);
            fragments.add(fragment);
        }
        return fragments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations_list, container, false);

        tab = getArguments().getString(Utils.ARGS_PAGE_TITLE);
        stations = new ArrayList<>();

        RecyclerView list = (RecyclerView) v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new StationRowAdapter(stations);
        list.setAdapter(adapter);

        refresh = (PullRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetStationList().execute();
            }
        });

        refresh.setRefreshing(true);
        new GetStationList().execute();

        return v;
    }

    class GetStationList extends AsyncTask<String, String, ArrayList<Station>> {

        @Override
        protected ArrayList<Station> doInBackground(String... strings) {
            Utils.log("Getting stations for " + tab + "...");
            ArrayList<Station> newStations = new ArrayList<>();
            TrainTrack app = (TrainTrack) getActivity().getApplication();
            switch (tab) {
                case A_Z:
                    newStations.addAll(app.getStations());
                    break;
                case NEARBY:
                    Location gps = ((StationsFragment) getParentFragment()).getGps();
                    for (Station station : app.getStations()) {
                        if ((station.getLatitude() != 0) && (station.getLongitude() != 0)) {
                            Location stationLocation = new Location("");
                            stationLocation.setLatitude(station.getLatitude());
                            stationLocation.setLongitude(station.getLongitude());
                            station.setDistance((int) gps.distanceTo(stationLocation));
                            newStations.add(station);
                        }
                    }
                    Collections.sort(newStations);
                    break;
                case FAVOURITES:
                    newStations.addAll(app.getUser().getFavouriteStations());
                    break;
                case RECENT:
                    newStations.addAll(app.getUser().getRecentStations());
                    break;
            }

            return newStations;
        }

        @Override
        protected void onPostExecute(ArrayList<Station> newStations) {
            super.onPostExecute(newStations);
            Utils.log("Got stations for " + tab + ".");
            stations.clear();
            stations.addAll(newStations);
            adapter.notifyDataSetChanged();
            refresh.setRefreshing(false);
        }
    }
}
