package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationDetailsFragment extends Fragment {

    private static final String ARGS_UUID = "ARGS_UUID";
    private Station station;

    public static Fragment newInstance(String uuid) {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, "Details");
        args.putString(ARGS_UUID, uuid);
        StationDetailsFragment fragment = new StationDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uuid = getArguments().getString(ARGS_UUID);
        TrainTrack app = (TrainTrack) getActivity().getApplication();
        station = app.getStation(uuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_details, container, false);
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

        TextView address = (TextView) v.findViewById(R.id.address);
        address.setText(station.getAddress());

        TextView phone = (TextView) v.findViewById(R.id.phone);
        phone.setText(station.getPhone());

        TextView twitter = (TextView) v.findViewById(R.id.twitter);
        twitter.setText(station.getTwitter());

        return v;
    }

}
