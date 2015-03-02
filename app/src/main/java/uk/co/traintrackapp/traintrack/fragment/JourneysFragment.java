package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.JourneyActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.JourneyRowAdapter;
import uk.co.traintrackapp.traintrack.model.Journey;

public class JourneysFragment extends Fragment {

    public static JourneysFragment newInstance() {
        return new JourneysFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_journeys, container,
                false);
        final TrainTrack app = (TrainTrack) getActivity().getApplication();
        final ArrayList<Journey> journeys = app.getJourneys();
        final ListView list = (ListView) v.findViewById(R.id.list);
        final JourneyRowAdapter adapter = new JourneyRowAdapter(journeys, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                Journey journey = adapter.getItem(index);
                Intent intent = new Intent().setClass(getActivity(),
                        JourneyActivity.class);
                intent.putExtra("journey_uuid", journey.getUuid());
                startActivity(intent);
            }
        });

        return v;
    }

}
