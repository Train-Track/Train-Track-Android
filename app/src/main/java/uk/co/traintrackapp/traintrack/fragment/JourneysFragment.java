package uk.co.traintrackapp.traintrack.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.JourneyActivity;
import uk.co.traintrackapp.traintrack.MainActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.JourneyRowAdapter;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.utils.Utils;

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
        final ArrayList<Journey> journeys = app.getUser().getJourneys();
        final ListView list = (ListView) v.findViewById(R.id.list);

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.attachToListView(list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Choose a departing station", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).updateFragment(MainActivity.STATIONS_FRAGMENT);
            }
        });

        final JourneyRowAdapter adapter = new JourneyRowAdapter(journeys, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                Journey journey = adapter.getItem(index);
                Intent intent = new Intent().setClass(getActivity(),
                        JourneyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utils.ARGS_JOURNEY, journey);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return v;
    }

}
