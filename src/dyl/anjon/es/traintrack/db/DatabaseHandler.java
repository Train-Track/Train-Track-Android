package dyl.anjon.es.traintrack.db;

import dyl.anjon.es.traintrack.utils.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "train_track";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Utils.log("Database is going to be used again");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ContentValues values;

		// create stations
		db.execSQL("CREATE TABLE stations (id INTEGER PRIMARY KEY, crs_code TEXT, name TEXT, latitude NUMERIC, longitude NUMERIC, favourite BOOLEAN)");
		// 1
		values = new ContentValues();
		values.put("name", "Cardiff Central");
		values.put("crs_code", "CDF");
		values.put("favourite", false);
		long cdf = db.insert("stations", null, values);
		// 2
		values = new ContentValues();
		values.put("name", "Newport (Gwent)");
		values.put("crs_code", "NPT");
		values.put("favourite", false);
		long npt = db.insert("stations", null, values);
		// 3
		values = new ContentValues();
		values.put("name", "Severn Tunnel Junction");
		values.put("crs_code", "STJ");
		values.put("favourite", false);
		long sjt = db.insert("stations", null, values);
		// 4
		values = new ContentValues();
		values.put("name", "Filton Abbey Wood");
		values.put("crs_code", "FAW");
		values.put("favourite", false);
		long faw = db.insert("stations", null, values);
		// 5
		values = new ContentValues();
		values.put("name", "Bristol Temple Meads");
		values.put("crs_code", "BRI");
		values.put("favourite", false);
		long bri = db.insert("stations", null, values);
		// 6
		values = new ContentValues();
		values.put("name", "Grangetown");
		values.put("crs_code", "GTN");
		values.put("favourite", false);
		long gtn = db.insert("stations", null, values);
		// 7
		values = new ContentValues();
		values.put("name", "Cogan");
		values.put("crs_code", "CGN");
		values.put("favourite", false);
		long cgn = db.insert("stations", null, values);

		// create schedules
		db.execSQL("CREATE TABLE schedules (id INTEGER PRIMARY KEY, toc_name TEXT)");
		// 1 CGN - CDF
		values = new ContentValues();
		values.put("toc_name", "Arriva Trains Wales");
		long cgn_cdf = db.insert("schedules", null, values);
		// 2 CDF - BRI
		values = new ContentValues();
		values.put("toc_name", "First Great Western");
		long cdf_bri = db.insert("schedules", null, values);

		// create schedule locations table
		db.execSQL("CREATE TABLE schedule_locations (id INTEGER PRIMARY KEY, schedule_id INTEGER, station_id INTEGER, platform TEXT, time TEXT)");
		// 1 CGN - CDF
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("station_id", cgn);
		values.put("platform", "2");
		values.put("time", "17:15");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("station_id", gtn);
		values.put("platform", "2");
		values.put("time", "17:19");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("station_id", cdf);
		values.put("platform", "6");
		values.put("time", "17:26");
		db.insert("schedule_locations", null, values);
		// 2 CDF - BRI
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("station_id", cdf);
		values.put("platform", "2");
		values.put("time", "17:30");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("station_id", npt);
		values.put("platform", "1");
		values.put("time", "17:46");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("station_id", sjt);
		values.put("platform", "2");
		values.put("time", "18:04");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("station_id", faw);
		values.put("platform", "2");
		values.put("time", "18:16");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("station_id", bri);
		values.put("platform", "11");
		values.put("time", "18:24");
		db.insert("schedule_locations", null, values);

		// create journeys
		db.execSQL("CREATE TABLE journeys (id INTEGER PRIMARY KEY, user_id INTEGER)");
		values = new ContentValues();
		values.put("user_id", "1");
		long journeyId = db.insert("journeys", null, values);
		// create journey legs table
		db.execSQL("CREATE TABLE journey_legs (id INTEGER PRIMARY KEY, journey_id INTEGER, schedule_id INTEGER, origin_station_id INTEGER, destination_station_id INTEGER, departure_time TEXT, arrival_time TEXT, departure_platform TEXT, arrival_platform TEXT)");
		// 1 CGN - CDF
		values = new ContentValues();
		values.put("journey_id", journeyId);
		values.put("schedule_id", cgn_cdf);
		values.put("origin_station_id", cgn);
		values.put("destination_station_id", cdf);
		values.put("departure_time", "17:15");
		values.put("arrival_time", "17:27");
		values.put("departure_platform", "2");
		values.put("arrival_platform", "6");
		db.insert("journey_legs", null, values);
		// 2 CDF - BRI
		values = new ContentValues();
		values.put("journey_id", journeyId);
		values.put("schedule_id", cdf_bri);
		values.put("origin_station_id", cdf);
		values.put("destination_station_id", bri);
		values.put("departure_time", "17:30");
		values.put("arrival_time", "18:24");
		values.put("departure_platform", "2");
		values.put("arrival_platform", "11");
		db.insert("journey_legs", null, values);

		// create friends
		db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, facebook_id TEXT, friend BOOLEAN)");
		values = new ContentValues();
		values.put("name", "Dylan Jones");
		values.put("facebook_id", "123456789");
		values.put("friend", false);
		db.insert("users", null, values);
		values = new ContentValues();
		values.put("name", "Alice Timms");
		values.put("facebook_id", "123456");
		values.put("friend", true);
		db.insert("users", null, values);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS schedules");
		db.execSQL("DROP TABLE IF EXISTS schedule_locations");
		db.execSQL("DROP TABLE IF EXISTS stations");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS journeys");
		db.execSQL("DROP TABLE IF EXISTS journey_legs");
		onCreate(db);
	}

}
