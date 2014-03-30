package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Schedule {

	public static final String TABLE_NAME = "schedules";

	private int id;
	private String trainOperatingCompany;
	private ArrayList<ScheduleLocation> scheduleLocations;

	public Schedule() {
		this.setId(0);
		this.scheduleLocations = new ArrayList<ScheduleLocation>();
	}

	/**
	 * @return the schedule id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the schedule id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name of the company operating the train
	 */
	public String getTrainOperatingCompany() {
		return trainOperatingCompany;
	}

	/**
	 * @param trainOperatingCompany
	 *            the name of the company running the train
	 */
	public void setTrainOperatingCompany(String trainOperatingCompany) {
		this.trainOperatingCompany = trainOperatingCompany;
	}

	/**
	 * @return the origin
	 */
	public ScheduleLocation getOrigin(Context context) {
		if (this.scheduleLocations.isEmpty()) {
			this.scheduleLocations = this.getScheduleLocations(context);
		}
		if (this.scheduleLocations.isEmpty()) {
			Location location = new Location();
			ScheduleLocation scheduleLocation = new ScheduleLocation();
			scheduleLocation.setLocation(location);
			scheduleLocation.setTime("?");
			scheduleLocation.setPlatform("?");
			return scheduleLocation;
		}

		return this.scheduleLocations.get(0);
	}

	/**
	 * @return the destination
	 */
	public ScheduleLocation getDestination(Context context) {
		if (this.scheduleLocations.isEmpty()) {
			this.scheduleLocations = this.getScheduleLocations(context);
		}
		if (this.scheduleLocations.isEmpty()) {
			Location location = new Location();
			ScheduleLocation scheduleLocation = new ScheduleLocation();
			scheduleLocation.setLocation(location);
			scheduleLocation.setTime("?");
			scheduleLocation.setPlatform("?");
			return scheduleLocation;
		}

		int last = this.scheduleLocations.size();
		return this.scheduleLocations.get(last - 1);
	}

	/**
	 * @param location
	 * @return ScheduleLocation where the station is the station provided
	 */
	public ScheduleLocation at(Context context, Location location) {
		if (this.scheduleLocations.isEmpty()) {
			this.scheduleLocations = this.getScheduleLocations(context);
		}
		for (int i = 0; i < this.scheduleLocations.size(); i++) {
			if (this.scheduleLocations.get(i).getLocation()
					.equals(location))
				return this.scheduleLocations.get(i);
		}
		return null;
	}

	/**
	 * @return the schedule locations
	 */
	public ArrayList<ScheduleLocation> getScheduleLocations(Context context) {
		ArrayList<ScheduleLocation> scheduleLocations = new ArrayList<ScheduleLocation>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("schedule_locations", new String[] { "id",
				"schedule_id", "location_id", "time", "platform" },
				"schedule_id = ?", new String[] { String.valueOf(this.id) },
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				ScheduleLocation scheduleLocation = new ScheduleLocation();
				scheduleLocation.setId(cursor.getInt(0));
				scheduleLocation.setScheduleId(cursor.getInt(1));
				scheduleLocation.setLocationId(cursor.getInt(2));
				scheduleLocation.setLocation(Location.get(context,
						cursor.getInt(2)));
				scheduleLocation.setTime(cursor.getString(3));
				scheduleLocation.setPlatform(cursor.getString(4));
				scheduleLocations.add(scheduleLocation);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return scheduleLocations;
	}

	/**
	 * @param context
	 * @param id
	 * @return the schedule selected
	 */
	public static Schedule get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("schedules",
				new String[] { "id", "toc_name" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Schedule schedule = new Schedule();
		schedule.setId(cursor.getInt(0));
		schedule.setTrainOperatingCompany(cursor.getString(1));
		cursor.close();
		dbh.close();

		return schedule;
	}

}
