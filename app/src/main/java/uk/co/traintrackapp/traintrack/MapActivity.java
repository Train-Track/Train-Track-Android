package uk.co.traintrackapp.traintrack;

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

import uk.co.traintrackapp.traintrack.api.CallingPoint;
import uk.co.traintrackapp.traintrack.api.Service;
import uk.co.traintrackapp.traintrack.model.Station;

public class MapActivity extends Activity {

    private GoogleMap map;
    private HashMap<Marker, Station> hashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        hashmap = new HashMap<>();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(54.47942,
                -4.232974)));
        map.animateCamera(CameraUpdateFactory.zoomTo(5));
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
                Station station = hashmap.get(marker);
                intent.putExtra("station_id", station.getId());
                intent.putExtra("station_name", station.getName());
                intent.putExtra("station_crs", station.getCrsCode());
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
            if (s == null) {
                return;
            }
            ArrayList<CallingPoint> callingPoints = s.getCallingPoints();
            LatLng[] points = new LatLng[callingPoints.size()];
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < callingPoints.size(); i++) {
                CallingPoint callingPoint = callingPoints.get(i);
                Station station = callingPoint.getStation();
                if (station != null) {
                    points[i] = new LatLng(station.getLatitude(),
                            station.getLongitude());
                } else {
                    points[i] = new LatLng(0, 0);
                }
                builder.include(points[i]);
                Marker m = map.addMarker(new MarkerOptions()
                        .position(points[i])
                        .title(callingPoint.getStation().toString())
                        .snippet(callingPoint.getScheduledTime())
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.ic_launcher))
                        .visible(true));
                hashmap.put(m, station);
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
            TrainTrack app = (TrainTrack) getApplication();
            ArrayList<Station> stations = new ArrayList<>();

            String stationCrs = getIntent().getStringExtra("station_crs");
            if (stationCrs != null) {
                stations.add(app.getStation(stationCrs));
            }

            boolean all = getIntent().getBooleanExtra("all_stations", false);
            if (all) {
                stations = app.getStations();
            }
            return stations;
        }

        @Override
        protected void onPostExecute(ArrayList<Station> stations) {
            super.onPostExecute(stations);
            BitmapDescriptor icon = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_launcher);
            for (Station s : stations) {
                LatLng pos = new LatLng(s.getLatitude(), s.getLongitude());
                Marker m = map.addMarker(new MarkerOptions().position(pos)
                        .title(s.getName())
                        .snippet("View Arrival/Departure Board").icon(icon));
                hashmap.put(m, s);
            }
            if (stations.size() == 1) {
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                        stations.get(0).getLatitude(), stations.get(0)
                        .getLongitude())));
                map.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

}
