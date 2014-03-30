package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.ScheduleLocation;
import dyl.anjon.es.traintrack.models.Location;

public class ScheduleRowAdapter extends BaseAdapter {

	private ArrayList<Schedule> schedules;
	private LayoutInflater inflater = null;
	private Location location;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param location
	 *            the schedule is being seen from
	 */
	public ScheduleRowAdapter(LayoutInflater inflater,
			ArrayList<Schedule> schedules, Location location) {
		this.schedules = schedules;
		this.inflater = inflater;
		this.location = location;
	}

	public int getCount() {
		return schedules.size();
	}

	public Schedule getItem(int position) {
		return schedules.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final View v = inflater.inflate(R.layout.row_schedule, null);
		final Schedule schedule = schedules.get(position);
		final ScheduleLocation scheduleLocation = schedule.at(
				inflater.getContext(), location);

		final TextView destination = (TextView) v
				.findViewById(R.id.destination);
		ScheduleLocation destinationScheduleLocation = schedule
				.getDestination(inflater.getContext());
		if (destinationScheduleLocation.getLocation().equals(location)) {
			destination.setText("Terminates Here");
		} else {
			destination.setText(destinationScheduleLocation.toString());
		}

		final TextView origin = (TextView) v.findViewById(R.id.origin);
		ScheduleLocation originScheduleLocation = schedule.getOrigin(inflater
				.getContext());
		if (originScheduleLocation.getLocation().equals(location)) {
			origin.setText("Starts Here");
		} else {
			origin.setText("From " + originScheduleLocation.toString());
		}

		final TextView time = (TextView) v.findViewById(R.id.time);
		time.setText(scheduleLocation.getTime());

		final TextView platform = (TextView) v.findViewById(R.id.platform);
		platform.setText(scheduleLocation.getPlatform());

		return v;
	}
}
