package uk.co.traintrackapp.traintrack.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Tiploc implements Serializable {

    protected static final String DEFAULT_NAME = "Unknown";

    private int id;
    private String uuid;
    private String name;
    private Double lat;
    private Double lng;

    public Tiploc() {
        this.id = 0;
        this.uuid = "";
        this.name = DEFAULT_NAME;
        this.lat = null;
        this.lng = null;
    }

    public Tiploc(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            this.name = json.getString("name");
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

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return lat;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return lng;
    }

    /**
     * @return the name of the station
     */
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tiploc tiploc = (Tiploc) o;
        return uuid.equals(tiploc.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
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
            json.put("lat", getLatitude());
            json.put("lng", getLongitude());
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

}
