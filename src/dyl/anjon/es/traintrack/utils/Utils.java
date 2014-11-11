package dyl.anjon.es.traintrack.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class Utils {

	public static boolean DEBUG_MODE = true;
	public static String API_URL = "http://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb5.asmx";
	public static String ACCESS_TOKEN = "";
	public static String SOAP_START = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://thalesgroup.com/RTTI/2010-11-01/ldb/commontypes\" xmlns:typ=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\">";
	public static String SOAP_HEADER = "<soapenv:Header><com:AccessToken><com:TokenValue>"
			+ ACCESS_TOKEN
			+ "</com:TokenValue></com:AccessToken></soapenv:Header>";
	public static String SOAP_END = "</soapenv:Envelope>";

	/**
	 * @param message
	 *            the log message to write
	 */
	public static void log(String message) {
		Log.i("TrainTrack", message);
	}

	/**
	 * @param hour
	 *            the hour
	 * @param minute
	 *            the minute
	 */
	public static String zeroPadTime(int hour, int minute) {
		String time = "";
		if (hour < 10) {
			time = "0";
		}
		time += hour + ":";
		if (minute < 10) {
			time += "0";
		}
		time += minute;
		return time;
	}

	/**
	 * @param url
	 *            the URL to send to
	 * @param postData
	 *            the raw string to send as the payload
	 */
	public static String httpPost(String url, String postData) {
		StringBuilder builder = new StringBuilder();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(postData));
			httpPost.setHeader("Content-Type", "text/xml");
			HttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (ClientProtocolException e) {
			Utils.log(e.getMessage());
		} catch (IOException e) {
			Utils.log(e.getMessage());
		}
		return builder.toString();
	}

	/**
	 * @param xml
	 *            the xml to parse
	 * @return doc the parsed XML document. If unable to parse, returns null
	 */
	public static Document parseXml(String xml) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			doc = builder.parse(is);
		} catch (SAXException e) {
			Utils.log(e.getMessage());
		} catch (IOException e) {
			Utils.log(e.getMessage());
		} catch (ParserConfigurationException e) {
			Utils.log(e.getMessage());
		}
		return doc;
	}

	/**
	 * @param hourOfDay
	 *            0-23 hours
	 * @param minute
	 *            0 - 59 minutes
	 * 
	 * @return date object
	 */
	public static Date getDateWithTime(int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		return cal.getTime();
	}

	/**
	 * @param time
	 *            in the format hh:mm
	 * 
	 * @return date object
	 */
	public static Date getDateWithTime(String time) {
		int hourOfDay = Integer.valueOf(time.split(":")[0]);
		int minute = Integer.valueOf(time.split(":")[1]);
		return getDateWithTime(hourOfDay, minute);
	}
}
