package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.JourneyActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.adapter.JourneyRowAdapter;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneysFragment extends Fragment {

    private FindCallback<Journey> findJourneyCallback;
    private ArrayList<Journey> journeys;
    private ListView list;
    private JourneyRowAdapter adapter;

    public static JourneysFragment newInstance() {
        return new JourneysFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journeys, container,
                false);
        journeys = new ArrayList<>();
        list = (ListView) rootView.findViewById(R.id.list);
        adapter = new JourneyRowAdapter(inflater, journeys, getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                Journey journey = adapter.getItem(index);
                Intent intent = new Intent().setClass(getActivity(),
                        JourneyActivity.class);
                intent.putExtra("journey_id", journey.getObjectId());
                startActivity(intent);
            }
        });

        findJourneyCallback = new FindCallback<Journey>() {
            @Override
            public void done(List<Journey> results, ParseException e) {
                if ((e == null) && (results.size() > 0)) {
                    journeys.addAll(results);
                    adapter.refresh(journeys);
                    list.setSelection(0);
                    Journey.pinAllInBackground(results);
                    Utils.log("Got journeys.");
                } else if (e == null) {
                    ParseQuery<Journey> q = ParseQuery.getQuery(Journey.class);
                    q.setLimit(1000);
                    Utils.log("Getting journeys from online data store...");
                    q.findInBackground(findJourneyCallback);
                } else {
                    Utils.log(e.getMessage());
                }
            }
        };

        loadJourneys();

        return rootView;
    }

    private void loadJourneys() {
        if (journeys.isEmpty()) {
            ParseQuery<Journey> q = ParseQuery.getQuery(Journey.class);
            q.setLimit(1000);
            q.fromLocalDatastore();
            Utils.log("Getting journeys...");
            q.findInBackground(findJourneyCallback);
        } else {
            adapter.refresh(journeys);
            list.setSelection(0);
        }
    }

}
