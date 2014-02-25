package dyl.anjon.es.traintrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "train_track";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ContentValues values;

		// create schedules
		db.execSQL("CREATE TABLE schedules (id INTEGER PRIMARY KEY, toc_name TEXT)");
		values = new ContentValues();
		values.put("toc_name", "Arriva Trains Wales");
		db.insert("schedules", null, values);

		// create schedule locations table
		db.execSQL("CREATE TABLE schedule_locations (id INTEGER PRIMARY KEY, schedule_id INTEGER, station_id INTEGER, platform TEXT, time TEXT)");
		values = new ContentValues();
		values.put("schedule_id", 1);
		values.put("station_id", 1);
		values.put("platform", "3B");
		values.put("time", "22:01");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", 1);
		values.put("station_id", 2);
		values.put("platform", "9");
		values.put("time", "23:01");
		db.insert("schedule_locations", null, values);

		// create stations
		db.execSQL("CREATE TABLE stations (id INTEGER PRIMARY KEY, crs_code TEXT, name TEXT, latitude NUMERIC, longitude NUMERIC)");
		values = new ContentValues();
		values.put("name", "Cardiff Central");
		values.put("crs_code", "CDF");
		db.insert("stations", null, values);
		values = new ContentValues();
		values.put("name", "Cogan");
		values.put("crs_code", "CGN");
		db.insert("stations", null, values);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS schedules");
		db.execSQL("DROP TABLE IF EXISTS schedule_locations");
		db.execSQL("DROP TABLE IF EXISTS stations");
		onCreate(db);
	}

}
