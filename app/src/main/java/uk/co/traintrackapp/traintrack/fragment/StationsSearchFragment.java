package uk.co.traintrackapp.traintrack.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.StationRowAdapter;
import uk.co.traintrackapp.traintrack.model.Station;

public class StationsSearchFragment extends Fragment {

    public static Fragment newInstance() {
        return new StationsSearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stations_search, container, false);

        TrainTrack app = (TrainTrack) getActivity().getApplication();
        ArrayList<Station> stations = app.getStations();

        final RecyclerView list = (RecyclerView) v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        final StationRowAdapter adapter = new StationRowAdapter(stations);
        list.setAdapter(adapter);

        final EditText search = (EditText) v.findViewById(R.id.search);
        search.requestFocus();
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

        return v;
    }

}
