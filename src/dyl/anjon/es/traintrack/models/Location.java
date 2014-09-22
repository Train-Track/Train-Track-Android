package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Location {

	public static final String TABLE_NAME = "locations";

	private int id;

	@SerializedName("crs")
	private String crsCode;

	@SerializedName("name")
	private String name;

	@SerializedName("lat")
	private double latitude;

	@SerializedName("lng")
	private double longitude;

	private boolean favourite;

	@SerializedName("station")
	private boolean station;

	private float distance;

	public Location() {
		this.setCrsCode("");
		this.setName("Unknown");
		this.setFavourite(false);
	}

	/**
	 * @param crsCode
	 * @param name
	 * @param favourite
	 */
	public Location(String crsCode, String name, boolean favourite) {
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
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
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
	 * @return true if a public UK station
	 */
	public boolean isStation() {
		return station;
	}

	/**
	 * @param station
	 *            set to true if a public UK station
	 */
	public void setStation(boolean station) {
		this.station = station;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
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
	public static Location get(int id) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "crs_code",
				"name", "latitude", "longitude", "favourite", "station" },
				"id = ?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Location location = new Location();
		location.setId(cursor.getInt(0));
		location.setCrsCode(cursor.getString(1));
		location.setName(cursor.getString(2));
		location.setLatitude(cursor.getLong(3));
		location.setLongitude(cursor.getLong(4));
		location.setFavourite(cursor.getInt(5) == 1);
		location.setStation(cursor.getInt(6) == 1);
		cursor.close();
		dbh.close();

		return location;
	}

	/**
	 * @param context
	 * @param id
	 * @return the station selected
	 */
	public static Location getByCrs(String crs) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "crs_code",
				"name", "latitude", "longitude", "favourite", "station" },
				"crs_code = ?", new String[] { String.valueOf(crs) }, null,
				null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Location location = new Location();
		location.setId(cursor.getInt(0));
		location.setCrsCode(cursor.getString(1));
		location.setName(cursor.getString(2));
		location.setLatitude(cursor.getLong(3));
		location.setLongitude(cursor.getLong(4));
		location.setFavourite(cursor.getInt(5) == 1);
		location.setStation(cursor.getInt(6) == 1);
		cursor.close();
		dbh.close();

		return location;
	}

	/**
	 * @param context
	 * @return all stations
	 */
	public static ArrayList<Location> getAllStations(Context context) {
		ArrayList<Location> locations = new ArrayList<Location>();

		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE station = 1 ORDER BY name ASC", null);
		if (cursor.moveToFirst()) {
			do {
				Location location = new Location();
				location.setId(cursor.getInt(0));
				location.setCrsCode(cursor.getString(1));
				location.setName(cursor.getString(2));
				location.setLatitude(cursor.getLong(3));
				location.setLongitude(cursor.getLong(4));
				location.setFavourite(cursor.getInt(5) == 1);
				location.setStation(cursor.getInt(6) == 1);
				locations.add(location);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();
		return locations;
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
				new String[] { "schedule_id" }, "location_id = ?",
				new String[] { String.valueOf(this.getId()) }, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				schedules.add(Schedule.get(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return schedules;
	}

	public Location save(Context context) {
		if (this.getId() != 0) {
			DatabaseHandler dbh = new DatabaseHandler();
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("crs_code", this.getCrsCode());
			values.put("name", this.getName());
			values.put("latitude", this.getLatitude());
			values.put("longitude", this.getLongitude());
			values.put("favourite", this.isFavourite());
			values.put("station", this.isStation());
			db.update(TABLE_NAME, values, "id = ?",
					new String[] { String.valueOf(this.getId()) });
		}
		return this;
	}

}
