package dyl.anjon.es.traintrack.models;

import java.util.Date;

public class JourneyLeg {

	private TrainStation origin;
	private TrainStation destination;
	private Date departureTime;

	public JourneyLeg(TrainStation origin, TrainStation destination) {
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setDepartureTime(departureTime);
	}

	/**
	 * @return the origin
	 */
	public TrainStation getOrigin() {
		return origin;
	}

	/**
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(TrainStation origin) {
		this.origin = origin;
	}

	/**
	 * @return the destination
	 */
	public TrainStation getDestination() {
		return destination;
	}

	/**
	 * @param destinaion
	 *            the destination to set
	 */
	public void setDestination(TrainStation destination) {
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
