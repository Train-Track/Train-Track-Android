package dyl.anjon.es.traintrack.models;

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

}
