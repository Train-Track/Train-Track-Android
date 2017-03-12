package uk.co.traintrackapp.traintrack.model;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneyLeg implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String uuid;
    private int journeyId;
    private DateTime scheduledDeparture;
    private DateTime scheduledArrival;
    private DateTime actualDeparture;
    private DateTime actualArrival;
    private String departurePlatform;
    private String arrivalPlatform;
    private Operator operator;
    private Station origin;
    private Station destination;

    public JourneyLeg() {
        this.id = 0;
        this.uuid = null;
        this.scheduledDeparture = null;
        this.scheduledArrival = null;
        this.actualDeparture = null;
        this.actualArrival = null;
        this.departurePlatform = null;
        this.arrivalPlatform = null;
        this.operator = null;
        this.origin = new Station();
        this.destination = new Station();
    }

    public JourneyLeg(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.journeyId = json.getInt("journey_id");
            this.uuid = json.getString("uuid");
            String schDep = json.getString("scheduled_departure");
            this.scheduledDeparture = Utils.getDateTimeFromString(schDep);
            String schArr = json.getString("scheduled_arrival");
            this.scheduledArrival = Utils.getDateTimeFromString(schArr);
            String actualDep = json.getString("actual_departure");
            this.actualDeparture = Utils.getDateTimeFromString(actualDep);
            String actualArr = json.getString("actual_arrival");
            this.actualArrival = Utils.getDateTimeFromString(actualArr);
            this.departurePlatform = json.getString("departure_platform");
            this.arrivalPlatform = json.getString("arrival_platform");
            this.operator = new Operator(json.getJSONObject("operator"));
            this.origin = new Station(json.getJSONObject("origin"));
            this.destination = new Station(json.getJSONObject("destination"));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public int getId() {
        return id;
    }

    public void setJourneyId(int journeyId) {
        this.journeyId = journeyId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getJourneyId() {
        return journeyId;
    }

    public DateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(DateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public DateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(DateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public DateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(DateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public DateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(DateTime actualArrival) {
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

    public void setId(int id) {
        this.id = id;
    }

    public Station getOrigin() {
        return origin;
    }

    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
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
     *
     * @param obj object to check
     * @return true if UUIDs are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JourneyLeg) {
            JourneyLeg journeyLeg = (JourneyLeg) obj;
            return this.getUuid().equals((journeyLeg.getUuid()));
        } else {
            return false;
        }
    }

    /**
     *
     * @return jsonObject the representation of the journey leg as JSON
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("uuid", getUuid());
            json.put("journey_id", getJourneyId());
            json.put("scheduled_departure", Utils.getStringFromDateTime(getScheduledDeparture()));
            json.put("scheduled_arrival", Utils.getStringFromDateTime(getScheduledArrival()));
            json.put("actual_departure", Utils.getStringFromDateTime(getActualDeparture()));
            json.put("actual_arrival", Utils.getStringFromDateTime(getActualArrival()));
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
