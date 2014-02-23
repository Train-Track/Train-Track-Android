package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

public class Station {

	private int id;
	private String crsCode;
	private String name;
	private long latitude;
	private long longitude;

	/**
	 * @param crsCode
	 * @param name
	 */
	public Station(String crsCode, String name) {
		this.setCrsCode(crsCode);
		this.setName(name);
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
	public long getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public long getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getName();
	}

	public ArrayList<Schedule> getSchedules() {
		ArrayList<Schedule> schedules = new ArrayList<Schedule>();
		// TODO: Get real schedules

		Station cardiff = new Station("CDF", "Cardiff Central");
		ScheduleLocation origin = new ScheduleLocation();
		origin.setStation(cardiff);
		origin.setPlatform("2");

		Station bristol = new Station("BRI", "Bristol Temple Meads");
		ScheduleLocation destination = new ScheduleLocation();
		destination.setStation(bristol);
		destination.setPlatform("13");

		Schedule schedule = new Schedule();
		schedule.addScheduleLocation(origin);
		schedule.addScheduleLocation(destination);
		schedules.add(schedule);

		return schedules;
	}

}
