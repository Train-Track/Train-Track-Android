package dyl.anjon.es.traintrack;

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

public class RowAdapter extends BaseAdapter implements Filterable {

	private List<String> rowList;
	private List<String> origRowList;
	private LayoutInflater inflater = null;

	public RowAdapter(LayoutInflater inflater, ArrayList<String> strings) {
		this.rowList = strings;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public String getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		String string = rowList.get(position);
		v = inflater.inflate(R.layout.row_station, null);
		TextView title = (TextView) v.findViewById(R.id.name);
		title.setText(string);
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<String>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<String> FilteredArrList = new ArrayList<String>();

				if (origRowList == null) {
					origRowList = new ArrayList<String>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					constraint = constraint.toString().toLowerCase(
							Locale.ENGLISH);
					for (int i = 0; i < origRowList.size(); i++) {
						String string = origRowList.get(i);
						if (string.toLowerCase(Locale.UK).contains(
								constraint.toString())) {
							FilteredArrList.add(string);
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
