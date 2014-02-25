package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.ScheduleLocation;
import dyl.anjon.es.traintrack.models.Station;

public class ScheduleLocationRowAdapter extends BaseAdapter {

	private ArrayList<ScheduleLocation> scheduleLocations;
	private LayoutInflater inflater = null;
	private Station station = null;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param station
	 *            the schedule is being seen from
	 */
	public ScheduleLocationRowAdapter(LayoutInflater inflater,
			ArrayList<ScheduleLocation> scheduleLocations, Station station) {
		this.scheduleLocations = scheduleLocations;
		this.inflater = inflater;
		this.station = station;
	}

	public int getCount() {
		return scheduleLocations.size();
	}

	public ScheduleLocation getItem(int position) {
		return scheduleLocations.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final View v = inflater.inflate(R.layout.row_schedule_location, null);
		ScheduleLocation scheduleLocation = scheduleLocations.get(position);
		final TextView time = (TextView) v.findViewById(R.id.time);
		time.setText(scheduleLocation.getTime().toString());
		final TextView station = (TextView) v.findViewById(R.id.station);
		station.setText(scheduleLocation.getStation().toString());
		if (scheduleLocation.getStation().equals(this.station)) {
			station.setTextColor(Color.RED);
		}
		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(scheduleLocation.getPlatform().toString());
		return v;
	}

}
