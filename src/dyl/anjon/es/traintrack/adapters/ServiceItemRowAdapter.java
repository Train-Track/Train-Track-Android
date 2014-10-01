package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.models.Location;

//TO replace scheduleRowAdapter
public class ServiceItemRowAdapter extends BaseAdapter {

	private ArrayList<ServiceItem> serviceItems;
	private LayoutInflater inflater = null;
	private Location location;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param location
	 *            the schedule is being seen from
	 */
	public ServiceItemRowAdapter(LayoutInflater inflater,
			ArrayList<ServiceItem> serviceItems, Location location) {
		this.serviceItems = serviceItems;
		this.inflater = inflater;
		this.location = location;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		final View v = inflater.inflate(R.layout.row_service, null);
		final ServiceItem serviceItem = serviceItems.get(position);

		final TextView destination = (TextView) v
				.findViewById(R.id.destination);
		if (serviceItem.getDestination().equals(location)) {
			destination.setText("Terminates Here");
		} else {
			destination.setText(serviceItem.getDestination().toString());
		}

		final TextView origin = (TextView) v.findViewById(R.id.origin);
		if (serviceItem.getOrigin().equals(location)) {
			origin.setText("Starts Here");
		} else {
			origin.setText("From " + serviceItem.getOrigin().toString());
		}
		origin.setText(origin.getText() + " - " + serviceItem.getOperator());

		final TextView time = (TextView) v.findViewById(R.id.time);
		if (serviceItem.getScheduledTimeDeparture() != null) {
			time.setText(serviceItem.getScheduledTimeDeparture());
		} else {
			time.setText(serviceItem.getScheduledTimeArrival());
		}

		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(serviceItem.getPlatform());

		return v;
	}
}
