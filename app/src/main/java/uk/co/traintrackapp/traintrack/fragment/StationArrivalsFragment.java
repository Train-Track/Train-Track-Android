package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationArrivalsFragment extends Fragment {

    private static final String ARGS_UUID = "ARGS_UUID";
    private String pageTitle;
    private Station station;

    public static Fragment newInstance(String uuid) {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, "Arrivals");
        args.putString(ARGS_UUID, uuid);
        StationArrivalsFragment fragment = new StationArrivalsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageTitle = getArguments().getString(Utils.ARGS_PAGE_TITLE);
        String uuid = getArguments().getString(ARGS_UUID);
        TrainTrack app = (TrainTrack) getActivity().getApplication();
        station = app.getStation(uuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_details, container, false);
        TextView title = (TextView) v.findViewById(R.id.name);
        title.setText(station.getName());
        TextView uuid = (TextView) v.findViewById(R.id.uuid);
        uuid.setText(station.getUuid());
        return v;
    }

}
