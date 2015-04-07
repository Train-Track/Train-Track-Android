package uk.co.traintrackapp.traintrack.api;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.model.Operator;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class ServiceItem {

    private static final String ON_TIME = "On time";
    private static final String DELAYED = "Delayed";
    private String serviceId;
    private Station origin;
    private Station destination;
    private String scheduledTimeArrival;
    private String estimatedTimeArrival;
    private String scheduledTimeDeparture;
    private String estimatedTimeDeparture;
    private String platform;
    private Operator operator;

    public ServiceItem() {
        serviceId = "";
        origin = new Station();
        destination = new Station();
        scheduledTimeArrival = "";
        estimatedTimeArrival = "";
        scheduledTimeDeparture = "";
        estimatedTimeDeparture = "";
        platform = "";
        operator = new Operator();
    }

    public ServiceItem(JSONObject json) {
        this();
        try {
            serviceId = json.getString("service_id");
            if (!json.isNull("origin")) {
                origin = new Station(json.getJSONObject("origin"));
            }
            if (!json.isNull("destination")) {
                destination = new Station(json.getJSONObject("destination"));
            }
            scheduledTimeArrival = json.getString("sta");
            estimatedTimeArrival = json.getString("eta");
            scheduledTimeDeparture = json.getString("std");
            estimatedTimeDeparture = json.getString("etd");
            platform = json.getString("platform");
            if (!json.isNull("operator")) {
                operator = new Operator(json.getJSONObject("operator"));
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public String getServiceId() {
        return serviceId;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }

    public String getScheduledTimeArrival() {
        return scheduledTimeArrival;
    }

    public String getEstimatedTimeArrival() {
        return estimatedTimeArrival;
    }

    public String getScheduledTimeDeparture() {
        return scheduledTimeDeparture;
    }

    public String getEstimatedTimeDeparture() {
        return estimatedTimeDeparture;
    }

    public String getPlatform() {
        return platform;
    }

    public Operator getOperator() {
        return operator;
    }

    public boolean isDelayedDeparting() {
        return !getEstimatedTimeDeparture().equalsIgnoreCase(ON_TIME);
    }

    public boolean isDelayedArriving() {
        return !getEstimatedTimeArrival().equalsIgnoreCase(ON_TIME);
    }

    public boolean terminatesHere() {
        return getScheduledTimeDeparture() == null;
    }

    public boolean startsHere() {
        return getScheduledTimeArrival() == null;
    }

    public String getScheduledTime() {
        if (terminatesHere()) {
            return getScheduledTimeArrival();
        } else {
            return getScheduledTimeDeparture();
        }
    }

    public String getEstimatedTime() {
        if (terminatesHere()) {
            return getEstimatedTimeArrival();
        } else {
            return getEstimatedTimeDeparture();
        }
    }

    public boolean isOnTime() {
        return getEstimatedTime().equalsIgnoreCase(ON_TIME);
    }

    public String getTime() {
        if (isOnTime()) {
            return getScheduledTime();
        } else if (getEstimatedTime().equalsIgnoreCase(DELAYED)) {
            return getScheduledTime();
        } else {
            return getEstimatedTime();
        }
    }

    public String toString() {
        return getScheduledTimeDeparture() + " " + getOperator()
                + " service from " + getOrigin() + " to "
                + getDestination() + " on platform " + getPlatform();
    }

}
