package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.models.Schedule;

public class ScheduleRowAdapter extends BaseAdapter {

	private ArrayList<Schedule> rowList;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param schedules
	 */
	public ScheduleRowAdapter(LayoutInflater inflater,
			ArrayList<Schedule> schedules) {
		this.rowList = schedules;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public Schedule getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_schedule, null);
		Schedule schedule = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(schedule.toString());
		return v;
	}

}
