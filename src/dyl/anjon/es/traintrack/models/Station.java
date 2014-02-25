package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Station {

	private int id;
	private String crsCode;
	private String name;
	private long latitude;
	private long longitude;

	/**
	 * @param crsCode
	 * @param name
	 */
	public Station(String crsCode, String name) {
		this.setCrsCode(crsCode);
		this.setName(name);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the crsCode
	 */
	public String getCrsCode() {
		return crsCode;
	}

	/**
	 * @param crsCode
	 *            the crsCode to set
	 */
	public void setCrsCode(String crsCode) {
		this.crsCode = crsCode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the latitude
	 */
	public long getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public long getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * @return true or false
	 */
	@Override
	public boolean equals(Object station) {
		if (this.toString().equals(station.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param context
	 * @param id
	 * @return the station selected
	 */
	public static Station get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		Cursor cursor = db.query("stations", new String[] { "id", "crs_code", "name" },
				 "id = ?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Station station = new Station(cursor.getString(1),
				cursor.getString(2));
		station.setId(cursor.getInt(0));

		return station;
	}
	
	
	/**
	 * @param context
	 * @return all stations
	 */
	public static ArrayList<Station> getAll(Context context) {
		ArrayList<Station> stations = new ArrayList<Station>();
	
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM stations", null);
		if (cursor.moveToFirst()) {
			do {
				Station station = new Station(cursor.getString(1),
						cursor.getString(2));
				station.setId(cursor.getInt(0));
				stations.add(station);
			} while (cursor.moveToNext());
		}
		return stations;
	}

	
	/**
	 * @param context
	 * @return all stations
	 */
	public ArrayList<Schedule> getSchedules(Context context) {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
	
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		Cursor cursor = db.query("schedule_locations", new String[] { "schedule_id" },
				 "station_id = ?", new String[] { String.valueOf(this.getId()) }, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				schedules.add(Schedule.get(context, cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		
		
		return schedules;
	}

}
