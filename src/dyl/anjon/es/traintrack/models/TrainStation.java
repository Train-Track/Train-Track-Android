package dyl.anjon.es.traintrack.models;

public class TrainStation {

	private String crsCode;
	private String name;
	private long latitude;
	private long longitude;

	public TrainStation(String crsCode, String name) {
		this.setCrsCode(crsCode);
		this.setName(name);
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

}
