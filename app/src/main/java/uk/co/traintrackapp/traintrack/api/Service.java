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
    private Operator operator;
    private String platform;
    private String scheduledTimeArrival;
    private String estimatedTimeArrival;
    private String actualTimeArrival;
    private String scheduledTimeDeparture;
    private String estimatedTimeDeparture;
    private String actualTimeDeparture;
    private ArrayList<CallingPoint> previousCallingPoints;
    private ArrayList<CallingPoint> subsequentCallingPoints;
    private ArrayList<CallingPoint> callingPoints;

    private Service(JSONObject json) {
        previousCallingPoints = new ArrayList<>();
        subsequentCallingPoints = new ArrayList<>();
        callingPoints = new ArrayList<>();

        try {
            serviceId = json.getString("id");
            serviceType = json.getString("service_type");
            isCancelled = json.getBoolean("is_cancelled");
            disruptionReason = json.getString("disruption_reason");
            overdueMessage = json.getString("overdue_message");
            station = new Station(json.getJSONObject("station"));
            operator = new Operator(json.getJSONObject("operator"));
            platform = json.getString("platform");
            scheduledTimeArrival = json.getString("sta");
            estimatedTimeArrival = json.getString("eta");
            actualTimeArrival = json.getString("ata");
            scheduledTimeDeparture = json.getString("std");
            estimatedTimeDeparture = json.getString("etd");
            actualTimeDeparture = json.getString("atd");
            JSONArray prevArray = json.getJSONArray("previous_calling_points");
            for (int i = 0; i < prevArray.length(); i++) {
                CallingPoint cp = new CallingPoint(prevArray.getJSONObject(i));
                if (i == 0) {
                    cp.setIcon(CallingPoint.START);
                } else {
                    cp.setIcon(CallingPoint.STOP);
                }
                previousCallingPoints.add(cp);
            }
            JSONArray subsArray = json.getJSONArray("subsequent_calling_points");
            for (int i = 0; i < subsArray.length(); i++) {
                CallingPoint cp = new CallingPoint(subsArray.getJSONObject(i));
                if (i == subsArray.length() -1) {
                    cp.setIcon(CallingPoint.END);
                } else {
                    cp.setIcon(CallingPoint.STOP);
                }
                subsequentCallingPoints.add(cp);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        CallingPoint thisCallingPoint = new CallingPoint();
        thisCallingPoint.setStation(station);
        if (terminatesHere()) {
            thisCallingPoint.setScheduledTime(getScheduledTimeArrival());
            thisCallingPoint.setEstimatedTime(getEstimatedTimeArrival());
            thisCallingPoint.setActualTime(getActualTimeArrival());
        } else {
            thisCallingPoint.setScheduledTime(getScheduledTimeDeparture());
            thisCallingPoint.setEstimatedTime(getEstimatedTimeDeparture());
            thisCallingPoint.setActualTime(getActualTimeDeparture());
        }
        if (getSubsequentCallingPoints().size() == 0) {
            thisCallingPoint.setIcon(CallingPoint.END);
        } else if (getPreviousCallingPoints().size() == 0) {
            thisCallingPoint.setIcon(CallingPoint.START);
        } else {
            thisCallingPoint.setIcon(CallingPoint.STOP);
        }
        callingPoints.addAll(getPreviousCallingPoints());
        callingPoints.add(thisCallingPoint);
        callingPoints.addAll(getSubsequentCallingPoints());
    }

    private boolean terminatesHere() {
        return getScheduledTimeDeparture() == null;
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

    public ArrayList<CallingPoint> getPreviousCallingPoints() {
        return previousCallingPoints;
    }

    public ArrayList<CallingPoint> getSubsequentCallingPoints() {
        return subsequentCallingPoints;
    }

    public ArrayList<CallingPoint> getCallingPoints() {
        return callingPoints;
    }

    public String toString() {
        return callingPoints.toString();
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

}