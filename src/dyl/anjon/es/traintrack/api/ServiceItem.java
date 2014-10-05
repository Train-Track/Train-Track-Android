package dyl.anjon.es.traintrack.api;

import org.w3c.dom.Element;

import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.utils.Utils;

public class ServiceItem {

	private static final String ON_TIME = "On Time";
	private Station origin;
	private Station destination;
	private String scheduledTimeArrival;
	private String estimatedTimeArrival;
	private String scheduledTimeDeparture;
	private String estimatedTimeDeparture;
	private String platform;
	private Operator operator;
	private String operatorCode;
	private boolean isCircularRoute;
	private String serviceId;

	public ServiceItem(Element ts) {
		if (ts.getElementsByTagName("origin").getLength() > 0) {
			Element orig = (Element) ts.getElementsByTagName("origin").item(0);
			if (orig.getElementsByTagName("crs").getLength() > 0) {
				String crs = orig.getElementsByTagName("crs").item(0)
						.getTextContent();
				Station origin = Station.getByCrs(crs);
				if (origin != null) {
					this.origin = origin;
				} else {
					Utils.log("NO ORIGIN FOUND IN DB!");
				}
			}
		}
		if (ts.getElementsByTagName("destination").getLength() > 0) {
			Element dest = (Element) ts.getElementsByTagName("destination")
					.item(0);
			if (dest.getElementsByTagName("crs").getLength() > 0) {
				String crs = dest.getElementsByTagName("crs").item(0)
						.getTextContent();
				Station destination = Station.getByCrs(crs);
				if (destination != null) {
					this.destination = destination;
				} else {
					Utils.log("NO DESTINATION FOUND IN DB!");
				}
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
			Operator operator = Operator.getByCode(this.operatorCode);
			if (operator != null) {
				this.operator = operator;
			} else {
				Utils.log("NO OPERATOR FOUND IN DB!");
			}
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

	public Station getOrigin() {
		return origin;
	}

	public void setOrigin(Station origin) {
		this.origin = origin;
	}

	public Station getDestination() {
		return destination;
	}

	public void setDestination(Station destination) {
		this.destination = destination;
	}

	public String getScheduledTimeArrival() {
		return scheduledTimeArrival;
	}

	public void setScheduledTimeArrival(String scheduledTimeArrival) {
		this.scheduledTimeArrival = scheduledTimeArrival;
	}

	public String getEstimatedTimeArrival() {
		return estimatedTimeArrival;
	}

	public void setEstimatedTimeArrival(String estimatedTimeArrival) {
		this.estimatedTimeArrival = estimatedTimeArrival;
	}

	public String getScheduledTimeDeparture() {
		return scheduledTimeDeparture;
	}

	public void setScheduledTimeDeparture(String scheduledTimeDeparture) {
		this.scheduledTimeDeparture = scheduledTimeDeparture;
	}

	public String getEstimatedTimeDeparture() {
		return estimatedTimeDeparture;
	}

	public void setEstimatedTimeDeparture(String estimatedTimeDeparture) {
		this.estimatedTimeDeparture = estimatedTimeDeparture;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public boolean isCircularRoute() {
		return isCircularRoute;
	}

	public void setCircularRoute(boolean isCircularRoute) {
		this.isCircularRoute = isCircularRoute;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public String toString() {
		return this.getScheduledTimeDeparture() + " " + this.getOperator()
				+ " service from " + this.getOrigin() + " to "
				+ this.getDestination() + " on platform " + this.getPlatform();
	}

}
