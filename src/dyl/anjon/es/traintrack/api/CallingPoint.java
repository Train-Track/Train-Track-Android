package dyl.anjon.es.traintrack.api;

import org.w3c.dom.Element;

import dyl.anjon.es.traintrack.models.Station;

public class CallingPoint {

	private Station station;
	private String scheduledTime;
	private String estimatedTime;
	private String actualTime;

	public CallingPoint() {

	}

	public CallingPoint(Element cp) {
		if (cp.getElementsByTagName("crs").getLength() > 0) {
			setStationByCrs(cp.getElementsByTagName("crs").item(0)
					.getTextContent());
		}
		if (cp.getElementsByTagName("st").getLength() > 0) {
			setScheduledTime(cp.getElementsByTagName("st").item(0)
					.getTextContent());
		}
		if (cp.getElementsByTagName("et").getLength() > 0) {
			setEstimatedTime(cp.getElementsByTagName("et").item(0)
					.getTextContent());
		}
		if (cp.getElementsByTagName("at").getLength() > 0) {
			setActualTime(cp.getElementsByTagName("at").item(0)
					.getTextContent());
		}
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String toString() {
		return "Name: " + getStation() + "\n";
	}

	public void setStationByCrs(String crs) {
		setStation(Station.getByCrs(crs));
	}
	
	public boolean hasArrived() {
		if (getActualTime() != null) {
			return true;
		}
		else {
			return false;
		}
	}

}
