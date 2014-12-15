package dyl.anjon.es.traintrack.api;

import org.w3c.dom.Element;

public class ServiceItem {

	private static final String ON_TIME = "On Time";
	private String originCrs;
	private String originName;
	private String destinationCrs;
	private String destinationName;
	private String scheduledTimeArrival;
	private String estimatedTimeArrival;
	private String scheduledTimeDeparture;
	private String estimatedTimeDeparture;
	private String platform;
	private String operatorCode;
	private String operatorName;
	private boolean isCircularRoute;
	private String serviceId;

	public ServiceItem(Element ts) {
		if (ts.getElementsByTagName("origin").getLength() > 0) {
			Element orig = (Element) ts.getElementsByTagName("origin").item(0);
			if (orig.getElementsByTagName("crs").getLength() > 0) {
				String crs = orig.getElementsByTagName("crs").item(0)
						.getTextContent();
				this.originCrs = crs;
				String locationName = orig.getElementsByTagName("locationName")
						.item(0).getTextContent();
				this.originName = locationName;
			}
		}
		if (ts.getElementsByTagName("destination").getLength() > 0) {
			Element dest = (Element) ts.getElementsByTagName("destination")
					.item(0);
			if (dest.getElementsByTagName("crs").getLength() > 0) {
				String crs = dest.getElementsByTagName("crs").item(0)
						.getTextContent();
				this.destinationCrs = crs;
				String locationName = dest.getElementsByTagName("locationName")
						.item(0).getTextContent();
				this.destinationName = locationName;
			}
		}
		if (ts.getElementsByTagName("sta").getLength() > 0) {
			this.scheduledTimeArrival = ts.getElementsByTagName("sta").item(0)
					.getTextContent();
		}
		if (ts.getElementsByTagName("eta").getLength() > 0) {
			this.estimatedTimeArrival = ts.getElementsByTagName("eta").item(0)
					.getTextContent();
		}
		if (ts.getElementsByTagName("std").getLength() > 0) {
			this.scheduledTimeDeparture = ts.getElementsByTagName("std")
					.item(0).getTextContent();
		}
		if (ts.getElementsByTagName("etd").getLength() > 0) {
			this.estimatedTimeDeparture = ts.getElementsByTagName("etd")
					.item(0).getTextContent();
		}
		if (ts.getElementsByTagName("platform").getLength() > 0) {
			this.platform = ts.getElementsByTagName("platform").item(0)
					.getTextContent();
		}
		if (ts.getElementsByTagName("operatorCode").getLength() > 0) {
			this.operatorCode = ts.getElementsByTagName("operatorCode").item(0)
					.getTextContent();
		}
		if (ts.getElementsByTagName("operator").getLength() > 0) {
			this.operatorName = ts.getElementsByTagName("operator").item(0)
					.getTextContent();
		}
		if (ts.getElementsByTagName("isCircularRoute").getLength() > 0) {
			this.isCircularRoute = Boolean.valueOf(ts
					.getElementsByTagName("isCircularRoute").item(0)
					.getTextContent());
		}
		if (ts.getElementsByTagName("serviceID").getLength() > 0) {
			this.serviceId = ts.getElementsByTagName("serviceID").item(0)
					.getTextContent();
		}
	}

	public String getOriginCrs() {
		return originCrs;
	}

	public String getOriginName() {
		return originName;
	}

	public String getDestinationCrs() {
		return destinationCrs;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public String getScheduledTimeArrival() {
		return scheduledTimeArrival;
	}

	public String getEstimatedTimeArrival() {
		return estimatedTimeArrival;
	}

	public String getScheduledTimeDeparture() {
		return scheduledTimeDeparture;
	}

	public String getEstimatedTimeDeparture() {
		return estimatedTimeDeparture;
	}

	public String getPlatform() {
		return platform;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public boolean isCircularRoute() {
		return isCircularRoute;
	}

	public String getServiceId() {
		return serviceId;
	}

	public boolean isDelayedDeparting() {
		if (getEstimatedTimeDeparture().equalsIgnoreCase(ON_TIME)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isDelayedArriving() {
		if (getEstimatedTimeArrival().equalsIgnoreCase(ON_TIME)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean terminatesHere() {
		if (getScheduledTimeDeparture() == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean startsHere() {
		if (getScheduledTimeArrival() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String getScheduledTime() {
		if (terminatesHere()) {
			return getScheduledTimeArrival();
		} else {
			return getScheduledTimeDeparture();
		}
	}

	public String getEstimatedTime() {
		if (terminatesHere()) {
			return getEstimatedTimeArrival();
		} else {
			return getEstimatedTimeDeparture();
		}
	}

	public String toString() {
		return getScheduledTimeDeparture() + " " + getOperatorName()
				+ " service from " + getOriginName() + " to "
				+ getDestinationName() + " on platform " + getPlatform();
	}

}
