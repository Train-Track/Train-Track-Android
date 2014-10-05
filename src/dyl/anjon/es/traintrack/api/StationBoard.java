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
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetArrivalDepartureBoardResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetArrivalDepartureBoardResult><generatedAt>2014-10-05T22:52:55.7389064+01:00</generatedAt><locationName>Leeds</locationName><crs>LDS</crs><nrccMessages><message>Trainsthrough Castleford are being delayed by up to&amp;nbsp;30 minutes. More details can be found in &lt;A href=\"http://nationalrail.co.uk/service_disruptions/83744.aspx\"&gt;Latest Travel News.&lt;/A&gt;</message></nrccMessages><platformAvailable>true</platformAvailable><trainServices><service><origin><location><locationName>Manchester Airport</locationName><crs>MIA</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>York</locationName><crs>YRK</crs><via /><futureChangeTo /></location></destination><sta>22:33</sta><eta>On time</eta><std>22:38</std><etd>22:52</etd><platform>12D</platform><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><serviceID>KJatBrLiQ542c4DZXWeKJg==</serviceID></service><service><origin><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Huddersfield</locationName><crs>HUD</crs><via /><futureChangeTo /></location></destination><std>22:56</std><etd>On time</etd><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>974jKV3QnASytlO/1Fcd4A==</serviceID></service><service><origin><location><locationName>London Kings Cross</locationName><crs>KGX</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></destination><sta>22:57</sta><eta>On time</eta><platform>1</platform><operator>East Coast</operator><operatorCode>GR</operatorCode><serviceID>UaO8t70yHgCdNjNm4a3V5A==</serviceID></service><service><origin><location><locationName>Plymouth</locationName><crs>PLY</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></destination><sta>23:02</sta><eta>On time</eta><platform>9</platform><operator>CrossCountry</operator><operatorCode>XC</operatorCode><serviceID>9TQNE186m4XNTlm+dWztvg==</serviceID></service><service><origin><location><locationName>Knottingley</locationName><crs>KNO</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></destination><sta>23:04</sta><eta>On time</eta><platform>13A</platform><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>flK5s+dxewRxVUQxepMXZQ==</serviceID></service><service><origin><location><locationName>Scarborough</locationName><crs>SCA</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Manchester Piccadilly</locationName><crs>MAN</crs><via /><futureChangeTo /></location></destination><sta>23:05</sta><eta>23:08</eta><std>23:08</std><etd>23:09</etd><platform>16A</platform><operator>First TransPennine Express</operator><operatorCode>TP</operatorCode><serviceID>G66o6V7nqgF/KJNHX+iF6g==</serviceID></service><service><origin><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Bradford Interchange</locationName><crs>BDI</crs><via /><futureChangeTo /></location></destination><std>23:22</std><etd>On time</etd><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>UEu/W2EvU/cQshZacLoHQg==</serviceID></service></trainServices><busServices><service><origin><location><locationName>Bradford Forster Square</locationName><crs>BDQ</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></destination><sta>22:53</sta><eta>On time</eta><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>bSs1Z8VSnqZaHwwYmrg8Cw==</serviceID></service><service><origin><location><locationName>Guiseley</locationName><crs>GSY</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></destination><sta>23:15</sta><eta>On time</eta><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>wh6LFE5qpH8BBADB730YiA==</serviceID></service><service><origin><location><locationName>Leeds</locationName><crs>LDS</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Skipton</locationName><crs>SKI</crs><via /><futureChangeTo /></location></destination><std>23:20</std><etd>On time</etd><operator>Northern</operator><operatorCode>NT</operatorCode><serviceID>BLSiGetewHINfqGTHKoh0A==</serviceID></service></busServices></GetArrivalDepartureBoardResult></GetArrivalDepartureBoardResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}
		return new StationBoard(xmlResponse);
	}

	public String toString() {
		return this.trainServices.toString();
	}
}
