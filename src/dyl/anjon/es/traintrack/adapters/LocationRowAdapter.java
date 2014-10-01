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
import dyl.anjon.es.traintrack.models.Location;

public class LocationRowAdapter extends BaseAdapter implements Filterable {

	private List<Location> rowList;
	private List<Location> origRowList;
	private LayoutInflater inflater = null;

	public LocationRowAdapter(LayoutInflater inflater,
			ArrayList<Location> locations) {
		this.rowList = locations;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public Location getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_station, null);
		Location location = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(location.getName());
		TextView crsCode = (TextView) v.findViewById(R.id.crs_code);
		crsCode.setText(location.getCrsCode());
		if (location.getDistance() > 0) {
			TextView distance = (TextView) v.findViewById(R.id.distance);
			distance.setText(location.getDistanceText());
		}
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<Location>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<Location> list = new ArrayList<Location>();

				if (origRowList == null) {
					origRowList = new ArrayList<Location>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					String string = constraint.toString()
							.toLowerCase(Locale.ENGLISH).toString();
					for (int i = 0; i < origRowList.size(); i++) {
						Location location = origRowList.get(i);
						if (location.isNameSimilarTo(string)) {
							list.add(location);
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

	public void refresh(ArrayList<Location> locations) {
		this.rowList = locations;
		this.notifyDataSetChanged();
	}

}
