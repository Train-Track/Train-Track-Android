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
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "train_track";
	private Context context;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public DatabaseHandler() {
		super(Utils.getSession().getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		this.context = Utils.getSession().getContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// Locations
		db.execSQL("CREATE TABLE " + Location.TABLE_NAME
				+ " (id INTEGER PRIMARY KEY," + " crs_code TEXT,"
				+ " name TEXT," + " latitude DOUBLE," + " longitude DOUBLE,"
				+ " favourite BOOLEAN)");

		Gson gson = new Gson();
		InputStream is = null;
		try {
			is = this.context.getAssets().open("locations.json");
		} catch (IOException e) {
			Utils.log(e.getMessage());
		}
		Reader reader = new InputStreamReader(is);
		List<Location> locations = gson.fromJson(reader,
				new TypeToken<List<Location>>() {
				}.getType());
		ListIterator<Location> it = locations.listIterator();
		while (it.hasNext()) {
			Location location = it.next();
			ContentValues values = new ContentValues();
			values.put("id", location.getId());
			values.put("crs_code", location.getCrsCode());
			values.put("name", location.getName());
			values.put("latitude", location.getLatitude());
			values.put("longitude", location.getLongitude());
			values.put("favourite", location.isFavourite());
			db.insert(Location.TABLE_NAME, null, values);
		}

		// Operators
		db.execSQL("CREATE TABLE "
				+ Operator.TABLE_NAME
				+ " (id INTEGER PRIMARY KEY, code TEXT, name TEXT, delay_repay_url TEXT)");
		gson = new Gson();
		is = null;
		try {
			is = this.context.getAssets().open("operators.json");
		} catch (IOException e) {
			Utils.log(e.getMessage());
		}
		reader = new InputStreamReader(is);
		List<Operator> opertors = gson.fromJson(reader,
				new TypeToken<List<Operator>>() {
				}.getType());
		ListIterator<Operator> itop = opertors.listIterator();
		while (itop.hasNext()) {
			Operator operator = itop.next();
			ContentValues values = new ContentValues();
			values.put("id", operator.getId());
			values.put("code", operator.getCode());
			values.put("name", operator.getName());
			values.put("delay_repay_url", operator.getDelayRepayUrl());
			db.insert(Operator.TABLE_NAME, null, values);
		}

		ContentValues values;
		// Journeys
		db.execSQL("CREATE TABLE journeys (id INTEGER PRIMARY KEY, user_id INTEGER)");
		// Journey Legs
		db.execSQL("CREATE TABLE journey_legs (id INTEGER PRIMARY KEY, journey_id INTEGER, schedule_id INTEGER, origin_id INTEGER, destination_id INTEGER, departure_time TEXT, arrival_time TEXT, departure_platform TEXT, arrival_platform TEXT)");

		// Friends
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
		db.execSQL("DROP TABLE IF EXISTS operators");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS journeys");
		db.execSQL("DROP TABLE IF EXISTS journey_legs");
		onCreate(db);
	}

}
