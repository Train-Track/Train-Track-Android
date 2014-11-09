package dyl.anjon.es.traintrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

		ContentValues values;
		// Journeys
		db.execSQL("CREATE TABLE journeys (id INTEGER PRIMARY KEY, user_id INTEGER)");
		// Journey Legs
		db.execSQL("CREATE TABLE journey_legs (id INTEGER PRIMARY KEY, journey_id INTEGER, schedule_id INTEGER, origin_id INTEGER, destination_id INTEGER, departure_time TEXT, arrival_time TEXT, departure_platform TEXT, arrival_platform TEXT)");

		// Users
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
		db.execSQL("DROP TABLE IF EXISTS stations");
		db.execSQL("DROP TABLE IF EXISTS operators");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS journeys");
		db.execSQL("DROP TABLE IF EXISTS journey_legs");
		onCreate(db);
	}

}
