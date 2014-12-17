package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Station;

public class StationFragment extends Fragment {

    private Station station;

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Station activity opened", station.name);
        return inflater.inflate(R.layout.fragment_station, container, false);
    }

}