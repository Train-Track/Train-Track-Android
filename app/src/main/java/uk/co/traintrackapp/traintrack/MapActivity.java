package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.traintrackapp.traintrack.model.CallingPoint;
import uk.co.traintrackapp.traintrack.model.Service;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.Tiploc;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class MapActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap map;
    private HashMap<Marker, Station> stationMarkers;
    private BitmapDescriptor nationalRailIcon;
    private BitmapDescriptor undergroundIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        nationalRailIcon = BitmapDescriptorFactory.fromResource(R.drawable.rail);
        undergroundIcon = BitmapDescriptorFactory.fromResource(R.drawable.tube);
        stationMarkers = new HashMap<>();

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Call ActivityCompat#requestPermissions
            return;
        } else {
            map.setMyLocationEnabled(true);
        }

        Service service = (Service) getIntent().getExtras().getSerializable(Utils.ARGS_SERVICE);
        if (service != null) {
            drawService(service);
        } else {
            new GetMapMarkers().execute();
        }

        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent().setClass(getApplicationContext(),
                        StationActivity.class);
                Station station = stationMarkers.get(marker);
                if (station != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Utils.ARGS_STATION, station);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    private void drawService(Service service) {
        List<CallingPoint> callingPoints = service.getCallingPoints();
        List<LatLng> line = new ArrayList<>();
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (CallingPoint callingPoint : callingPoints) {
            Station station = callingPoint.getStation();
            Tiploc tiploc = callingPoint.getTiploc();
            if ((station != null) && (station.getLatitude() != null) && (station.getLongitude() != null)) {
                LatLng point = new LatLng(station.getLatitude(), station.getLongitude());
                line.add(point);
                bounds.include(point);
                Marker m = map.addMarker(new MarkerOptions()
                        .position(point)
                        .title(station.toString())
                        .snippet(callingPoint.getScheduledTime().toString("HH:mm"))
                        .visible(true));
                stationMarkers.put(m, station);
            } else if ((tiploc != null) && (tiploc.getLatitude() != null) && (tiploc.getLongitude() != null)) {
                LatLng point = new LatLng(tiploc.getLatitude(), tiploc.getLongitude());
                line.add(point);
                bounds.include(point);
                Marker m = map.addMarker(new MarkerOptions()
                        .position(point)
                        .title(tiploc.toString())
                        .snippet(callingPoint.getScheduledTime().toString("HH:mm"))
                        .visible(true));
                stationMarkers.put(m, null);
            }

            //TODO Make these icons the right size
                /*
                if (callingPoint.getStation().isUnderground()) {
                    m.setIcon(undergroundIcon);
                } else {
                    m.setIcon(nationalRailIcon);
                }
                */
        }
        map.addPolyline(new PolylineOptions().add(line.toArray(new LatLng[line.size()])).width(12)
                .color(Color.RED));
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),
                100));
    }

    private class GetMapMarkers extends AsyncTask<String, String, ArrayList<Station>> {

        @Override
        protected ArrayList<Station> doInBackground(String... service) {
            TrainTrack app = (TrainTrack) getApplication();
            ArrayList<Station> stations = new ArrayList<>();

            String stationUuid = getIntent().getStringExtra("station_uuid");
            if (stationUuid != null) {
                stations.add(app.getStation(stationUuid));
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
            for (Station s : stations) {
                LatLng pos = new LatLng(s.getLatitude(), s.getLongitude());
                Marker m = map.addMarker(new MarkerOptions().position(pos)
                        .title(s.getName())
                        .snippet("View Arrival/Departure Board"));
                if (s.isUnderground()) {
                    m.setIcon(undergroundIcon);
                } else {
                    m.setIcon(nationalRailIcon);
                }
                stationMarkers.put(m, s);
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
