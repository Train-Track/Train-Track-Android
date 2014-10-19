package dyl.anjon.es.traintrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import dyl.anjon.es.traintrack.api.CallingPoint;
import dyl.anjon.es.traintrack.api.Service;
import dyl.anjon.es.traintrack.models.Station;

public class MapActivity extends Activity {

	private GoogleMap map;
	private Station station;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Intent intent = getIntent();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (map == null) {
			finish();
		}

		int stationId = intent.getIntExtra("station_id", 0);
		station = Station.get(stationId);
		if (station != null) {
			LatLng pos = new LatLng(station.getLatitude(),
					station.getLongitude());
			map.addMarker(new MarkerOptions()
					.position(pos)
					.title(station.getName())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
		}

		String serviceId = intent.getStringExtra("service_id");
		if (serviceId != null) {
			new GetServiceRequest().execute(serviceId);
		}

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

}
