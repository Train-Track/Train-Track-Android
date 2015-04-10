package uk.co.traintrackapp.traintrack.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations, container,
                false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabsStrip.setViewPager(viewPager);

/*
        final EditText search = (EditText) rootView.findViewById(R.id.search);
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

        return v;
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
