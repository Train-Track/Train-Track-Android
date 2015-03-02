package uk.co.traintrackapp.traintrack.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneyLeg {

    private int id;
    private String uuid;
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
        uuid = UUID.randomUUID().toString();
    }

    public JourneyLeg(JSONObject json) {
        super();
        try {
            id = json.getInt("id");
            journeyId = json.getInt("journey_id");
            uuid = json.getString("uuid");
            String schDep = json.getString("scheduled_departure");
            scheduledDeparture = Utils.getDateFromString(schDep);
            String schArr = json.getString("scheduled_arrival");
            scheduledArrival = Utils.getDateFromString(schArr);
            String actualDep = json.getString("actual_departure");
            actualDeparture = Utils.getDateFromString(actualDep);
            String actualArr = json.getString("actual_arrival");
            actualArrival = Utils.getDateFromString(actualArr);
            departurePlatform = json.getString("departure_platform");
            arrivalPlatform = json.getString("arrival_platform");
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

    public String getUuid() {
        return uuid;
    }

    public int getJourneyId() {
        return journeyId;
    }

    public Date getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(Date scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public Date getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(Date scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public Date getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(Date actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public Date getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(Date actualArrival) {
        this.actualArrival = actualArrival;
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
     *
     * @param operator the operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
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
        return dateFormat.format(getScheduledDeparture());
    }

    /**
     * @return the arrival time as hh:mm
     */
    public String getArrivalTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        return dateFormat.format(getScheduledArrival());
    }

    /**
     *
     * @return jsonObject the representation of the journey leg as JSON
     */
    public JSONObject toJson() {
        //TODO get the real times
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("uuid", getUuid());
            json.put("journey_id", getJourneyId());
            json.put("scheduled_departure", Utils.getStringFromDate(getScheduledDeparture()));
            json.put("scheduled_arrival", Utils.getStringFromDate(getScheduledArrival()));
            json.put("actual_departure", Utils.getStringFromDate(getActualDeparture()));
            json.put("actual_arrival", Utils.getStringFromDate(getActualArrival()));
            json.put("departure_platform", getDeparturePlatform());
            json.put("arrival_platform", getArrivalPlatform());
            json.put("operator", getOperator().toJson());
            json.put("origin", getDepartureStation().toJson());
            json.put("destination", getArrivalStation().toJson());
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

}
