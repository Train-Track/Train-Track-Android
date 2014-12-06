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
		if (getGeneratedAt() != null) {
			return "Last updated at " + generatedAt.format("%H:%m");
		} else {
			return "Uh oh!";
		}
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
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetArrivalDepartureBoardResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetArrivalDepartureBoardResult><generatedAt>2014-10-18T00:41:49.1177966+01:00</generatedAt><locationName>Andover</locationName><crs>ADV</crs><platformAvailable>true</platformAvailable><trainServices><service><origin><location><locationName>Accrington</locationName><crs>ACR</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></destination><sta>00:27</sta><eta>00:40</eta><platform>9</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>aJbyv/IEwjMUxzud/F3itA==</serviceID></service><service><origin><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Alloa</locationName><crs>ALO</crs><via /><futureChangeTo /></location></destination><std>00:34</std><etd>Delayed</etd><platform>9</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>9iVb1dqkSRrG0wCewphwww==</serviceID></service><service><origin><location><locationName>Alton</locationName><crs>AON</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></destination><sta>00:34</sta><eta>00:42</eta><platform>10</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>oBvDDYUSISYFvl9HhCYYhg==</serviceID></service><service><origin><location><locationName>Alton</locationName><crs>AON</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></destination><sta>01:16</sta><eta>On time</eta><platform>9</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>0/H2j1bnJCZtOlYq6ATNwg==</serviceID></service><service><origin><location><locationName>Arisaig</locationName><crs>ARG</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></destination><sta>01:22</sta><eta>On time</eta><platform>10</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>//8hNcU0IBuOfAfSeEOkfg==</serviceID></service><service><origin><location><locationName>Acle</locationName><crs>ACL</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Andover</locationName><crs>ADV</crs><via /><futureChangeTo /></location></destination><sta>02:07</sta><eta>On time</eta><platform>14</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>AEhCbIOeEqJMBfsk94nb+w==</serviceID></service></trainServices></GetArrivalDepartureBoardResult></GetArrivalDepartureBoardResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}
		return new StationBoard(xmlResponse);
	}

	public String toString() {
		return this.trainServices.toString();
	}
}
