package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.Station;

public class StationRowAdapter extends BaseAdapter implements Filterable {

	private List<Station> rowList;
	private List<Station> origRowList;
	private LayoutInflater inflater = null;

	public StationRowAdapter(LayoutInflater inflater,
			ArrayList<Station> stations) {
		this.rowList = stations;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public Station getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_station, null);
		Station station = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(station.getName());
		TextView crsCode = (TextView) v.findViewById(R.id.crs_code);
		crsCode.setText(station.getCrsCode());
		if (station.getDistance() > 0) {
			TextView distance = (TextView) v.findViewById(R.id.distance);
			distance.setText(station.getDistanceText());
		}
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<Station>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<Station> list = new ArrayList<Station>();

				if (origRowList == null) {
					origRowList = new ArrayList<Station>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					String string = constraint.toString()
							.toLowerCase(Locale.ENGLISH).toString();
					for (int i = 0; i < origRowList.size(); i++) {
						Station station = origRowList.get(i);
						if (station.isNameSimilarTo(string)) {
							list.add(station);
						}
					}
					results.count = list.size();
					results.values = list;
				}
				return results;
			}
		};
		return filter;

	}

	public void refresh(ArrayList<Station> stations) {
		this.rowList = stations;
		this.notifyDataSetChanged();
	}

}