package dyl.anjon.es.traintrack;

import java.util.ArrayList;

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
		
		ArrayList<String> stringList = new ArrayList<String>();
		stringList.add(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));
		for (int i = 0; i< 30; i++) {
			stringList.add(i + "Cardiff Central");
		}
		
		ListView list = (ListView) rootView.findViewById(R.id.list);
		final RowAdapter adapter = new RowAdapter(inflater, stringList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				String string = (String) adapter.getItem(index);
				Log.i("STATION HIT", string);
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
