package uk.co.traintrackapp.traintrack.model;

import java.util.ArrayList;
import java.util.Locale;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import uk.co.traintrackapp.traintrack.utils.Utils;

@ParseClassName("Station")
public class Station extends ParseObject {

    private double distance;

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
     * @return the image
     */
    public Image getImage() {
        return (Image) getParseObject("image");
    }

    /**
     * @return the location
     */
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return getLocation().getLatitude();
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return getLocation().getLongitude();
    }

    /**
     * @return true if favourite
     */
    public boolean isFavourite() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param favourite
     */
    public void setFavourite(boolean favourite) {
        // TODO Auto-generated method stub
    }

    /**
     * @param distance (in km)
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return distance (km)
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * @return text representation of distance
     */
    public String getDistanceText() {
        double distance = getDistance();
        String format = "";
        if (distance > 100) {
            format = "%.0f km";
        } else if (distance > 0) {
            format = "%.2f km";
        }
        return String.format(Locale.getDefault(), format, distance);
    }

    /**
     * @param string the string to look for in the name
     * @return true if string is within name
     */
    public boolean isNameSimilarTo(CharSequence string) {
        return getName().toLowerCase(Locale.UK).contains(
                string.toString().toLowerCase(Locale.UK));
    }

    /**
     * @return the name of the station
     */
    public String toString() {
        return getName();
    }

    /**
     * @param crs CRS Code
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
     * @param id station ID
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
     * @return all the stations
     */
    public static ArrayList<Station> getAll() {
        ArrayList<Station> stations = new ArrayList<Station>();
        ParseQuery<Station> query = ParseQuery.getQuery(Station.class);
        query.fromLocalDatastore();
        query.orderByAscending("name");
        try {
            stations.addAll(query.find());
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return stations;
    }

}
