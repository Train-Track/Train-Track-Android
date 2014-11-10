package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseException;
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

	public double getLatitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static ArrayList<Station> getAll() {
		// TODO Auto-generated method stub
		return new ArrayList<Station>();
	}

	public static Station get(int stationId) {
		// TODO Auto-generated method stub
		return null;
	}

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

	public String toString() {
		return getName();
	}

}
