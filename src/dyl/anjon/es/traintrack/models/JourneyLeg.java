package dyl.anjon.es.traintrack.models;

import java.util.Date;

public class JourneyLeg {

	private Station origin;
	private Station destination;
	private Date departureTime;

	public JourneyLeg(Station origin, Station destination) {
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setDepartureTime(departureTime);
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
	public Date getDepartureTime() {
		return departureTime;
	}

	/**
	 * @param departureTime
	 *            the departureTime to set
	 */
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getOrigin().toString() + " to "
				+ this.getDestination().toString();
	}

}
