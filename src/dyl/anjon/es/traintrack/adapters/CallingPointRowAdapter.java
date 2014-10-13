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
import dyl.anjon.es.traintrack.models.Station;

public class CallingPointRowAdapter extends BaseAdapter {

	private ArrayList<CallingPoint> callingPoints;
	private LayoutInflater inflater = null;
	private Station station = null;

	/**
	 * @param inflater
	 * @param callingPoints
	 *            to be displayed
	 * @param station
	 *            the station it is being seen from
	 */
	public CallingPointRowAdapter(LayoutInflater inflater,
			ArrayList<CallingPoint> callingPoints, Station station) {
		this.callingPoints = callingPoints;
		this.inflater = inflater;
		this.station = station;
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
		final TextView icon = (TextView) v.findViewById(R.id.icon);
		icon.setText(callingPoint.getIcon());
		final TextView time = (TextView) v.findViewById(R.id.time);
		time.setText(callingPoint.getScheduledTime());
		final TextView station = (TextView) v.findViewById(R.id.station);
		if (callingPoint.getStation() != null) {
			station.setText(callingPoint.getStation().toString());
			if (callingPoint.getStation().equals(this.station)) {
				station.setTextColor(Color.RED);
				v.setEnabled(false);
			}
		}
		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(callingPoint.getEstimatedTime());
		return v;
	}
}
