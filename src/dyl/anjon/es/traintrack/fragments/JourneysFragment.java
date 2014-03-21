package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import dyl.anjon.es.traintrack.JourneyActivity;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.JourneyRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;

public class JourneysFragment extends Fragment {

	public JourneyRowAdapter adapter;

	public JourneysFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_journeys, container,
				false);

		ArrayList<Journey> journeys = Journey.getAll(getActivity());

		ListView list = (ListView) rootView.findViewById(R.id.list);
		adapter = new JourneyRowAdapter(inflater, journeys);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int index, long id) {
				Journey journey = (Journey) adapter.getItem(index);
				Intent intent = new Intent().setClass(getActivity(),
						JourneyActivity.class);
				intent.putExtra("journey_id", journey.getId());
				startActivity(intent);
				return;
			}

		});

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
												journey.getId());
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

		return rootView;
	}

	public void onResume() {
		super.onResume();
		ArrayList<Journey> journeys = Journey.getAll(getActivity());
		adapter.refresh(journeys);
	}
}
