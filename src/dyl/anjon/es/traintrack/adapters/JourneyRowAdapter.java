package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.Journey;

public class JourneyRowAdapter extends BaseAdapter {

	private List<Journey> rowList;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param journeys
	 */
	public JourneyRowAdapter(LayoutInflater inflater,
			ArrayList<Journey> journeys) {
		this.rowList = journeys;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public Journey getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_journey, null);
		Journey journey = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(journey.getOrigin(inflater.getContext()).toString() + " to "
				+ journey.getDestination(inflater.getContext()).toString());
		return v;
	}
	
	public void refresh(ArrayList<Journey> journeys) {
		this.rowList = journeys;
		this.notifyDataSetChanged();
	}

}
