package dyl.anjon.es.traintrack.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import dyl.anjon.es.traintrack.models.Station;

public class StationDatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "train_track";
	private static final String TABLE_NAME = "stations";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CRS_CODE = "crsCode";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_LNG = "longitude";

	public StationDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_CRS_CODE + " TEXT," + KEY_NAME
				+ " TEXT," + KEY_LAT + " NUMERIC," + KEY_LNG + " NUMERIC" + ")");

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, "Cardiff Central");
		values.put(KEY_CRS_CODE, "CDF");
		db.insert(TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * @param station
	 *            a station to store in database
	 */
	public void addStation(Station station) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, station.getName());
		values.put(KEY_CRS_CODE, station.getCrsCode());
		values.put(KEY_LAT, station.getLatitude());
		values.put(KEY_LNG, station.getLongitude());

		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	/**
	 * @param id
	 * @return the station selected
	 */
	public Station getStation(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
				KEY_CRS_CODE, KEY_NAME, KEY_LAT, KEY_LNG }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Station station = new Station(cursor.getString(1), cursor.getString(2));
		station.setId(cursor.getInt(0));
		return station;
	}

	/**
	 * @return all stations
	 */
	public ArrayList<Station> getAllStations() {
		ArrayList<Station> stations = new ArrayList<Station>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

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

}
