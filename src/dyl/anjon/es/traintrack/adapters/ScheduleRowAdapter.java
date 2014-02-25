package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.Station;

public class ScheduleRowAdapter extends BaseAdapter {

	private ArrayList<Schedule> schedules;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param schedules to be displayed
	 * @param station the schedule is being seen from
	 */
	public ScheduleRowAdapter(LayoutInflater inflater,
			ArrayList<Schedule> schedules, Station station) {
		this.schedules = schedules;
		this.inflater = inflater;
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
		Schedule schedule = schedules.get(position);
		final TextView destination = (TextView) v.findViewById(R.id.destination);
		destination.setText(schedule.getDestination(inflater.getContext()).toString());
		return v;
	}

}
