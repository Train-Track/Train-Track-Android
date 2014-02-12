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
import dyl.anjon.es.traintrack.models.TrainStation;

public class TrainStationRowAdapter extends BaseAdapter implements Filterable {

	private List<TrainStation> rowList;
	private List<TrainStation> origRowList;
	private LayoutInflater inflater = null;

	public TrainStationRowAdapter(LayoutInflater inflater,
			ArrayList<TrainStation> stations) {
		this.rowList = stations;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public TrainStation getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_station, null);
		TrainStation station = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(station.getName());
		TextView crsCode = (TextView) v.findViewById(R.id.crs_code);
		crsCode.setText(station.getCrsCode());
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<TrainStation>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<TrainStation> FilteredArrList = new ArrayList<TrainStation>();

				if (origRowList == null) {
					origRowList = new ArrayList<TrainStation>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					constraint = constraint.toString().toLowerCase(
							Locale.ENGLISH);
					for (int i = 0; i < origRowList.size(); i++) {
						TrainStation station = origRowList.get(i);
						if (station.getName().toLowerCase(Locale.UK)
								.contains(constraint.toString())) {
							FilteredArrList.add(station);
						}
					}
					results.count = FilteredArrList.size();
					results.values = FilteredArrList;
				}
				return results;
			}
		};
		return filter;

	}

}
