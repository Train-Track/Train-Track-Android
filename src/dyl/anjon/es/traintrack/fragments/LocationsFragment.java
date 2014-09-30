package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import dyl.anjon.es.traintrack.LocationActivity;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.LocationRowAdapter;
import dyl.anjon.es.traintrack.models.Location;

public class LocationsFragment extends Fragment {

	private android.location.Location gps;

	public LocationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_locations,
				container, false);

		final ArrayList<Location> stations = Location
				.getAllStations(getActivity());

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final LocationRowAdapter adapter = new LocationRowAdapter(inflater,
				stations);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Location location = (Location) adapter.getItem(index);
				Intent intent = new Intent().setClass(getActivity(),
						LocationActivity.class);
				intent.putExtra("location_id", location.getId());
				startActivity(intent);
				return;
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
			}
		});

		Button nearby = (Button) rootView.findViewById(R.id.nearby);
		nearby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gps == null) {
					return;
				}
				for (int i = 0; i < stations.size(); i++) {
					Location station = stations.get(i);
					float results[] = { 0, 0, 0 };
					android.location.Location.distanceBetween(
							station.getLatitude(), station.getLongitude(),
							gps.getLatitude(), gps.getLongitude(), results);
					station.setDistance(results[0]);
				}
				Collections.sort(stations, new DistanceComparator());
				adapter.refresh(stations);
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
			public void onLocationChanged(android.location.Location location) {
				updateLocation(location);
			}
		};
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		return rootView;
	}

	private void updateLocation(android.location.Location gps) {
		if (gps != null) {
			this.gps = gps;
		}
	}

	public class DistanceComparator implements Comparator<Location> {
		@Override
		public int compare(Location o1, Location o2) {
			return Float.compare(o1.getDistance(), o2.getDistance());
		}
	}

}
