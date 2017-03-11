package uk.co.traintrackapp.traintrack.model;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class CallingPoint {

    public static final int START = R.drawable.start;
    public static final int STOP = R.drawable.stop;
    public static final int END = R.drawable.end;

    private Station station;
    private Tiploc tiploc;
    private DateTime scheduledTimeArrival;
    private DateTime estimatedTimeArrival;
    private DateTime actualTimeArrival;
    private DateTime scheduledTimeDeparture;
    private DateTime estimatedTimeDeparture;
    private DateTime actualTimeDeparture;
    private boolean cancelled;
    private boolean passingPoint;
    private boolean noReport;
    private int icon;

    public CallingPoint() {
        this.station = new Station();
        this.scheduledTimeArrival = null;
        this.estimatedTimeArrival = null;
        this.actualTimeArrival = null;
        this.scheduledTimeDeparture = null;
        this.estimatedTimeDeparture = null;
        this.actualTimeDeparture = null;
        this.cancelled = false;
        this.passingPoint = false;
        this.noReport = false;
    }

    public CallingPoint(JSONObject json) {
        this();
        try {

            if (!json.isNull("station")) {
                this.station = new Station(json.getJSONObject("station"));
            }

            if (!json.isNull("tiploc")) {
                this.tiploc = new Tiploc(json.getJSONObject("tiploc"));
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

            if (json.has("cancelled")) {
                this.cancelled = json.getBoolean("cancelled");
            }

            if (json.has("pass")) {
                this.passingPoint = json.getBoolean("pass");
            }

            if (json.has("no_report")) {
                this.noReport = json.getBoolean("no_report");
            }

        } catch (JSONException e) {
            Utils.log("Calling Point: " + e.getMessage());
        }
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }

    public Tiploc getTiploc() {
        return tiploc;
    }

    public void setTiploc(Tiploc tiploc) {
        this.tiploc = tiploc;
    }

    public DateTime getScheduledTimeArrival() {
        return scheduledTimeArrival;
    }

    public void setScheduledTimeArrival(DateTime scheduledTimeArrival) {
        this.scheduledTimeArrival = scheduledTimeArrival;
    }

    public DateTime getEstimatedTimeArrival() {
        return estimatedTimeArrival;
    }

    public void setEstimatedTimeArrival(DateTime estimatedTimeArrival) {
        this.estimatedTimeArrival = estimatedTimeArrival;
    }

    public DateTime getActualTimeArrival() {
        return actualTimeArrival;
    }

    public void setActualTimeArrival(DateTime actualTimeArrival) {
        this.actualTimeArrival = actualTimeArrival;
    }

    public DateTime getScheduledTimeDeparture() {
        return scheduledTimeDeparture;
    }

    public void setScheduledTimeDeparture(DateTime scheduledTimeDeparture) {
        this.scheduledTimeDeparture = scheduledTimeDeparture;
    }

    public DateTime getEstimatedTimeDeparture() {
        return estimatedTimeDeparture;
    }

    public void setEstimatedTimeDeparture(DateTime estimatedTimeDeparture) {
        this.estimatedTimeDeparture = estimatedTimeDeparture;
    }

    public DateTime getActualTimeDeparture() {
        return actualTimeDeparture;
    }

    public void setActualTimeDeparture(DateTime actualTimeDeparture) {
        this.actualTimeDeparture = actualTimeDeparture;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isPassingPoint() {
        return passingPoint;
    }

    public void setPassingPoint(boolean passingPoint) {
        this.passingPoint = passingPoint;
    }

    public boolean isNoReport() {
        return noReport;
    }

    public void setNoReport(boolean noReport) {
        this.noReport = noReport;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String toString() {
        return getScheduledTime() + " " + getName() + " " + getTime() + "\n";
    }

    public String getName() {
        if (!getStation().getName().equals(Station.DEFAULT_NAME)) {
            return getStation().toString();
        } else if (!getTiploc().getName().equals(Tiploc.DEFAULT_NAME)) {
            return getTiploc().toString();
        } else {
            return Station.DEFAULT_NAME;
        }
    }

    public boolean hasArrived() {
        return getActualTimeArrival() != null;
    }

    public boolean isOnTime() {
        if (getEstimatedTimeDeparture() != null) {
            return getEstimatedTimeDeparture().isAfter(getScheduledTimeDeparture());
        } else if (getEstimatedTimeArrival() != null) {
            return getEstimatedTimeArrival().isAfter(getScheduledTimeArrival());
        }
        else {
            return true;
        }
    }

    public DateTime getScheduledTime() {
        if (getScheduledTimeDeparture() != null) {
            return getScheduledTimeDeparture();
        } else {
            return getScheduledTimeArrival();
        }
    }

    public DateTime getTime() {
        if (getActualTimeDeparture() != null) {
            return getActualTimeDeparture();
        } else if (getEstimatedTimeDeparture() != null) {
            return getEstimatedTimeDeparture();
        }  else {
            return getScheduledTime();
        }
    }

}