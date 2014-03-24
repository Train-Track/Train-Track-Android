package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.ContentValues;
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
	private boolean favourite;

	public Station() {
		this.setCrsCode("");
		this.setName("Unknown");
		this.setFavourite(false);
	}

	/**
	 * @param crsCode
	 * @param name
	 * @param favourite
	 */
	public Station(String crsCode, String name, boolean favourite) {
		this.setCrsCode(crsCode);
		this.setName(name);
		this.setFavourite(favourite);
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
	 * @return the favourite
	 */
	public boolean isFavourite() {
		return favourite;
	}

	/**
	 * @param favourite
	 *            the favourite to set
	 */
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
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
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("stations", new String[] { "id", "crs_code",
				"name", "latitude", "longitude", "favourite" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Station station = new Station();
		station.setId(cursor.getInt(0));
		station.setCrsCode(cursor.getString(1));
		station.setName(cursor.getString(2));
		station.setLatitude(cursor.getLong(3));
		station.setLongitude(cursor.getLong(4));
		station.setFavourite(cursor.getInt(5) == 1);
		cursor.close();
		dbh.close();

		return station;
	}

	/**
	 * @param context
	 * @return all stations
	 */
	public static ArrayList<Station> getAll(Context context) {
		ArrayList<Station> stations = new ArrayList<Station>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM stations ORDER BY name ASC",
				null);
		if (cursor.moveToFirst()) {
			do {
				Station station = new Station();
				station.setId(cursor.getInt(0));
				station.setCrsCode(cursor.getString(1));
				station.setName(cursor.getString(2));
				station.setLatitude(cursor.getLong(3));
				station.setLongitude(cursor.getLong(4));
				station.setFavourite(cursor.getInt(5) == 1);
				stations.add(station);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();
		return stations;
	}

	/**
	 * @param context
	 * @return all schedules
	 */
	public ArrayList<Schedule> getSchedules(Context context) {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("schedule_locations",
				new String[] { "schedule_id" }, "station_id = ?",
				new String[] { String.valueOf(this.getId()) }, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				schedules.add(Schedule.get(context, cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return schedules;
	}

	public Station save(Context context) {
		if (this.getId() != 0) {
			DatabaseHandler dbh = new DatabaseHandler(context);
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("crs_code", this.getCrsCode());
			values.put("name", this.getName());
			values.put("latitude", this.getLatitude());
			values.put("longitude", this.getLongitude());
			values.put("favourite", this.isFavourite());
			db.update("stations", values, "id = ?",
					new String[] { String.valueOf(this.getId()) });
		}
		return this;
	}

}
