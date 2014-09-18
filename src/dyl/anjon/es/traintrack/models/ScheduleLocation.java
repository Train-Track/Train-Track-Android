package dyl.anjon.es.traintrack.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;


public class ScheduleLocation {

	public static final String TABLE_NAME = "schedule_locations";

	private int id;
	private int scheduleId;
	private int locationId;
	private Location location;
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
	 * @return the location ID
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param location
	 *            ID this entry is apart of
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
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
	 * @return the location name
	 */
	@Override
	public String toString() {
		return this.getLocation().toString();
	}
	
	/**
	 * @param context
	 * @param id
	 * @return the schedule selected
	 */
	public static ScheduleLocation get(int id) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME,
				new String[] { "id", "schedule_id", "location_id", "platform", "time" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		ScheduleLocation scheduleLocation = new ScheduleLocation();
		scheduleLocation.setId(cursor.getInt(0));
		scheduleLocation.setScheduleId(cursor.getInt(1));
		scheduleLocation.setLocationId(cursor.getInt(2));
		scheduleLocation.setLocation(Location.get(cursor.getInt(2)));
		scheduleLocation.setPlatform(cursor.getString(3));
		scheduleLocation.setTime(cursor.getString(4));
		cursor.close();
		dbh.close();

		return scheduleLocation;
	}

}
