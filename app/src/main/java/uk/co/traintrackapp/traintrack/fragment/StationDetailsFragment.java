package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Station;
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
                if (app.getUser().getFavouriteStations().contains(station)) {
                    fab.setBackgroundResource(android.R.drawable.btn_star_big_off);
                    app.getUser().getFavouriteStations().remove(station);
                } else {
                    fab.setBackgroundResource(android.R.drawable.btn_star_big_on);
                    app.getUser().getFavouriteStations().add(station);
                }
                app.getUser().save(getActivity());
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

        return v;
    }

}
