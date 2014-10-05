package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Station {

	public static final String TABLE_NAME = "stations";

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

	private float distance;

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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * @return the distance in a nice format
	 */
	public String getDistanceText() {
		if (getDistance() > 0) {
			double distance = getDistance() / 1000;
			if (distance > 100) {
				return String.format(Locale.getDefault(), "%.0fkm", distance);
			} else if (distance > 0) {
				return String.format(Locale.getDefault(), "%.2fkm", distance);
			}
		}
		return "";

	}

	/**
	 * @param string
	 *            to check against
	 * @return true or false
	 */
	public boolean isNameSimilarTo(String string) {
		return (getName().toLowerCase(Locale.UK).contains(string))
				|| (getCrsCode().toLowerCase(Locale.UK).contains(string));
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
	public static Station get(int id) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "crs_code",
				"name", "latitude", "longitude", "favourite" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Station location = new Station();
		location.setId(cursor.getInt(0));
		location.setCrsCode(cursor.getString(1));
		location.setName(cursor.getString(2));
		location.setLatitude(cursor.getLong(3));
		location.setLongitude(cursor.getLong(4));
		location.setFavourite(cursor.getInt(5) == 1);
		cursor.close();
		dbh.close();

		return location;
	}

	/**
	 * @param context
	 * @param id
	 * @return the station selected
	 */
	public static Station getByCrs(String crs) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "crs_code",
				"name", "latitude", "longitude", "favourite" }, "crs_code = ?",
				new String[] { String.valueOf(crs) }, null, null, null, null);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			return null;
		}

		Station location = new Station();
		location.setId(cursor.getInt(0));
		location.setCrsCode(cursor.getString(1));
		location.setName(cursor.getString(2));
		location.setLatitude(cursor.getDouble(3));
		location.setLongitude(cursor.getDouble(4));
		location.setFavourite(cursor.getInt(5) == 1);
		cursor.close();
		dbh.close();

		return location;
	}

	/**
	 * @param context
	 * @return all stations
	 */
	public static ArrayList<Station> getAll() {
		ArrayList<Station> locations = new ArrayList<Station>();

		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " ORDER BY name ASC", null);
		if (cursor.moveToFirst()) {
			do {
				Station location = new Station();
				location.setId(cursor.getInt(0));
				location.setCrsCode(cursor.getString(1));
				location.setName(cursor.getString(2));
				location.setLatitude(cursor.getDouble(3));
				location.setLongitude(cursor.getDouble(4));
				location.setFavourite(cursor.getInt(5) == 1);
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

	public Station save() {
		if (this.getId() != 0) {
			DatabaseHandler dbh = new DatabaseHandler();
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("crs_code", this.getCrsCode());
			values.put("name", this.getName());
			values.put("latitude", this.getLatitude());
			values.put("longitude", this.getLongitude());
			values.put("favourite", this.isFavourite());
			db.update(TABLE_NAME, values, "id = ?",
					new String[] { String.valueOf(this.getId()) });
			dbh.close();
		}
		return this;
	}

}
