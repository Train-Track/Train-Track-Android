package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.JourneyRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.Station;

public class JourneysFragment extends Fragment {

	public JourneysFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_journeys, container,
				false);

		ArrayList<Journey> journeys = new ArrayList<Journey>();
		Station origin = new Station("BRI", "Bristol Temple Meads");
		Station destination = new Station("CDF", "Cardiff Central");
		JourneyLeg journeyLeg = new JourneyLeg(origin, destination);
		Journey journey = new Journey();
		journey.addJourneyLeg(journeyLeg);
		journeys.add(journey);

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final JourneyRowAdapter adapter = new JourneyRowAdapter(inflater,
				journeys);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Journey journey = (Journey) adapter.getItem(index);
				Log.i("Journey HIT", journey.toString());
				return;
			}

		});

		return rootView;
	}
}
