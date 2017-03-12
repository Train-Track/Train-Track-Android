package uk.co.traintrackapp.traintrack.model;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Service implements Serializable {

    private String serviceId;
    private String serviceType;
    private boolean isCancelled;
    private String disruptionReason;
    private String overdueMessage;
    private Station station;
    private Station origin;
    private Station destination;
    private Operator operator;
    private TubeLine tubeLine;
    private String platform;
    private DateTime scheduledTimeArrival;
    private DateTime estimatedTimeArrival;
    private DateTime actualTimeArrival;
    private DateTime scheduledTimeDeparture;
    private DateTime estimatedTimeDeparture;
    private DateTime actualTimeDeparture;
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
        this.tubeLine = new TubeLine();
        this.platform = "";
        this.scheduledTimeArrival = null;
        this.estimatedTimeArrival = null;
        this.actualTimeArrival = null;
        this.scheduledTimeDeparture = null;
        this.estimatedTimeDeparture = null;
        this.actualTimeDeparture = null;
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

            if (!json.isNull("line")) {
                this.tubeLine = new TubeLine(json.getJSONObject("line"));
            }

            if (json.has("platform")) {
                this.platform = json.getString("platform");
            }

            if (!json.isNull("sta")) {
                String sta = json.getString("sta");
                this.scheduledTimeArrival = Utils.getDateTimeFromString(sta);
            }

            if (!json.isNull("eta")) {
                String eta = json.getString("eta");
                this.estimatedTimeArrival = Utils.getDateTimeFromString(eta);
            }

            if (!json.isNull("ata")) {
                String ata = json.getString("ata");
                this.actualTimeArrival = Utils.getDateTimeFromString(ata);
            }

            if (!json.isNull("std")) {
                String std = json.getString("std");
                this.scheduledTimeDeparture = Utils.getDateTimeFromString(std);
            }

            if (!json.isNull("etd")) {
                String etd = json.getString("etd");
                this.estimatedTimeDeparture = Utils.getDateTimeFromString(etd);
            }

            if (!json.isNull("atd")) {
                String atd = json.getString("atd");
                this.actualTimeDeparture = Utils.getDateTimeFromString(atd);
            }

            if (json.has("calling_points")) {
                JSONArray stations = json.getJSONArray("calling_points");
                for (int i = 0; i < stations.length(); i++) {
                    CallingPoint cp = new CallingPoint(stations.getJSONObject(i));
                    if (cp.isPassingPoint()) {
                        cp.setIcon(CallingPoint.PASS);
                    } else if (i == 0) {
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
            Utils.log("Service: " + e.getMessage());
        }
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

    public TubeLine getTubeLine() {
        return tubeLine;
    }

    public void setTubeLine(TubeLine tubeLine) {
        this.tubeLine = tubeLine;
    }

    public String getPlatform() {
        return platform;
    }

    public DateTime getScheduledTimeArrival() {
        return scheduledTimeArrival;
    }

    public DateTime getEstimatedTimeArrival() {
        return estimatedTimeArrival;
    }

    public DateTime getActualTimeArrival() {
        return actualTimeArrival;
    }

    public DateTime getScheduledTimeDeparture() {
        return scheduledTimeDeparture;
    }

    public DateTime getEstimatedTimeDeparture() {
        return estimatedTimeDeparture;
    }

    public DateTime getActualTimeDeparture() {
        return actualTimeDeparture;
    }

    public ArrayList<CallingPoint> getCallingPoints() {
        return callingPoints;
    }


    public boolean startsHere() {
        if (getScheduledTimeArrival() == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean terminatesHere() {
        if (getScheduledTimeDeparture() == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDelayedArriving() {
        DateTime sta = getScheduledTimeArrival();
        DateTime eta = getScheduledTimeArrival();
        if ((sta == null) || (eta == null)) {
            return false;
        } else if (eta.isAfter(sta)) {
            return true;
        }
        return false;
    }

    public boolean isDelayedDeparting() {
        DateTime std = getScheduledTimeDeparture();
        DateTime etd = getScheduledTimeDeparture();
        if ((std == null) || (etd == null)) {
            return false;
        } else if (etd.isAfter(std)) {
            return true;
        }
        return false;
    }

    public DateTime getTime() {
        if (getScheduledTimeDeparture() != null) {
            return getScheduledTimeDeparture();
        } else {
            return getScheduledTimeArrival();
        }
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