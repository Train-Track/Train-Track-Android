package uk.co.traintrackapp.traintrack.model;

import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import uk.co.traintrackapp.traintrack.fragment.StationDetailsFragment;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class Station {

    public static final String FILENAME = "stations.json";
    private int id;
    private String uuid;
    private String name;
    private String crs;
    private double lat;
    private double lng;
    private boolean isUnderground;
    private boolean isNationalRail;
    private double distance;

    public Station() {
        id = 0;
        uuid = "";
        name = "Unknown";
        crs = "";
        lat = 0;
        lng = 0;
        isUnderground = false;
        isNationalRail = false;
        distance = 0;
    }

    public Station(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            this.name = json.getString("name");
            this.crs = json.getString("crs");
            this.lat = json.getDouble("lat");
            this.lng = json.getDouble("lng");
            this.isUnderground = json.getBoolean("underground");
            this.isNationalRail = json.getBoolean("national_rail");
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
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
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
     * @return true if underground station
     */
    public boolean isUnderground() {
        return isUnderground;
    }

    /**
     * @return the true if national rail station
     */
    public boolean isNationalRail() {
        return isNationalRail;
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
        String query = string.toString().toLowerCase(Locale.UK);
        return getName().toLowerCase(Locale.UK).contains(query) ||
                getCrsCode().toLowerCase(Locale.UK).contains(query);
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
            json.put("uuid", getUuid());
            json.put("name", getName());
            json.put("crs", getCrsCode());
            json.put("lat", getLatitude());
            json.put("lng", getLongitude());
            json.put("underground", isUnderground());
            json.put("national_rail", isNationalRail());
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

}
