package uk.co.traintrackapp.traintrack.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationsFragment extends Fragment {

    private Location gps;

    public Location getGps() {
        return gps;
    }

    public static StationsFragment newInstance() {
        return new StationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations, container,
                false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

/*
        final Button map = (Button) rootView.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getActivity(),
                        MapActivity.class);
                intent.putExtra("all_stations", true);
                startActivity(intent);
            }
        });
*/

        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {

            gps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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
                    if (location != null) {
                        gps = location;
                    }
                }
            };
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.stations, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, StationsSearchFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<android.support.v4.app.Fragment> fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.addAll(StationsListFragment.getFragments());
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getArguments().getString(Utils.ARGS_PAGE_TITLE);
        }
    }

}
