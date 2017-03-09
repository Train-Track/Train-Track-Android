package uk.co.traintrackapp.traintrack.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.model.Operator;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class Service {

    private String serviceId;
    private String serviceType;
    private boolean isCancelled;
    private String disruptionReason;
    private String overdueMessage;
    private Station station;
    private Station origin;
    private Station destination;
    private Operator operator;
    private String platform;
    private String scheduledTimeArrival;
    private String estimatedTimeArrival;
    private String actualTimeArrival;
    private String scheduledTimeDeparture;
    private String estimatedTimeDeparture;
    private String actualTimeDeparture;
    private ArrayList<CallingPoint> callingPoints;

    private Service() {
        this.serviceId = "";
        this.serviceType = "";
        this.isCancelled = false;
        this.disruptionReason = "";
        this.overdueMessage = "";
        this.station = new Station();
        this.origin = new Station();
        this.destination = new Station();
        this.operator = new Operator();
        this.platform = "";
        this.scheduledTimeArrival = "";
        this.estimatedTimeArrival = "";
        this.actualTimeArrival = "";
        this.scheduledTimeDeparture = "";
        this.estimatedTimeDeparture = "";
        this.actualTimeDeparture = "";
        this.callingPoints = new ArrayList<>();
    }

    protected Service(JSONObject json) {
        this();
        try {

            if (json.has("id")) {
                this.serviceId = json.getString("id");
            }

            if (json.has("service_type")) {
                this.serviceType = json.getString("service_type");
            }

            if (json.has("is_cancelled")) {
                this.isCancelled = json.getBoolean("is_cancelled");
            }

            if (json.has("disruption_reason")) {
                this.disruptionReason = json.getString("disruption_reason");
            }

            if (json.has("overdue_message")) {
                this.overdueMessage = json.getString("overdue_message");
            }

            if (json.has("station")) {
                this.station = new Station(json.getJSONObject("station"));
            }

            if (json.has("origin")) {
                this.origin = new Station(json.getJSONObject("origin"));
            }

            if (json.has("destination")) {
                this.destination = new Station(json.getJSONObject("destination"));
            }

            if (json.has("operator")) {
                this.operator = new Operator(json.getJSONObject("operator"));
            }

            if (json.has("platform")) {
                this.platform = json.getString("platform");
            }

            if (json.has("sta")) {
                this.scheduledTimeArrival = json.getString("sta");
            }

            if (json.has("eta")) {
                this.estimatedTimeArrival = json.getString("eta");
            }

            if (json.has("ata")) {
                this.actualTimeArrival = json.getString("ata");
            }

            if (json.has("std")) {
                this.scheduledTimeDeparture = json.getString("std");
            }

            if (json.has("etd")) {
                this.estimatedTimeDeparture = json.getString("etd");
            }

            if (json.has("atd")) {
                this.actualTimeDeparture = json.getString("atd");
            }

            if (json.has("calling_points")) {
                JSONArray stations = json.getJSONArray("calling_points");
                for (int i = 0; i < stations.length(); i++) {
                    CallingPoint cp = new CallingPoint(stations.getJSONObject(i));
                    if (i == 0) {
                        cp.setIcon(CallingPoint.START);
                    } else if (i == stations.length() - 1) {
                        cp.setIcon(CallingPoint.END);
                    } else {
                        cp.setIcon(CallingPoint.STOP);
                    }
                    this.callingPoints.add(cp);
                }
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public boolean startsHere() {
        String sta = getScheduledTimeArrival();
        if ((sta == null) || (sta.isEmpty())) {
            return true;
        }
        return false;
    }

    public boolean terminatesHere() {
        String std = getScheduledTimeDeparture();
        if ((std == null) || (std.isEmpty())) {
            return true;
        }
        return false;
    }

    public boolean isDelayedArriving() {
        String sta = getScheduledTimeArrival();
        String eta = getScheduledTimeArrival();
        if ((eta == null) || (eta.isEmpty())) {
            return false;
        } else if (eta != sta)  {
            //TODO potentially could be early, needs converting
            return true;
        }
        return false;
    }

    public boolean isDelayedDeparting() {
        String std = getScheduledTimeDeparture();
        String etd = getScheduledTimeDeparture();
        if ((std == null) || (etd.isEmpty())) {
            return false;
        } else if (std != etd)  {
            //TODO potentially could be early, needs converting
            return true;
        }
        return false;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public String getDisruptionReason() {
        return disruptionReason;
    }

    public String getOverdueMessage() {
        return overdueMessage;
    }

    public Station getStation() {
        return station;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getDestination() {
        return destination;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getPlatform() {
        return platform;
    }

    public String getScheduledTimeArrival() {
        return scheduledTimeArrival;
    }

    public String getEstimatedTimeArrival() {
        return estimatedTimeArrival;
    }

    public String getActualTimeArrival() {
        return actualTimeArrival;
    }

    public String getScheduledTimeDeparture() {
        return scheduledTimeDeparture;
    }

    public String getEstimatedTimeDeparture() {
        return estimatedTimeDeparture;
    }

    public String getActualTimeDeparture() {
        return actualTimeDeparture;
    }

    public ArrayList<CallingPoint> getCallingPoints() {
        return callingPoints;
    }

    public String getTime() {
        //TODO Add all other possibilities
        return getScheduledTimeArrival();
    }

    public static Service getByServiceId(String serviceId) {
        JSONObject json = new JSONObject();
        try {
            String jsonString = Utils.httpGet(Utils.API_BASE_URL + "/services/" + URLEncoder.encode(serviceId, "UTF-8"));
            json = new JSONObject(jsonString);
        } catch (JSONException | UnsupportedEncodingException e) {
            Utils.log(e.getMessage());
        }
        return new Service(json);
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", isCancelled=" + isCancelled +
                ", disruptionReason='" + disruptionReason + '\'' +
                ", overdueMessage='" + overdueMessage + '\'' +
                ", station=" + station +
                ", origin=" + origin +
                ", destination=" + destination +
                ", operator=" + operator +
                ", platform='" + platform + '\'' +
                ", scheduledTimeArrival='" + scheduledTimeArrival + '\'' +
                ", estimatedTimeArrival='" + estimatedTimeArrival + '\'' +
                ", actualTimeArrival='" + actualTimeArrival + '\'' +
                ", scheduledTimeDeparture='" + scheduledTimeDeparture + '\'' +
                ", estimatedTimeDeparture='" + estimatedTimeDeparture + '\'' +
                ", actualTimeDeparture='" + actualTimeDeparture + '\'' +
                ", callingPoints=" + callingPoints +
                '}';
    }

}