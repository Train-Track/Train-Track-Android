package uk.co.traintrackapp.traintrack.api;

import org.w3c.dom.Element;

import uk.co.traintrackapp.traintrack.R;

public class CallingPoint {

    public static int START = R.drawable.start;
    public static int STOP = R.drawable.stop;
    public static int END = R.drawable.end;

    private String stationCrs;
    private String stationName;
    private String scheduledTime;
    private String estimatedTime;
    private String actualTime;
    private int icon;

    public CallingPoint() {

    }

    public CallingPoint(Element cp) {
        if (cp.getElementsByTagName("crs").getLength() > 0) {
            setStationCrs(cp.getElementsByTagName("crs").item(0)
                    .getTextContent());
			/*
			 * setStationByCrs(cp.getElementsByTagName("crs").item(0)
			 * .getTextContent());
			 */
        }
        if (cp.getElementsByTagName("locationName").getLength() > 0) {
            setStationName(cp.getElementsByTagName("locationName").item(0)
                    .getTextContent());
        }
        if (cp.getElementsByTagName("st").getLength() > 0) {
            setScheduledTime(cp.getElementsByTagName("st").item(0)
                    .getTextContent());
        }
        if (cp.getElementsByTagName("et").getLength() > 0) {
            setEstimatedTime(cp.getElementsByTagName("et").item(0)
                    .getTextContent());
        }
        if (cp.getElementsByTagName("at").getLength() > 0) {
            setActualTime(cp.getElementsByTagName("at").item(0)
                    .getTextContent());
        }
    }

    public String getStationCrs() {
        return stationCrs;
    }

    public void setStationCrs(String stationCrs) {
        this.stationCrs = stationCrs;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String toString() {
        return getStationName() + "\n";
    }

    public boolean hasArrived() {
        if (getActualTime() != null) {
            return true;
        } else {
            return false;
        }
    }

}