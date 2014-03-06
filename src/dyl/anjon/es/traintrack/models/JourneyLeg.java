package dyl.anjon.es.traintrack.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class JourneyLeg {

	private int id;
	private int journeyId;
	private int scheduleId;
	private int originStationId;
	private int destinationStationId;
	private Station origin;
	private Station destination;
	private String departureTime;
	private String arrivalTime;

	public JourneyLeg() {
	}

	public JourneyLeg(Station origin, Station destination) {
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
	 * @return the id of the origin station
	 */
	public int getOriginStationId() {
		return originStationId;
	}

	/**
	 * @param id
	 *            the id of the origin station to set
	 */
	public void setOriginStationId(int originStationId) {
		this.originStationId = originStationId;
	}

	/**
	 * @return the id of the destination station
	 */
	public int getDestinationStationId() {
		return destinationStationId;
	}

	/**
	 * @param id
	 *            the id of the destination station to set
	 */
	public void setDestinationStationId(int destinationStationId) {
		this.destinationStationId = destinationStationId;
	}

	/**
	 * @return the origin
	 */
	public Station getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(Station origin) {
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public Station getDestination() {
		return destination;
	}

	/**
	 * @param destinaion
	 *            the destination to set
	 */
	public void setDestination(Station destination) {
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
	public static JourneyLeg get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query("journey_legs", new String[] { "id",
				"journey_id", "schedule_id", "origin_station_id",
				"destination_station_id", "departure_time", "arrival_time" },
				"id = ?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		JourneyLeg journeyLeg = new JourneyLeg();
		journeyLeg.setId(cursor.getInt(0));
		journeyLeg.setJourneyId(cursor.getInt(1));
		journeyLeg.setScheduleId(cursor.getInt(2));
		journeyLeg.setOriginStationId(cursor.getInt(3));
		journeyLeg.setOrigin(Station.get(context, cursor.getInt(3)));
		journeyLeg.setDestinationStationId(cursor.getInt(4));
		journeyLeg.setDestination(Station.get(context, cursor.getInt(4)));
		journeyLeg.setDepartureTime(cursor.getString(5));
		journeyLeg.setArrivalTime(cursor.getString(6));
		dbh.close();

		return journeyLeg;
	}

}
