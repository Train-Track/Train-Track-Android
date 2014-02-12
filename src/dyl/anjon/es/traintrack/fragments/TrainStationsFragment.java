package dyl.anjon.es.traintrack.fragments;

import java.util.ArrayList;

import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.adapters.TrainStationRowAdapter;
import dyl.anjon.es.traintrack.models.TrainStation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class TrainStationsFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public TrainStationsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.train_stations_fragment,
				container, false);
		
		ArrayList<TrainStation> stations = new ArrayList<TrainStation>();
		stations.add(new TrainStation("CDF", "Cardiff Central"));
		
		
		ListView list = (ListView) rootView.findViewById(R.id.list);
		final TrainStationRowAdapter adapter = new TrainStationRowAdapter(inflater, stations);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				TrainStation station = (TrainStation) adapter.getItem(index);
				Log.i("STATION HIT", station.getName());
				return;
			}

		});

		EditText search = (EditText) rootView.findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence search, int arg1, int arg2,
					int arg3) {
				adapter.getFilter().filter(search);
			}
		});

		return rootView;
	}
}
