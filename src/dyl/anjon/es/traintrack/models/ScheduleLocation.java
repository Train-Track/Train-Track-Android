package dyl.anjon.es.traintrack.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;


public class ScheduleLocation {

	private int id;
	private int scheduleId;
	private int stationId;
	private Station station;
	private String time;
	private String platform;

	public ScheduleLocation() {
	}

	/**
	 * @return the ID of the schedule location
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the schedule ID
	 */
	public int getScheduleId() {
		return scheduleId;
	}

	/**
	 * @param schedule
	 *            ID this entry is apart of
	 */
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	/**
	 * @return the station ID
	 */
	public int getStationId() {
		return stationId;
	}

	/**
	 * @param station
	 *            ID this entry is apart of
	 */
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the station
	 */
	public Station getStation() {
		return station;
	}

	/**
	 * @param station
	 *            the station to set
	 */
	public void setStation(Station station) {
		this.station = station;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param string
	 *            the time to set
	 */
	public void setTime(String string) {
		this.time = string;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform
	 *            the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * @return the station name
	 */
	@Override
	public String toString() {
		return this.getStation().toString();
	}
	
	/**
	 * @param context
	 * @param id
	 * @return the schedule selected
	 */
	public static ScheduleLocation get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("schedule_locations",
				new String[] { "id", "schedule_id", "station_id", "platform", "time" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		ScheduleLocation scheduleLocation = new ScheduleLocation();
		scheduleLocation.setId(cursor.getInt(0));
		scheduleLocation.setScheduleId(cursor.getInt(1));
		scheduleLocation.setStationId(cursor.getInt(2));
		scheduleLocation.setStation(Station.get(context, cursor.getInt(2)));
		scheduleLocation.setPlatform(cursor.getString(3));
		scheduleLocation.setTime(cursor.getString(4));
		dbh.close();

		return scheduleLocation;
	}

}
