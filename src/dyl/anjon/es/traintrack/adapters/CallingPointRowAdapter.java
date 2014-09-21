package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.api.CallingPoint;
import dyl.anjon.es.traintrack.models.Location;

public class CallingPointRowAdapter extends BaseAdapter {

	private ArrayList<CallingPoint> callingPoints;
	private LayoutInflater inflater = null;
	private Location location = null;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param location
	 *            the schedule is being seen from
	 */
	public CallingPointRowAdapter(LayoutInflater inflater,
			ArrayList<CallingPoint> callingPoints, Location location) {
		this.callingPoints = callingPoints;
		this.inflater = inflater;
		this.location = location;
	}

	public int getCount() {
		return callingPoints.size();
	}

	public CallingPoint getItem(int position) {
		return callingPoints.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_calling_point, null);
		CallingPoint callingPoint = callingPoints.get(position);
		final TextView time = (TextView) v.findViewById(R.id.time);
		time.setText(callingPoint.getScheduledTime());
		final TextView station = (TextView) v.findViewById(R.id.station);
		if (callingPoint.getLocation() != null) {
			station.setText(callingPoint.getLocation().toString());
			if (callingPoint.getLocation().equals(this.location)) {
				station.setTextColor(Color.RED);
				v.setEnabled(false);
			}
		}
		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(callingPoint.getEstimatedTime());
		return v;
	}

}
