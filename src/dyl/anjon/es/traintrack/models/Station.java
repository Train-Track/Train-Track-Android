package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import dyl.anjon.es.traintrack.utils.Utils;

@ParseClassName("Station")
public class Station extends ParseObject {

	public Station() {
	}

	/*
	 * @return the crsCode
	 */
	public String getCrsCode() {
		return getString("crs");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return getString("name");
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		ParseGeoPoint location = getParseGeoPoint("location");
		return location.getLatitude();
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		ParseGeoPoint location = getParseGeoPoint("location");
		return location.getLongitude();
	}

	/**
	 * @return all the stations
	 */
	public static ArrayList<Station> getAll() {
		ArrayList<Station> stations = new ArrayList<Station>();
		ParseQuery<Station> query = ParseQuery.getQuery(Station.class);
		query.fromLocalDatastore();
		try {
			stations.addAll(query.find());
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		return stations;
	}

	/**
	 * @return true if favourite
	 */
	public boolean isFavourite() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFavourite(boolean b) {
		// TODO Auto-generated method stub
	}

	public int getDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public CharSequence getDistanceText() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNameSimilarTo(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDistance(float f) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param crs
	 *            CRS Code
	 * @return the station
	 */
	public static Station getByCrs(String crs) {
		ParseQuery<Station> query = ParseQuery.getQuery(Station.class);
		query.fromLocalDatastore();
		query.whereEqualTo("crs", crs);
		Station station = null;
		try {
			station = query.getFirst();
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		return station;
	}

	/**
	 * @param id
	 *            station ID
	 * @return the station
	 */
	public static Station getById(String id) {
		ParseQuery<Station> query = ParseQuery.getQuery(Station.class);
		query.fromLocalDatastore();
		Station station = null;
		try {
			station = query.get(id);
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		return station;
	}

	/**
	 * @return the name of the station
	 */
	public String toString() {
		return getName();
	}

}
