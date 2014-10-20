package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.models.Station;

public class ServiceItemRowAdapter extends BaseAdapter {

	private ArrayList<ServiceItem> serviceItems;
	private Station station;
	private Context context;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param station
	 *            the service is being seen from
	 */
	public ServiceItemRowAdapter(ArrayList<ServiceItem> serviceItems,
			Station station, Context context) {
		this.serviceItems = serviceItems;
		this.station = station;
		this.context = context;
	}

	public int getCount() {
		return serviceItems.size();
	}

	public ServiceItem getItem(int position) {
		return serviceItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.row_service, null);
			holder = new ViewHolder();
			holder.destination = (TextView) convertView
					.findViewById(R.id.destination);
			holder.origin = (TextView) convertView.findViewById(R.id.origin);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.platform = (TextView) convertView
					.findViewById(R.id.platform);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ServiceItem serviceItem = serviceItems.get(position);
		if (serviceItem.terminatesHere()) {
			holder.destination.setText("Terminates Here");
		} else {
			holder.destination.setText(serviceItem.getDestination().toString());
		}
		if (serviceItem.startsHere()) {
			holder.origin.setText("Starts Here");
		} else {
			holder.origin.setText("From " + serviceItem.getOrigin().toString());
		}
		// origin.setText(origin.getText() + " - " + serviceItem.getOperator());
		holder.time.setText(serviceItem.getTime());
		holder.platform.setText(serviceItem.getPlatform());

		return convertView;
	}

	static class ViewHolder {
		TextView destination;
		TextView origin;
		TextView time;
		TextView platform;
	}
}
