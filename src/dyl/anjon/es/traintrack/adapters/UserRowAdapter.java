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
import dyl.anjon.es.traintrack.models.User;

public class UserRowAdapter extends BaseAdapter implements Filterable {

	private List<User> rowList;
	private List<User> origRowList;
	private LayoutInflater inflater = null;

	public UserRowAdapter(LayoutInflater inflater, ArrayList<User> users) {
		this.rowList = users;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public User getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_user, null);
		User user = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(user.toString());
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<User>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<User> FilteredArrList = new ArrayList<User>();

				if (origRowList == null) {
					origRowList = new ArrayList<User>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					constraint = constraint.toString().toLowerCase(
							Locale.ENGLISH);
					for (int i = 0; i < origRowList.size(); i++) {
						User user = origRowList.get(i);
						if (user.getName().toLowerCase(Locale.UK)
								.contains(constraint.toString())) {
							FilteredArrList.add(user);
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
