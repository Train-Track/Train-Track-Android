package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dyl.anjon.es.traintrack.models.Station;

public class MapActivity extends Activity {

	private GoogleMap map;
	private Station station;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		final Intent intent = getIntent();
		final int stationId = intent.getIntExtra("station_id", 0);
		station = Station.get(stationId);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (map != null) {
			LatLng position = new LatLng(station.getLatitude(),
					station.getLongitude());
			map.addMarker(new MarkerOptions()
					.position(position)
					.title(station.getName())
					.snippet("Hellllo!!")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
		}

	}

}
