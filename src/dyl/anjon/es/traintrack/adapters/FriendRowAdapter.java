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
import dyl.anjon.es.traintrack.models.Friend;

public class FriendRowAdapter extends BaseAdapter implements Filterable {

	private List<Friend> rowList;
	private List<Friend> origRowList;
	private LayoutInflater inflater = null;

	public FriendRowAdapter(LayoutInflater inflater, ArrayList<Friend> friends) {
		this.rowList = friends;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public Friend getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_friend, null);
		Friend friend = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(friend.toString());
		return v;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				rowList = (List<Friend>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<Friend> FilteredArrList = new ArrayList<Friend>();

				if (origRowList == null) {
					origRowList = new ArrayList<Friend>(rowList);
				}

				if (constraint == null || constraint.length() == 0) {
					results.count = origRowList.size();
					results.values = origRowList;
				} else {
					constraint = constraint.toString().toLowerCase(
							Locale.ENGLISH);
					for (int i = 0; i < origRowList.size(); i++) {
						Friend friend = origRowList.get(i);
						if (friend.getName().toLowerCase(Locale.UK)
								.contains(constraint.toString())) {
							FilteredArrList.add(friend);
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
