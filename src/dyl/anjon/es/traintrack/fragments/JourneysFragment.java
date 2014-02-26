package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import dyl.anjon.es.traintrack.JourneyActivity;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.JourneyRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;

public class JourneysFragment extends Fragment {

	public JourneysFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_journeys, container,
				false);

		ArrayList<Journey> journeys = Journey.getAll(getActivity());

		ListView list = (ListView) rootView.findViewById(R.id.list);
		final JourneyRowAdapter adapter = new JourneyRowAdapter(inflater,
				journeys);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Journey journey = (Journey) adapter.getItem(index);
				Intent intent = new Intent().setClass(getActivity(),
						JourneyActivity.class);
				intent.putExtra("journey_id", journey.getId());
				startActivity(intent);
				return;
			}

		});

		return rootView;
	}
}
