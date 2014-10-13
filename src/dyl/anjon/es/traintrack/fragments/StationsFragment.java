package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.StationActivity;
import dyl.anjon.es.traintrack.adapters.StationRowAdapter;
import dyl.anjon.es.traintrack.models.Station;

public class StationsFragment extends Fragment {

	private Location gps;

	public StationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_stations, container,
				false);

		final ArrayList<Station> stations = Station.getAll();

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final StationRowAdapter adapter = new StationRowAdapter(inflater,
				stations);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Station station = (Station) adapter.getItem(index);
				Intent intent = new Intent().setClass(getActivity(),
						StationActivity.class);
				intent.putExtra("station_id", station.getId());
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
					Station station = stations.get(i);
					float results[] = { 0, 0, 0 };
					Location.distanceBetween(station.getLatitude(),
							station.getLongitude(), gps.getLatitude(),
							gps.getLongitude(), results);
					station.setDistance(results[0]);
				}
				Collections.sort(stations, new DistanceComparator());
				adapter.refresh(stations);
			}

		});

		Button favourites = (Button) rootView.findViewById(R.id.favourites);
		favourites.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.getFavouriteFilter().filter(null);
			}
		});

		Button aZ = (Button) rootView.findViewById(R.id.a_z);
		aZ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.refresh(Station.getAll());
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

	public class DistanceComparator implements Comparator<Station> {
		@Override
		public int compare(Station o1, Station o2) {
			return Float.compare(o1.getDistance(), o2.getDistance());
		}
	}

}
