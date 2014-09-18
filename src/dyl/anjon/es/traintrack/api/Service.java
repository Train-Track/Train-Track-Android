package dyl.anjon.es.traintrack.api;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dyl.anjon.es.traintrack.models.Location;
import dyl.anjon.es.traintrack.utils.Utils;

public class Service {

	public static final String TABLE_NAME = "services";
	private String serviceId;
	private String serviceType;
	private String operator;
	private String operatorCode;
	private String platform;
	private Location location;
	private String crs;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
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
		// String xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		String xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetServiceDetailsResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetServiceDetailsResult><generatedAt>2014-09-16T20:30:26.3255855+01:00</generatedAt><serviceType>train</serviceType><locationName>Manchester Piccadilly</locationName><crs>MAN</crs><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><disruptionReason>This train has been delayed by a train fault</disruptionReason><overdueMessage>We were expecting the monitoring point at Manchester Piccadilly to report on this train at 15:49. This report has not been received.</overdueMessage><platform>3</platform><sta>15:02</sta><ata>15:48</ata><std>15:06</std><etd>Delayed</etd><previousCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Cleethorpes</locationName><crs>CLE</crs><st>12:26</st><at>On time</at></callingPoint><callingPoint><locationName>Grimsby Town</locationName><crs>GMB</crs><st>12:34</st><at>On time</at></callingPoint><callingPoint><locationName>Barnetby</locationName><crs>BTB</crs><st>12:53</st><at>On time</at></callingPoint><callingPoint><locationName>Scunthorpe</locationName><crs>SCU</crs><st>13:08</st><at>On time</at></callingPoint><callingPoint><locationName>Doncaster</locationName><crs>DON</crs><st>13:42</st><at>14:15</at></callingPoint><callingPoint><locationName>Meadowhall</locationName><crs>MHS</crs><st>14:01</st><at>14:34</at></callingPoint><callingPoint><locationName>Sheffield</locationName><crs>SHF</crs><st>14:11</st><at>14:52</at></callingPoint><callingPoint><locationName>Stockport</locationName><crs>SPT</crs><st>14:53</st><at>15:36</at></callingPoint></callingPointList></previousCallingPoints><subsequentCallingPoints><callingPointList serviceType=\"train\" serviceChangeRequired=\"false\"><callingPoint><locationName>Manchester Airport</locationName><crs>MIA</crs><st>15:26</st><et>Delayed</et></callingPoint></callingPointList></subsequentCallingPoints></GetServiceDetailsResult></GetServiceDetailsResponse></soap:Body></soap:Envelope>";

		return new Service(serviceId, xmlResponse);
	}

	public String toString() {
		return this.previousCallingPoints.toString();
	}

}
