package dyl.anjon.es.traintrack.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("JourneyLeg")
public class JourneyLeg extends ParseObject {

	public JourneyLeg() {
	}

	/**
	 * @return the departure station
	 */
	public Station getDepartureStation() {
		return (Station) get("departureStation");
	}

	/**
	 * @param departureStation
	 *            the departure station
	 */
	public void setDepartureStation(Station departureStation) {
		put("departureStation", departureStation);
	}

	/**
	 * @return the departure platform
	 */
	public String getDeparturePlatform() {
		return getString("departurePlatform");
	}

	/**
	 * @param departurePlatform
	 *            the departure platform
	 */
	public void setDeparturePlatform(String departurePlatform) {
		put("departurePlatform", departurePlatform);
	}

	/**
	 * @return the departure time
	 */
	public String getDepartureTime() {
		return getString("departureTime");
	}

	/**
	 * @param departureTime
	 *            the departure time
	 */
	public void setDepartureTime(String departureTime) {
		put("departureTime", departureTime);
	}

	/**
	 * @return the arrival station
	 */
	public Station getArrivalStation() {
		return (Station) get("arrivalStation");
	}

	/**
	 * @param arrivalStation
	 *            the arrival station
	 */
	public void setArrivalStation(Station arrivalStation) {
		put("arrivalStation", arrivalStation);
	}

	/**
	 * @return the arrival platform
	 */
	public String getArrivalPlatform() {
		return getString("arrivalPlatform");
	}

	/**
	 * @param arrivalPlatform
	 *            the departure platform
	 */
	public void setArrivalPlatform(String arrivalPlatform) {
		put("arrivalPlatform", arrivalPlatform);
	}

	/**
	 * @return the arrival time
	 */
	public String getArrivalTime() {
		return getString("arrivalTime");
	}

	/**
	 * @param arrivalTime
	 *            the arrival time
	 */
	public void setArrivalTime(String arrivalTime) {
		put("arrivalTime", arrivalTime);
	}

	/**
	 * @param journey
	 *            the journey this leg is a part of
	 */
	public void setJourney(Journey journey) {
		put("journey", journey);
	}

	/**
	 * @return the name
	 */
	@Override
	public String toString() {
		return this.getDepartureStation().toString() + " to "
				+ this.getArrivalStation().toString();
	}

}
