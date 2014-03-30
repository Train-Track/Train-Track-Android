package dyl.anjon.es.traintrack.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ListIterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dyl.anjon.es.traintrack.models.Location;
import dyl.anjon.es.traintrack.utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "train_track";
	private Context context;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		Utils.log("Database is going to be used");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		//Locations
		db.execSQL("CREATE TABLE " + Location.TABLE_NAME + 
				" (id INTEGER PRIMARY KEY," +
				" crs_code TEXT," +
				" name TEXT," +
				" latitude NUMERIC," +
				" longitude NUMERIC," +
				" favourite BOOLEAN," +
				" station BOOLEAN)");
		
		Gson gson = new Gson();
		InputStream is = null;
		try {
			is = this.context.getAssets().open("locations.json");
		} catch (IOException e) {
			Utils.log(e.getMessage());
		}
		Reader reader = new InputStreamReader(is);
		List<Location> locations = gson.fromJson(reader, new TypeToken<List<Location>>() {}.getType());
		ListIterator<Location> it = locations.listIterator();
		while(it.hasNext()) {
			Location location = it.next();
			ContentValues values = new ContentValues();
			values.put("id", location.getId());
			values.put("crs_code", location.getCrsCode());
			values.put("name", location.getName());
			values.put("favourite", location.isFavourite());
			values.put("station", location.isStation());
			db.insert(Location.TABLE_NAME, null, values);
		}

		
		
		
		ContentValues values;
		

		// create stations
		// 1
		values = new ContentValues();
		values.put("name", "Cardiff Central");
		values.put("crs_code", "CDF");
		values.put("favourite", false);
		values.put("station", true);
		long cdf = db.insert(Location.TABLE_NAME, null, values);
		// 2
		values = new ContentValues();
		values.put("name", "Newport (Gwent)");
		values.put("crs_code", "NPT");
		values.put("favourite", false);
		values.put("station", true);
		long npt = db.insert(Location.TABLE_NAME, null, values);
		// 3
		values = new ContentValues();
		values.put("name", "Severn Tunnel Junction");
		values.put("crs_code", "STJ");
		values.put("favourite", false);
		values.put("station", true);
		long sjt = db.insert(Location.TABLE_NAME, null, values);
		// 4
		values = new ContentValues();
		values.put("name", "Filton Abbey Wood");
		values.put("crs_code", "FAW");
		values.put("favourite", false);
		values.put("station", true);
		long faw = db.insert(Location.TABLE_NAME, null, values);
		// 5
		values = new ContentValues();
		values.put("name", "Bristol Temple Meads");
		values.put("crs_code", "BRI");
		values.put("favourite", false);
		values.put("station", true);
		long bri = db.insert(Location.TABLE_NAME, null, values);
		// 6
		values = new ContentValues();
		values.put("name", "Grangetown");
		values.put("crs_code", "GTN");
		values.put("favourite", false);
		values.put("station", true);
		long gtn = db.insert(Location.TABLE_NAME, null, values);
		// 7
		values = new ContentValues();
		values.put("name", "Cogan");
		values.put("crs_code", "CGN");
		values.put("favourite", false);
		values.put("station", true);
		long cgn = db.insert(Location.TABLE_NAME, null, values);

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
		db.execSQL("CREATE TABLE schedule_locations (id INTEGER PRIMARY KEY, schedule_id INTEGER, location_id INTEGER, platform TEXT, time TEXT)");
		// 1 CGN - CDF
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("location_id", cgn);
		values.put("platform", "2");
		values.put("time", "17:15");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("location_id", gtn);
		values.put("platform", "2");
		values.put("time", "17:19");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cgn_cdf);
		values.put("location_id", cdf);
		values.put("platform", "6");
		values.put("time", "17:26");
		db.insert("schedule_locations", null, values);
		// 2 CDF - BRI
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("location_id", cdf);
		values.put("platform", "2");
		values.put("time", "17:30");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("location_id", npt);
		values.put("platform", "1");
		values.put("time", "17:46");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("location_id", sjt);
		values.put("platform", "2");
		values.put("time", "18:04");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("location_id", faw);
		values.put("platform", "2");
		values.put("time", "18:16");
		db.insert("schedule_locations", null, values);
		values = new ContentValues();
		values.put("schedule_id", cdf_bri);
		values.put("location_id", bri);
		values.put("platform", "11");
		values.put("time", "18:24");
		db.insert("schedule_locations", null, values);

		// create journeys
		db.execSQL("CREATE TABLE journeys (id INTEGER PRIMARY KEY, user_id INTEGER)");
		values = new ContentValues();
		values.put("user_id", "1");
		long journeyId = db.insert("journeys", null, values);
		// create journey legs table
		db.execSQL("CREATE TABLE journey_legs (id INTEGER PRIMARY KEY, journey_id INTEGER, schedule_id INTEGER, origin_id INTEGER, destination_id INTEGER, departure_time TEXT, arrival_time TEXT, departure_platform TEXT, arrival_platform TEXT)");
		// 1 CGN - CDF
		values = new ContentValues();
		values.put("journey_id", journeyId);
		values.put("schedule_id", cgn_cdf);
		values.put("origin_id", cgn);
		values.put("destination_id", cdf);
		values.put("departure_time", "17:15");
		values.put("arrival_time", "17:27");
		values.put("departure_platform", "2");
		values.put("arrival_platform", "6");
		db.insert("journey_legs", null, values);
		// 2 CDF - BRI
		values = new ContentValues();
		values.put("journey_id", journeyId);
		values.put("schedule_id", cdf_bri);
		values.put("origin_id", cdf);
		values.put("destination_id", bri);
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
		db.execSQL("DROP TABLE IF EXISTS locations");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS journeys");
		db.execSQL("DROP TABLE IF EXISTS journey_legs");
		onCreate(db);
	}

}
