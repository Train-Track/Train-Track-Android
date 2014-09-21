package dyl.anjon.es.traintrack.api;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dyl.anjon.es.traintrack.utils.Utils;

public class StationBoard {

	private Boolean platformAvailable;
	private Boolean areServicesAvailable;
	private String nrccMessages;
	private ArrayList<ServiceItem> trainServices;

	public StationBoard(String xml) {
		this.trainServices = new ArrayList<ServiceItem>();

		Document doc = null;

		if (xml != null) {
			doc = Utils.parseXml(xml);
		}

		if (doc != null) {
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

	public String getNrccMessages() {
		return nrccMessages;
	}

	public static StationBoard getByCrs(String crs) {

		String body = "<soapenv:Body><typ:GetArrivalDepartureBoardRequest><typ:crs>"
				+ crs
				+ "</typ:crs></typ:GetArrivalDepartureBoardRequest></soapenv:Body>";

		String xml = Utils.SOAP_START + Utils.SOAP_HEADER + body
				+ Utils.SOAP_END;
		String xmlResponse = "";
		if (Utils.DEBUG_MODE) {
			xmlResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetArrivalDepartureBoardResponse xmlns=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\"><GetArrivalDepartureBoardResult><generatedAt>2014-09-17T23:20:53.7023978+01:00</generatedAt><locationName>Cardiff Central</locationName><crs>CDF</crs><platformAvailable>true</platformAvailable><trainServices><service><origin><location><locationName>Maesteg</locationName><crs>MST</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Gloucester</locationName><crs>GCR</crs><via /><futureChangeTo /></location></destination><sta>23:09</sta><eta>On time</eta><std>23:20</std><etd>On time</etd><platform>1</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>c+mpfuGwoHBk4BjBeWGSMg==</serviceID></service><service><origin><location><locationName>Barry Island</locationName><crs>BYI</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Pontypridd</locationName><crs>PPD</crs><via /><futureChangeTo /></location></destination><sta>23:13</sta><eta>23:15</eta><std>23:26</std><etd>On time</etd><platform>6</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>5gVWUZJH2dSpm4Jg3gzSsg==</serviceID></service><service><origin><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Bristol Temple Meads</locationName><crs>BRI</crs><via /><futureChangeTo /></location></destination><std>23:27</std><etd>On time</etd><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>pgH2K2eqNDOPqc4wrLGyEQ==</serviceID></service><service><origin><location><locationName>Radyr</locationName><crs>RDR</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Barry Island</locationName><crs>BYI</crs><via /><futureChangeTo /></location></destination><sta>23:28</sta><eta>On time</eta><std>23:31</std><etd>On time</etd><platform>7</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>6NlIklAxmroKlcGKqSRMMw==</serviceID></service><service><origin><location><locationName>Ebbw Vale Parkway</locationName><crs>EBV</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:37</sta><eta>On time</eta><platform>4A</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>MymbuWS3sx3FHXkWGTqS1g==</serviceID></service><service><origin><location><locationName>Swansea</locationName><crs>SWA</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:38</sta><eta>On time</eta><platform>6</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>u3j9XCXFoPUzW8gPXaE4XQ==</serviceID></service><service><origin><location><locationName>Penarth</locationName><crs>PEN</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:40</sta><eta>On time</eta><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>ilPh7to/DpcsJ+Lz6bYmPQ==</serviceID></service><service><origin><location><locationName>London Paddington</locationName><crs>PAD</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Swansea</locationName><crs>SWA</crs><via /><futureChangeTo /></location></destination><sta>23:40</sta><eta>On time</eta><std>23:43</std><etd>On time</etd><platform>3</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>IhCZogtLfHsY+DETeR5rdA==</serviceID></service><service><origin><location><locationName>Bridgend</locationName><crs>BGN</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:43</sta><eta>On time</eta><platform>4A</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>nMy48i0oxIR5kuT4Q3BtyA==</serviceID></service><service><origin><location><locationName>Merthyr Tydfil</locationName><crs>MER</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:43</sta><eta>On time</eta><platform>7</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>NFKoTsMfBBQZgImfl2VgJw==</serviceID></service><service><origin><location><locationName>Portsmouth Harbour</locationName><crs>PMH</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>23:56</sta><eta>On time</eta><platform>3A</platform><operator>First Great Western</operator><operatorCode>GW</operatorCode><serviceID>DwxZ+4kY7U67oNpqt2x5AQ==</serviceID></service><service><origin><location><locationName>Cardiff Bay</locationName><crs>CDB</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>00:04</sta><eta>On time</eta><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>yrPYmnIUggzaGT54D2gv5w==</serviceID></service><service><origin><location><locationName>Manchester Piccadilly</locationName><crs>MAN</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>00:22</sta><eta>On time</eta><platform>3A</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>lNMGtbhgRfZ/mbMnyQ0GkQ==</serviceID></service><service><origin><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Hereford</locationName><crs>HFD</crs><via /><futureChangeTo /></location></destination><std>00:30</std><etd>On time</etd><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>GbaW/9OWws6mQpG2E27ilQ==</serviceID></service><service><origin><location><locationName>Cheltenham Spa</locationName><crs>CNM</crs><via /><futureChangeTo /></location></origin><destination><location><locationName>Cardiff Central</locationName><crs>CDF</crs><via /><futureChangeTo /></location></destination><sta>00:35</sta><eta>On time</eta><platform>3A</platform><operator>Arriva Trains Wales</operator><operatorCode>AW</operatorCode><serviceID>2s84wagFj0q+7Y7PSxfYrw==</serviceID></service></trainServices></GetArrivalDepartureBoardResult></GetArrivalDepartureBoardResponse></soap:Body></soap:Envelope>";
		} else {
			xmlResponse = Utils.httpPost(Utils.API_URL, xml);
		}
		return new StationBoard(xmlResponse);
	}

	public String toString() {
		return this.trainServices.toString();
	}
}
