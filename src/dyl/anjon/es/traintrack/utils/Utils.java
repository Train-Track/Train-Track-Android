package dyl.anjon.es.traintrack.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

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

import android.content.Context;
import android.util.Log;
import dyl.anjon.es.traintrack.models.User;

public class Utils {

	private static Utils instance;
	private User user;
	private Context context;

	public static boolean DEBUG_MODE = true;
	public static String API_URL = "http://lite.realtime.nationalrail.co.uk/LDBWS/ldb5.asmx";
	public static String ACCESS_TOKEN = "";
	public static String SOAP_START = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"http://thalesgroup.com/RTTI/2010-11-01/ldb/commontypes\" xmlns:typ=\"http://thalesgroup.com/RTTI/2012-01-13/ldb/types\">";
	public static String SOAP_HEADER = "<soapenv:Header><com:AccessToken><com:TokenValue>"
			+ ACCESS_TOKEN
			+ "</com:TokenValue></com:AccessToken></soapenv:Header>";
	public static String SOAP_END = "</soapenv:Envelope>";

	private Utils() {
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public boolean isLoggedIn() {
		return this.getUser() != null;
	}

	/**
	 * @return the user
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return the session currently stored
	 */
	public static synchronized Utils getSession() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	/**
	 * @param message
	 *            the log message to write
	 */
	public static void log(String message) {
		Log.i("TrainTrack", message);
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
}
