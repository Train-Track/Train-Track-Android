package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import dyl.anjon.es.traintrack.JourneyActivity;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.JourneyRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.utils.Utils;

public class JourneysFragment extends Fragment {

	public JourneyRowAdapter adapter;

	public JourneysFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_journeys, container,
				false);

		final ArrayList<Journey> journeys = new ArrayList<Journey>();
		ListView list = (ListView) rootView.findViewById(R.id.list);
		adapter = new JourneyRowAdapter(inflater, journeys);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				Journey journey = (Journey) adapter.getItem(index);
				Intent intent = new Intent().setClass(getActivity(),
						JourneyActivity.class);
				intent.putExtra("journey_id", journey.getObjectId());
				startActivity(intent);
				return;
			}

		});
/*
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int index, long id) {
				final Journey journey = (Journey) adapter.getItem(index);
				new AlertDialog.Builder(getActivity())
						.setTitle("Journey")
						.setMessage(journey.toString())
						.setNegativeButton("Back", null)
						.setNeutralButton("Edit",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent().setClass(
												getActivity(),
												JourneyActivity.class);
										intent.putExtra("journey_id",
												journey.getObjectId());
										startActivity(intent);
									}
								})
						.setPositiveButton("Delete",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										boolean success = journey
												.delete(getActivity());
										if (success) {
											Toast.makeText(getActivity(),
													"Journey was deleted",
													Toast.LENGTH_SHORT).show();
											ArrayList<Journey> journeys = Journey
													.getAll(getActivity());
											adapter.refresh(journeys);
										}
									}
								}).show();
				return false;
			}
		});
*/

		ParseQuery<Journey> query = ParseQuery.getQuery(Journey.class);
		query.fromLocalDatastore();
		try {
			if (query.count() == 0) {
				query = ParseQuery.getQuery(Journey.class);
			}
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		query.findInBackground(new FindCallback<Journey>() {
			@Override
			public void done(List<Journey> results, ParseException e) {
				journeys.addAll(results);
				adapter.refresh(journeys);
				Journey.pinAllInBackground(results);
			}
		});

		return rootView;
	}

}
