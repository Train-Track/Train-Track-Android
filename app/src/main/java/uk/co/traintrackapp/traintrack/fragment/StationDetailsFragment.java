package uk.co.traintrackapp.traintrack.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import uk.co.traintrackapp.traintrack.MapActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationDetailsFragment extends Fragment {

    private static final String PAGE_TITLE = "Details";

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, PAGE_TITLE);
        StationDetailsFragment fragment = new StationDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_details, container, false);
        StationActivity activity = ((StationActivity) getActivity());
        final Station station = activity.getStation();
        final TrainTrack app = (TrainTrack) getActivity().getApplication();
        final User user = app.getUser();

        TextView title = (TextView) v.findViewById(R.id.name);
        title.setText(station.getName());

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        if (app.getUser().getFavouriteStations().contains(station)) {
            fab.setBackgroundResource(android.R.drawable.btn_star_big_on);
        } else {
            fab.setBackgroundResource(android.R.drawable.btn_star_big_off);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getFavouriteStations().contains(station)) {
                    fab.setBackgroundResource(android.R.drawable.btn_star_big_off);
                    user.getFavouriteStations().remove(station);
                } else {
                    fab.setBackgroundResource(android.R.drawable.btn_star_big_on);
                    user.getFavouriteStations().add(station);
                }
                app.setUser(user);
                user.save(getActivity());
            }
        });

        ImageView nationalRailIcon = (ImageView) v.findViewById(R.id.national_rail_icon);
        if (station.isNationalRail()) {
            nationalRailIcon.setVisibility(View.VISIBLE);
        } else {
            nationalRailIcon.setVisibility(View.GONE);
        }

        ImageView undergroundIcon = (ImageView) v.findViewById(R.id.underground_icon);
        if (station.isUnderground()) {
            undergroundIcon.setVisibility(View.VISIBLE);
        } else {
            undergroundIcon.setVisibility(View.GONE);
        }

        TextView address = (TextView) v.findViewById(R.id.address);
        address.setText(station.getAddress());

        TextView phone = (TextView) v.findViewById(R.id.phone);
        phone.setText(station.getPhone());

        TextView twitter = (TextView) v.findViewById(R.id.twitter);
        twitter.setText(station.getTwitter());

        Button map = (Button) v.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(v.getContext(),
                        MapActivity.class);
                intent.putExtra("station_uuid", station.getUuid());
                v.getContext().startActivity(intent);
            }
        });

        final Button homeStation = (Button) v.findViewById(R.id.home_station);
        if (user.getHomeStation().equals(station)) {
            homeStation.setText("Home Station (IS)");
        }
        homeStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (user.getHomeStation().equals(station)) {
                homeStation.setText("Home Station (IS)");
                user.setHomeStation(new Station());
            } else {
                homeStation.setText(getString(R.string.home_station));
                user.setHomeStation(station);
            }
            app.setUser(user);
            user.save(getActivity());
            }
        });

        final Button workStation = (Button) v.findViewById(R.id.work_station);
        if (user.getWorkStation().equals(station)) {
            workStation.setText("Work Station (IS)");
        }
        workStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getWorkStation().equals(station)) {
                    workStation.setText("Work Station (IS)");
                    user.setWorkStation(new Station());
                } else {
                    workStation.setText(getString(R.string.work_station));
                    user.setWorkStation(station);
                }
                app.setUser(user);
                user.save(getActivity());
            }
        });

        return v;
    }

}
