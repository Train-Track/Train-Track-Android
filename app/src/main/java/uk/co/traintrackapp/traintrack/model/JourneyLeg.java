package uk.co.traintrackapp.traintrack.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneyLeg {

    private int id;
    private int journeyId;
    private Date scheduledDeparture;
    private Date scheduledArrival;
    private Date actualDeparture;
    private Date actualArrival;
    private String departurePlatform;
    private String arrivalPlatform;
    private Operator operator;
    private Station origin;
    private Station destination;

    public JourneyLeg() {
        id = 0;
    }

    public JourneyLeg(JSONObject json) {
        try {
            id = json.getInt("id");
            journeyId = json.getInt("journey_id");
            //TODO format dates
            String schDep = json.getString("scheduled_departure");
            String schArr = json.getString("scheduled_arrival");
            String actualDep = json.getString("actual_departure");
            String actualArr = json.getString("actual_arrival");
            String departurePlatform = json.getString("departure_platform");
            String arrivalPlatform = json.getString("arrival_platform");
            operator = new Operator(json.getJSONObject("operator"));
            origin = new Station(json.getJSONObject("origin"));
            destination = new Station(json.getJSONObject("destination"));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public int getId() {
        return id;
    }

    public Operator getOperator() {
        return operator;
    }

    /**
     * @return the departure station
     */
    public Station getDepartureStation() {
        return origin;
    }

    /**
     * @param departureStation
     *            the departure station
     */
    public void setDepartureStation(Station departureStation) {
        this.origin =  departureStation;
    }

    /**
     * @return the departure platform
     */
    public String getDeparturePlatform() {
        return departurePlatform;
    }

    /**
     * @param departurePlatform
     *            the departure platform
     */
    public void setDeparturePlatform(String departurePlatform) {
        this.departurePlatform = departurePlatform;
    }

    /**
     * @return the departure time
     */
    public Date getDepartureTime() {
        return scheduledDeparture;
    }

    /**
     * @param departureTime
     *            the departure time
     */
    public void setDepartureTime(Date departureTime) {
        this.scheduledDeparture = departureTime;
    }

    /**
     * @return the arrival station
     */
    public Station getArrivalStation() {
        return destination;
    }

    /**
     * @param arrivalStation
     *            the arrival station
     */
    public void setArrivalStation(Station arrivalStation) {
        this.destination = arrivalStation;
    }

    /**
     * @return the arrival platform
     */
    public String getArrivalPlatform() {
        return arrivalPlatform;
    }

    /**
     * @param arrivalPlatform
     *            the departure platform
     */
    public void setArrivalPlatform(String arrivalPlatform) {
        this.arrivalPlatform = arrivalPlatform;
    }

    /**
     * @return the arrival time
     */
    public Date getArrivalTime() {
        return scheduledArrival;
    }

    /**
     * @param arrivalTime
     *            the arrival time
     */
    public void setArrivalTime(Date arrivalTime) {
        this.scheduledArrival = arrivalTime;
    }

    /**
     * @return the name
     */
    @Override
    public String toString() {
        return this.getDepartureStation() + " to "
                + this.getArrivalStation();
    }

    /**
     * @return the departure time as hh:mm
     */
    public String getDepartureTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        return dateFormat.format(getDepartureTime());
    }

    /**
     * @return the arrival time as hh:mm
     */
    public String getArrivalTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        return dateFormat.format(getArrivalTime());
    }

}
