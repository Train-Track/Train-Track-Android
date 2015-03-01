package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.MapActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.StationRowAdapter;
import uk.co.traintrackapp.traintrack.model.Station;


public class StationsFragment extends Fragment {

    private Location gps;
    private ArrayList<Station> stations;
    private ListView list;
    private StationRowAdapter adapter;

    public static StationsFragment newInstance() {
        return new StationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stations, container,
                false);
        final TrainTrack app = (TrainTrack) getActivity().getApplication();
        stations = app.getStations();
        list = (ListView) rootView.findViewById(R.id.list);
        adapter = new StationRowAdapter(inflater, stations, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                Station station = adapter.getItem(index);
                Intent intent = new Intent().setClass(getActivity(),
                        StationActivity.class);
                intent.putExtra("station_id", station.getId());
                intent.putExtra("station_crs", station.getCrsCode());
                intent.putExtra("station_name", station.getName());
                startActivity(intent);
            }
        });

        EditText search = (EditText) rootView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence search, int arg1, int arg2,
                                      int arg3) {
                adapter.getFilter().filter(search);
                list.smoothScrollToPosition(0);
            }
        });

        Button aZ = (Button) rootView.findViewById(R.id.a_z);
        aZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stations = app.getStations();
                adapter.refresh(stations);
                list.setSelection(0);
            }
        });

        Button nearby = (Button) rootView.findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps == null) {
                    return;
                }
                adapter.getNearbyFilter().filter(
                        gps.getLatitude() + "," + gps.getLongitude());
                list.setSelection(0);
            }
        });

        Button favourites = (Button) rootView.findViewById(R.id.favourites);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFavouriteFilter().filter(null);
                list.setSelection(0);
            }
        });

        Button map = (Button) rootView.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getActivity(),
                        MapActivity.class);
                intent.putExtra("all_stations", true);
                startActivity(intent);
            }
        });

        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        updateLocation(locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER));

        LocationListener locationListener = new LocationListener() {
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }
        };
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return rootView;
    }

    private void updateLocation(Location gps) {
        if (gps != null) {
            this.gps = gps;
        }
    }

}
