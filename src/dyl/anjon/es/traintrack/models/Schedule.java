package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

public class Schedule {

	private ArrayList<ScheduleLocation> scheduleLocations;

	public Schedule() {
		this.scheduleLocations = new ArrayList<ScheduleLocation>();
	}

	/**
	 * @return the schedule locations
	 */
	public ArrayList<ScheduleLocation> getScheduleLocations() {
		return scheduleLocations;
	}

	/**
	 * @param scheduleLocation
	 *            the schedule location to add
	 */
	public void addScheduleLocation(ScheduleLocation scheduleLocation) {
		this.scheduleLocations.add(scheduleLocation);
	}

	/**
	 * @return the origin
	 */
	public Station getOrigin() {
		if (this.getScheduleLocations().isEmpty()) {
			return null;
		}

		return this.getScheduleLocations().get(0).getStation();
	}


	/**
	 * @return the destination
	 */
	public Station getDestination() {
		if (this.getScheduleLocations().isEmpty()) {
			return null;
		}

		int last = this.getScheduleLocations().size();
		return this.getScheduleLocations().get(last - 1).getStation();
	}


	/**
	 * @return the origin and destination
	 */
	@Override
	public String toString() {
		return this.getOrigin().toString() + " to "
				+ this.getDestination().toString();
	}

}
