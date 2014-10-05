package dyl.anjon.es.traintrack.api;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.format.Time;
import dyl.anjon.es.traintrack.utils.Utils;

public class StationBoard {

	private Boolean platformAvailable;
	private Boolean areServicesAvailable;
	private Time generatedAt;
	private ArrayList<String> nrccMessages;
	private ArrayList<ServiceItem> trainServices;

	public StationBoard(String xml) {
		this.trainServices = new ArrayList<ServiceItem>();
		this.nrccMessages = new ArrayList<String>();

		Document doc = null;

		if (xml != null) {
			doc = Utils.parseXml(xml);
		}

		if (doc != null) {

			Node generatedAt = doc.getElementsByTagName("generatedAt").item(0);
			if (generatedAt != null) {
				Time t = new Time();
				t.parse3339(generatedAt.getTextContent());
				this.generatedAt = new Time(t);
			}

			Node nrccMessages = doc.getElementsByTagName("nrccMessages")
					.item(0);
			if (nrccMessages != null) {
				NodeList nrccMessageList = nrccMessages.getChildNodes();
				for (int i = 0; i < nrccMessageList.getLength(); i++) {
					String message = nrccMessageList.item(i).getTextContent();
					message = android.text.Html
							.fromHtml(message.replace("<P>&nbsp;</P>", ""))
							.toString().trim();
					this.nrccMessages.add(message);
				}
			}

			Node platformAvailable = doc.getElementsByTagName(
					"platformAvailable").item(0);
			if (platformAvailable != null) {
				this.platformAvailable = Boolean.valueOf(platformAvailable
						.getTextContent());
			}

			Node trainServices = doc.getElementsByTagName("trainServices")
					.item(0);
			if (trainServices != null) {
				NodeList trainServicesList = trainServices.getChildNodes();
				for (int i = 0; i < trainServicesList.getLength(); i++) {
					Element ts = (Element) trainServicesList.item(i);
					ServiceItem serviceItem = new ServiceItem(ts);
					this.trainServices.add(serviceItem);
				}
			}
		}
	}

	public ArrayList<ServiceItem> getTrainServices() {
		return this.trainServices;
	}

	public Boolean getPlatformAvailable() {
		return platformAvailable;
	}

	public Boolean getAreServicesAvailable() {
		return areServicesAvailable;
	}

	public ArrayList<String> getNrccMessages() {
		return nrccMessages;
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

	public static StationBoard getByCrs(String crs) {

		String body = "<soapenv:Body><typ:GetArrivalDepartureBoardRequest><typ:crs>"
				+ crs
				+ "</typ:crs></typ:GetArrivalDepartureBoardRequest></soapenv:Body>";

		String xml = Utils.SOAP_START + Utils.SOAP_HEADER + body
				+ Utils.SOAP_END;
		String xmlResponse = "";
		if (Utils.DEBUG_MODE) {
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetDepartureBoardResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetDepartureBoardResult><generatedAt>2014-09-30T17:11:58.3076253+01:00</generatedAt><locationName>York</locationName><crs>YRK</crs><nrccMessages><message>&lt;P&gt;Trains between Darlington / Sunderland / Middlesbrough and York may be delayed by up to 30 minutes. More details can be found in &lt;A href=\"http://nationalrail.co.uk/service_disruptions/83509.aspx\"&gt;latest travel news&lt;/A&gt;.&lt;/P&gt;&lt;P&gt;&amp;nbsp;&lt;/P&gt;</message></nrccMessages><platformAvailable>true</platformAvailable><trainServices><service><origin><location><locationName>Newcastle</locationName><crs>NCL</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Liverpool Lime Street</locationName><crs>LIV</crs><via /><futureChangeTo /></location></destination><std>17:15</std><etd>On time</etd><platform>5</platform><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><serviceID>qfLq0RHbjE3I9Ew+dMpqsQ==</serviceID></service><service><origin><location><locationName>Manchester Airport</locationName><crs>MIA</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Middlesbrough</locationName><crs>MBR</crs><via /><futureChangeTo /></location></destination><std>17:15</std><etd>On time</etd><platform>11</platform><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><serviceID>uLDO+BWu4WgeteNEV5WsEw==</serviceID></service><service><origin><location><locationName>York</locationName><crs>YRK</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Blackpool North</locationName><crs>BPN</crs><via /><futureChangeTo /></location></destination><std>17:18</std><etd>On time</etd><platform>7</platform><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>KplFjjS4qtsI5BwDyv1s0w==</serviceID></service><service><origin><location><locationName>York</locationName><crs>YRK</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Manchester Airport</locationName><crs>MIA</crs><via /><futureChangeTo /></location></destination><std>17:22</std><etd>On time</etd><platform>4</platform><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><serviceID>kVbuJnOmgmngri8duICn7g==</serviceID></service><service><origin><location><locationName>York</locationName><crs>YRK</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Hull</locationName><crs>HUL</crs><via /><futureChangeTo /></location></destination><std>17:25</std><etd>On time</etd><platform>1</platform><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>6X7iD2VGEYGoalPrbZzazQ==</serviceID></service><service><origin><location><locationName>York</locationName><crs>YRK</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via>via Harrogate</via><futureChangeTo /></location></destination><std>17:29</std><etd>On time</etd><platform>8</platform><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>vNuTnl7nTqBeL6q/hL15Vw==</serviceID></service><service><origin><location><locationName>Newcastle</locationName><crs>NCL</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>London Kings Cross</locationName><crs>KGX</crs><via /><futureChangeTo /></location></destination><std>17:31</std><etd>On time</etd><platform>5A</platform><operator>East Coast</operator><operatorCode>GR</operatorCode><serviceID>uWTNMgd6KVkYjX4FwkpUeg==</serviceID></service><service><origin><location><locationName>Plymouth</locationName><crs>PLY</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Dundee</locationName><crs>DEE</crs><via /><futureChangeTo /></location></destination><std>17:32</std><etd>On time</etd><platform>10</platform><operator>CrossCountry</operator><operatorCode>XC</operatorCode><serviceID>Mq2SIqi9z0GbmKkXItgXAQ==</serviceID></service><service><origin><location><locationName>Newcastle</locationName><crs>NCL</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Reading</locationName><crs>RDG</crs><via>via Doncaster</via><futureChangeTo /></location></destination><std>17:35</std><etd>On time</etd><platform>3</platform><operator>CrossCountry</operator><operatorCode>XC</operatorCode><serviceID>/vneTu+r+N4PIC3H1/BGUw==</serviceID></service><service><origin><location><locationName>London Kings Cross</locationName><crs>KGX</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Glasgow Central</locationName><crs>GLC</crs><via /><futureChangeTo /></location></destination><std>17:36</std><etd>On time</etd><platform>9</platform><operator>East Coast</operator><operatorCode>GR</operatorCode><serviceID>+NcpgrEn+Q3ZtigM8UujYQ==</serviceID></service></trainServices></GetDepartureBoardResult></GetDepartureBoardResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}
		return new StationBoard(xmlResponse);
	}

	public String toString() {
		return this.trainServices.toString();
	}
}
