package uk.co.traintrackapp.traintrack.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Station {

    public static final String FILENAME = "stations.json";
    private int id;
    private String name;
    private String crs;
    private double lat;
    private double lng;
    private double distance;
    private boolean isFavourite;

    public Station() {
        id = 0;
        name = "Unknown";
        crs = "";
        lat = 0;
        lng = 0;
        distance = 0;
        isFavourite = false;
    }

    public Station(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
            this.crs = json.getString("crs");
            this.lat = json.getDouble("lat");
            this.lng = json.getDouble("lng");
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /*
     * @return the crsCode
     */
    public String getCrsCode() {
        return crs;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return lng;
    }

    /**
     * @return true if favourite
     */
    //TODO remove the bpw test case
    public boolean isFavourite() {
        return isFavourite || crs.equals("BPW");
    }

    /**
     * @param favourite true or false
     */
    public void setFavourite(boolean favourite) {
        this.isFavourite = favourite;
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
        return distance;
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
     *
     * @return JSON Object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("name", getName());
            json.put("crs", getCrsCode());
            json.put("lat", getLatitude());
            json.put("lng", getLongitude());
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }
}
