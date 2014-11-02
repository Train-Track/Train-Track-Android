package dyl.anjon.es.traintrack;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import dyl.anjon.es.traintrack.api.CallingPoint;
import dyl.anjon.es.traintrack.api.Service;
import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.utils.Utils;

public class MapActivity extends Activity {

	private GoogleMap map;
	private HashMap<Marker, Station> hashmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		hashmap = new HashMap<Marker, Station>();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		map.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				String serviceId = getIntent().getStringExtra("service_id");
				if (serviceId != null) {
					new GetServiceRequest().execute(serviceId);
				} else {
					new GetMapMarkers().execute();
				}
			}
		});
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent intent = new Intent().setClass(getApplicationContext(),
						StationActivity.class);
				intent.putExtra("station_id", hashmap.get(marker).getId());
				startActivity(intent);
			}
		});

	}

	class GetServiceRequest extends AsyncTask<String, String, Service> {

		@Override
		protected Service doInBackground(String... service) {
			return Service.getByServiceId(service[0]);
		}

		@Override
		protected void onPostExecute(Service s) {
			super.onPostExecute(s);
			ArrayList<CallingPoint> callingPoints = s.getCallingPoints();
			LatLng[] points = new LatLng[callingPoints.size()];
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			for (int i = 0; i < callingPoints.size(); i++) {
				CallingPoint callingPoint = callingPoints.get(i);
				Station station = callingPoint.getStation();
				LatLng pos = new LatLng(station.getLatitude(),
						station.getLongitude());
				points[i] = pos;
				builder.include(pos);
				map.addMarker(new MarkerOptions()
						.position(pos)
						.title(station.getName())
						.snippet(callingPoint.getScheduledTime())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher))
						.visible(true));
			}

			map.addPolyline(new PolylineOptions().add(points).width(12)
					.color(Color.RED));
			map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
					100));
		}
	}

	class GetMapMarkers extends AsyncTask<String, String, ArrayList<Station>> {

		@Override
		protected ArrayList<Station> doInBackground(String... service) {
			ArrayList<Station> stations = new ArrayList<Station>();
			int stationId = getIntent().getIntExtra("station_id", 0);
			if (stationId > 0) {
				stations.add(Station.get(stationId));
			}
			boolean allStations = getIntent().getBooleanExtra("all_stations",
					false);
			if (allStations) {
				stations.addAll(Station.getAll());
			}
			return stations;
		}

		@Override
		protected void onPostExecute(ArrayList<Station> stations) {
			super.onPostExecute(stations);
			BitmapDescriptor icon = BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher);
			Utils.log("Starting the loop");
			for (Station s : stations) {
				LatLng pos = new LatLng(s.getLatitude(), s.getLongitude());
				Marker m = map.addMarker(new MarkerOptions().position(pos)
						.title(s.getName())
						.snippet("View Arrival/Departure Board").icon(icon));
				hashmap.put(m, s);
				Utils.log("Ending the loop");
			}
		}
	}

}
