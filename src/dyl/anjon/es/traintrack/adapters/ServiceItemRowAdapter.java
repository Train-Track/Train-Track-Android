package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.models.Station;

//TO replace scheduleRowAdapter
public class ServiceItemRowAdapter extends BaseAdapter {

	private ArrayList<ServiceItem> serviceItems;
	private LayoutInflater inflater = null;
	private Station station;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param station
	 *            the service is being seen from
	 */
	public ServiceItemRowAdapter(LayoutInflater inflater,
			ArrayList<ServiceItem> serviceItems, Station station) {
		this.serviceItems = serviceItems;
		this.inflater = inflater;
		this.station = station;
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
		if (serviceItem.terminatesHere()) {
			destination.setText("Terminates Here");
		} else {
			destination.setText(serviceItem.getDestination().toString());
		}

		final TextView origin = (TextView) v.findViewById(R.id.origin);
		if (serviceItem.startsHere()) {
			origin.setText("Starts Here");
		} else {
			origin.setText("From " + serviceItem.getOrigin().toString());
		}
		origin.setText(origin.getText() + " - " + serviceItem.getOperator());

		final TextView time = (TextView) v.findViewById(R.id.time);
		time.setText(serviceItem.getTime());

		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(serviceItem.getPlatform());

		return v;
	}
}
