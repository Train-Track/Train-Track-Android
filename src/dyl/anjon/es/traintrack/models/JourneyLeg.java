package dyl.anjon.es.traintrack.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class JourneyLeg {

	public static final String TABLE_NAME = "journey_legs";

	private int id;
	private int journeyId;
	private int scheduleId;
	private int originId;
	private int destinationId;
	private Location origin;
	private Location destination;
	private String departureTime;
	private String arrivalTime;
	private String departurePlatform;
	private String arrivalPlatform;

	public JourneyLeg() {
		this.setId(0);
	}

	public JourneyLeg(Location origin, Location destination) {
		this.setOrigin(origin);
		this.setDestination(destination);
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
	 * @param id
	 *            the journey id to set
	 */
	public void setJourneyId(int journeyId) {
		this.journeyId = journeyId;
	}

	/**
	 * @return the journey id
	 */
	public int getJourneyId() {
		return journeyId;
	}

	/**
	 * @param id
	 *            the schedule id to set
	 */
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	/**
	 * @return the schedule id
	 */
	public int getScheduleId() {
		return scheduleId;
	}

	/**
	 * @return the id of the origin
	 */
	public int getOriginId() {
		return originId;
	}

	/**
	 * @param id
	 *            the id of the origin to set
	 */
	public void setOriginId(int originId) {
		this.originId = originId;
	}

	/**
	 * @return the id of the destination
	 */
	public int getDestinationId() {
		return destinationId;
	}

	/**
	 * @param id
	 *            the id of the destination to set
	 */
	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}

	/**
	 * @return the origin
	 */
	public Location getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public Location getDestination() {
		return destination;
	}

	/**
	 * @param destinaion
	 *            the destination to set
	 */
	public void setDestination(Location destination) {
		this.destination = destination;
	}

	/**
	 * @return the departureTime
	 */
	public String getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime
	 *            the departureTime to set
	 */
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * @return the departureTime
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * @param departureTime
	 *            the departureTime to set
	 */
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	/**
	 * @return the departurePlatform
	 */
	public String getDeparturePlatform() {
		return departurePlatform;
	}

	/**
	 * @param departurePlatform
	 *            the departurePlatform to set
	 */
	public void setDeparturePlatform(String departurePlatform) {
		this.departurePlatform = departurePlatform;
	}

	/**
	 * @return the arrivalPlatform
	 */
	public String getArrivalPlatform() {
		return arrivalPlatform;
	}

	/**
	 * @param arrivalPlatform
	 *            the arrivalPlatform to set
	 */
	public void setArrivalPlatform(String arrivalPlatform) {
		this.arrivalPlatform = arrivalPlatform;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getOrigin().toString() + " to "
				+ this.getDestination().toString();
	}

	/**
	 * @param context
	 * @param id
	 * @return the journey leg selected
	 */
	public static JourneyLeg get(int id) {
		DatabaseHandler dbh = new DatabaseHandler();
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query("journey_legs", new String[] { "id",
				"journey_id", "schedule_id", "origin_id",
				"destination_id", "departure_time", "arrival_time",
				"departure_platform", "arrival_platform" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		JourneyLeg journeyLeg = new JourneyLeg();
		journeyLeg.setId(cursor.getInt(0));
		journeyLeg.setJourneyId(cursor.getInt(1));
		journeyLeg.setScheduleId(cursor.getInt(2));
		journeyLeg.setOriginId(cursor.getInt(3));
		journeyLeg.setOrigin(Location.get(cursor.getInt(3)));
		journeyLeg.setDestinationId(cursor.getInt(4));
		journeyLeg.setDestination(Location.get(cursor.getInt(4)));
		journeyLeg.setDepartureTime(cursor.getString(5));
		journeyLeg.setArrivalTime(cursor.getString(6));
		journeyLeg.setDeparturePlatform(cursor.getString(7));
		journeyLeg.setArrivalPlatform(cursor.getString(8));
		cursor.close();
		dbh.close();

		return journeyLeg;
	}

	public JourneyLeg save(Context context) {
		if (this.getId() == 0) {
			DatabaseHandler dbh = new DatabaseHandler(context);
			SQLiteDatabase db = dbh.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("journey_id", this.getJourneyId());
			values.put("schedule_id", this.getScheduleId());
			values.put("origin_id", this.getOriginId());
			values.put("destination_id", this.getDestinationId());
			values.put("departure_time", this.getDepartureTime());
			values.put("arrival_time", this.getArrivalTime());
			values.put("departure_platform", this.getDeparturePlatform());
			values.put("arrival_platform", this.getArrivalPlatform());
			long id = db.insert("journey_legs", null, values);
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
			int rowsDeleted = db.delete("journey_legs", "id = ?",
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
