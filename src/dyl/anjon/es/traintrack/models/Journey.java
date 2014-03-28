package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;
import dyl.anjon.es.traintrack.utils.Utils;

public class Journey {

	private int id;
	private int userId;
	private ArrayList<JourneyLeg> journeyLegs;

	public Journey() {
		this.setId(0);
		this.journeyLegs = new ArrayList<JourneyLeg>();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param user_id
	 *            the id of the user to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the origin
	 */
	public Station getOrigin(Context context) {
		if (this.journeyLegs.isEmpty()) {
			this.journeyLegs = this.getJourneyLegs(context);
		}
		if (this.journeyLegs.isEmpty()) {
			return new Station();
		}

		return this.journeyLegs.get(0).getOrigin();
	}

	/**
	 * @return the destination
	 */
	public Station getDestination(Context context) {
		if (this.journeyLegs.isEmpty()) {
			this.journeyLegs = this.getJourneyLegs(context);
		}
		if (this.journeyLegs.isEmpty()) {
			return new Station();
		}

		int last = this.journeyLegs.size();
		return this.journeyLegs.get(last - 1).getDestination();
	}

	/**
	 * @param context
	 * @return the journey legs for this journey
	 */
	public ArrayList<JourneyLeg> getJourneyLegs(Context context) {
		ArrayList<JourneyLeg> journeyLegs = new ArrayList<JourneyLeg>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("journey_legs", new String[] { "id",
				"journey_id", "schedule_id", "origin_station_id",
				"destination_station_id", "departure_time", "arrival_time" },
				"journey_id = ?", new String[] { String.valueOf(this.id) },
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				JourneyLeg journeyLeg = new JourneyLeg();
				journeyLeg.setId(cursor.getInt(0));
				journeyLeg.setJourneyId(cursor.getInt(1));
				journeyLeg.setScheduleId(cursor.getInt(2));
				journeyLeg.setOriginStationId(cursor.getInt(3));
				journeyLeg.setDestinationStationId(cursor.getInt(4));
				journeyLeg.setOrigin(Station.get(context, cursor.getInt(3)));
				journeyLeg
						.setDestination(Station.get(context, cursor.getInt(4)));
				journeyLeg.setDepartureTime(cursor.getString(5));
				journeyLeg.setArrivalTime(cursor.getString(6));
				journeyLegs.add(journeyLeg);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();

		return journeyLegs;
	}

	/**
	 * @param context
	 * @return all journeys
	 */
	public static ArrayList<Journey> getAll(Context context) {
		ArrayList<Journey> journeys = new ArrayList<Journey>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM journeys", null);
		if (cursor.moveToFirst()) {
			do {
				Journey journey = new Journey();
				journey.setId(cursor.getInt(0));
				journey.setUserId(cursor.getInt(1));
				journeys.add(journey);
			} while (cursor.moveToNext());
		}
		cursor.close();
		dbh.close();
		return journeys;
	}

	/**
	 * @param context
	 * @param id
	 * @return the journey selected
	 */
	public static Journey get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor cursor = db.query("journeys", new String[] { "id" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Journey journey = new Journey();
		journey.setId(cursor.getInt(0));
		dbh.close();

		return journey;
	}

	/**
	 * @param context
	 * @return the journey saved along with local id
	 */
	public Journey save(Context context) {
		if (this.id == 0) {
			DatabaseHandler dbh = new DatabaseHandler(context);
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("user_id", Utils.getSession().getUser().getId());
			long id = db.insert("journeys", null, values);
			if (id > 0) {
				this.setId((int) id);
			}
		}
		return this;
	}

	/**
	 * @param context
	 * @return true if delete was successful or false if it was not
	 */
	public boolean delete(Context context) {
		if (this.id != 0) {
			DatabaseHandler dbh = new DatabaseHandler(context);
			SQLiteDatabase db = dbh.getWritableDatabase();
			int rowsDeleted = db.delete("journey_legs", "journey_id = ?",
					new String[] { String.valueOf(this.id) });
			rowsDeleted = db.delete("journeys", "id = ?",
					new String[] { String.valueOf(this.id) });
			if (rowsDeleted == 0) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

}
