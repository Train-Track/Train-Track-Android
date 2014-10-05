package dyl.anjon.es.traintrack.api;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.format.Time;

import dyl.anjon.es.traintrack.models.Location;
import dyl.anjon.es.traintrack.models.Operator;
import dyl.anjon.es.traintrack.utils.Utils;

public class Service {

	public static final String TABLE_NAME = "services";
	private String serviceId;
	private String serviceType;
	private Time generatedAt;
	private Operator operator;
	private String operatorCode;
	private String platform;
	private Location location;
	private String crs;
	private boolean isCancelled;
	private String scheduledTimeArrival;
	private String actualTimeArrival;
	private String estimatedTimeArrival;
	private String scheduledTimeDeparture;
	private String actualTimeDeparture;
	private String estimatedTimeDeparture;
	private String disruptionReason;
	private String overdueMessage;
	private ArrayList<CallingPoint> previousCallingPoints;
	private String previousCallingPointsServiceType;
	private ArrayList<CallingPoint> subsequentCallingPoints;
	private String subsequentCallingPointsServiceType;

	private Service(String serviceId, String xml) {

		this.serviceId = serviceId;
		this.previousCallingPoints = new ArrayList<CallingPoint>();
		this.subsequentCallingPoints = new ArrayList<CallingPoint>();

		Document doc = Utils.parseXml(xml);

		NodeList results = doc.getElementsByTagName("GetServiceDetailsResult");
		if (results.getLength() == 0) {
			return;
		}

		Node result = results.item(0);
		for (int i = 0; i < result.getChildNodes().getLength(); i++) {
			Node node = result.getChildNodes().item(i);
			if (node.getNodeName().equalsIgnoreCase("serviceType")) {
				setServiceType(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("generatedAt")) {
				Time t = new Time();
				t.parse3339(node.getTextContent());
				setGeneratedAt(t);
				setLocation(Location.getByCrs(getCrs()));
			} else if (node.getNodeName().equalsIgnoreCase("crs")) {
				setCrs(node.getTextContent());
				setLocation(Location.getByCrs(getCrs()));
			} else if (node.getNodeName().equalsIgnoreCase("operatorCode")) {
				setOperatorCode(node.getTextContent());
				Operator operator = Operator.getByCode(getOperatorCode());
				if (operator != null) {
					setOperator(operator);
				} else {
					Utils.log("NO OPERATOR FOUND IN DB!");
				}
			} else if (node.getNodeName().equalsIgnoreCase("isCancelled")) {
				setCancelled(Boolean.valueOf(node.getTextContent()));
			} else if (node.getNodeName().equalsIgnoreCase("disruptionReason")) {
				setDisruptionReason(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("overdueMessage")) {
				setOverdueMessage(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("sta")) {
				setScheduledTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("eta")) {
				setEstimatedTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("ata")) {
				setActualTimeArrival(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("std")) {
				setScheduledTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("etd")) {
				setEstimatedTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("atd")) {
				setActualTimeDeparture(node.getTextContent());
			} else if (node.getNodeName().equalsIgnoreCase("platform")) {
				setPlatform(node.getTextContent());
			}
		}

		NodeList callingPointsList = doc
				.getElementsByTagName("callingPointList");
		if (callingPointsList.getLength() > 0) {
			Node previousCallingPointsListNode = callingPointsList.item(0);

			Element previousCallingPointsListElement = (Element) previousCallingPointsListNode;
			this.previousCallingPointsServiceType = previousCallingPointsListElement
					.getAttribute("serviceType");

			NodeList callingPoints = previousCallingPointsListNode
					.getChildNodes();
			for (int i = 0; i < callingPoints.getLength(); i++) {
				Element cp = (Element) callingPoints.item(i);
				CallingPoint callingPoint = new CallingPoint(cp);
				this.previousCallingPoints.add(callingPoint);
			}
		}
		if (callingPointsList.getLength() > 1) {
			Node subsequentCallingPointsListNode = callingPointsList.item(1);
			Element subsequentCallingPointsListElement = (Element) subsequentCallingPointsListNode;
			this.subsequentCallingPointsServiceType = subsequentCallingPointsListElement
					.getAttribute("serviceType");

			NodeList callingPoints = subsequentCallingPointsListElement
					.getChildNodes();
			for (int i = 0; i < callingPoints.getLength(); i++) {
				Element cp = (Element) callingPoints.item(i);
				CallingPoint callingPoint = new CallingPoint(cp);
				this.subsequentCallingPoints.add(callingPoint);
			}
		}
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Time getGeneratedAt() {
		return generatedAt;
	}

	public String getGeneratedAtString() {
		return "Last updated at " + generatedAt.format("%H:%m");
	}

	public void setGeneratedAt(Time generatedAt) {
		this.generatedAt = generatedAt;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public void setCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public String getScheduledTimeArrival() {
		return scheduledTimeArrival;
	}

	public void setScheduledTimeArrival(String scheduledTimeArrival) {
		this.scheduledTimeArrival = scheduledTimeArrival;
	}

	public String getActualTimeArrival() {
		return actualTimeArrival;
	}

	public void setActualTimeArrival(String actualTimeArrival) {
		this.actualTimeArrival = actualTimeArrival;
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

	public String getActualTimeDeparture() {
		return actualTimeDeparture;
	}

	public void setActualTimeDeparture(String actualTimeDeparture) {
		this.actualTimeDeparture = actualTimeDeparture;
	}

	public String getEstimatedTimeDeparture() {
		return estimatedTimeDeparture;
	}

	public void setEstimatedTimeDeparture(String estimatedTimeDeparture) {
		this.estimatedTimeDeparture = estimatedTimeDeparture;
	}

	public String getDisruptionReason() {
		return disruptionReason;
	}

	public void setDisruptionReason(String disruptionReason) {
		this.disruptionReason = disruptionReason;
	}

	public String getOverdueMessage() {
		return overdueMessage;
	}

	public void setOverdueMessage(String overdueMessage) {
		this.overdueMessage = overdueMessage;
	}

	public ArrayList<CallingPoint> getPreviousCallingPoints() {
		return previousCallingPoints;
	}

	public void setPreviousCallingPoints(
			ArrayList<CallingPoint> previousCallingPoints) {
		this.previousCallingPoints = previousCallingPoints;
	}

	public String getPreviousCallingPointsServiceType() {
		return previousCallingPointsServiceType;
	}

	public void setPreviousCallingPointsServiceType(
			String previousCallingPointsServiceType) {
		this.previousCallingPointsServiceType = previousCallingPointsServiceType;
	}

	public ArrayList<CallingPoint> getSubsequentCallingPoints() {
		return subsequentCallingPoints;
	}

	public void setSubsequentCallingPoints(
			ArrayList<CallingPoint> subsequentCallingPoints) {
		this.subsequentCallingPoints = subsequentCallingPoints;
	}

	public String getSubsequentCallingPointsServiceType() {
		return subsequentCallingPointsServiceType;
	}

	public void setSubsequentCallingPointsServiceType(
			String subsequentCallingPointsServiceType) {
		this.subsequentCallingPointsServiceType = subsequentCallingPointsServiceType;
	}

	public static Service getByServiceId(String serviceId) {

		String body = "<soapenv:Body><typ:GetServiceDetailsRequest><typ:serviceID>"
				+ serviceId
				+ "</typ:serviceID></typ:GetServiceDetailsRequest></soapenv:Body>";

		String xml = Utils.SOAP_START + Utils.SOAP_HEADER + body
				+ Utils.SOAP_END;
		String xmlResponse = "";
		if (Utils.DEBUG_MODE) {
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetServiceDetailsResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetServiceDetailsResult><generatedAt>2014-09-20T14:46:21.9029735+01:00</generatedAt><serviceType>train</serviceType><locationName>Cardiff Central</locationName><crs>CDF</crs><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><isCancelled>true</isCancelled><disruptionReason>This train has been cancelled because of an operating incident</disruptionReason><sta>15:37</sta><eta>Cancelled</eta><std>15:40</std><etd>Cancelled</etd><previousCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Manchester Piccadilly</locationName><crs>MAN</crs><st>12:30</st><at>On time</at></callingPoint><callingPoint><locationName>Stockport</locationName><crs>SPT</crs><st>12:39</st><at>On time</at></callingPoint><callingPoint><locationName>Wilmslow</locationName><crs>WML</crs><st>12:46</st><at>12:49</at></callingPoint><callingPoint><locationName>Crewe</locationName><crs>CRE</crs><st>13:08</st><at>13:10</at></callingPoint><callingPoint><locationName>Shrewsbury</locationName><crs>SHR</crs><st>13:37</st><et>13:40</et></callingPoint><callingPoint><locationName>Ludlow</locationName><crs>LUD</crs><st>14:06</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Leominster</locationName><crs>LEO</crs><st>14:16</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Hereford</locationName><crs>HFD</crs><st>14:33</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Abergavenny</locationName><crs>AGV</crs><st>14:56</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Cwmbran</locationName><crs>CWM</crs><st>15:09</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Newport (South Wales)</locationName><crs>NWP</crs><st>15:22</st><et>Cancelled</et></callingPoint></callingPointList></previousCallingPoints><subsequentCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Bridgend</locationName><crs>BGN</crs><st>15:59</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Port Talbot Parkway</locationName><crs>PTA</crs><st>16:15</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Neath</locationName><crs>NTH</crs><st>16:22</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Swansea</locationName><crs>SWA</crs><st>16:35</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Gowerton</locationName><crs>GWN</crs><st>16:51</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Llanelli</locationName><crs>LLE</crs><st>16:57</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Pembrey &amp; Burry Port</locationName><crs>PBY</crs><st>17:04</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Kidwelly</locationName><crs>KWL</crs><st>17:11</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Ferryside</locationName><crs>FYS</crs><st>17:16</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Carmarthen</locationName><crs>CMN</crs><st>17:28</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Whitland</locationName><crs>WTL</crs><st>17:45</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Clunderwen</locationName><crs>CUW</crs><st>17:52</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Clarbeston Road</locationName><crs>CLR</crs><st>18:00</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Haverfordwest</locationName><crs>HVF</crs><st>18:08</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Johnston</locationName><crs>JOH</crs><st>18:16</st><et>Cancelled</et></callingPoint><callingPoint><locationName>Milford Haven</locationName><crs>MFH</crs><st>18:31</st><et>Cancelled</et></callingPoint></callingPointList></subsequentCallingPoints></GetServiceDetailsResult></GetServiceDetailsResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}

		return new Service(serviceId, xmlResponse);
	}

	public String toString() {
		return this.previousCallingPoints.toString();
	}

}
