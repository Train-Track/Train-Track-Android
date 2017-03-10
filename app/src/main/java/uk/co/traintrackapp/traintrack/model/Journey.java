package uk.co.traintrackapp.traintrack.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Journey {

    private int id;
    private String uuid;
    private ArrayList<JourneyLeg> journeyLegs;

    public Journey() {
        this.id = 0;
        this.uuid = UUID.randomUUID().toString();
        this.journeyLegs = new ArrayList<>();
    }

    public Journey (JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            JSONArray legs = json.getJSONArray("journey_legs");
            for (int i = 0; i < legs.length(); i++) {
                this.journeyLegs.add(new JourneyLeg(legs.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    /**
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
     * @return the origin
     */
    public Station getOrigin() {
        if (journeyLegs.size() > 0) {
            return journeyLegs.get(0).getDepartureStation();
        } else {
            return null;
        }
    }

    /**
     * @return the destination
     */
    public Station getDestination() {
        if (journeyLegs.size() > 0) {
            return journeyLegs.get(journeyLegs.size() - 1).getArrivalStation();
        } else {
            return null;
        }
    }

    /**
     * @return an array list of legs
     */
    public ArrayList<JourneyLeg> getJourneyLegs() {
        return journeyLegs;
    }

    /**
     * return the journey leg
     * @param uuid of the journey
     * @return the journey
     */
    public JourneyLeg getJourneyLeg(String uuid) {
         for (JourneyLeg leg : getJourneyLegs()) {
             if (leg.getUuid().equals(uuid)) {
                 return leg;
             }
         }
        return null;
    }

    /**
     * @param journeyLeg the leg to add to the journey
     */
    public void addJourneyLeg(JourneyLeg journeyLeg) {
        journeyLegs.add(journeyLeg);
    }

    /**
     * @param journeyLeg the leg to remove from the journey
     */
    public void removeJourneyLeg(JourneyLeg journeyLeg) {
        journeyLegs.remove(journeyLeg);
    }

    /**
     * @return a string representation
     */
    public String toString() {
        return getOrigin() + " to " + getDestination();
    }

    /**
     *
     * @return jsonObject the representation of the journey as JSON
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("uuid", getUuid());
            JSONArray jsonArray = new JSONArray();
            for (JourneyLeg journeyLeg : getJourneyLegs()) {
                jsonArray.put(journeyLeg.toJson());
            }
            json.put("journey_legs", jsonArray);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

    /**
     *
     * @param obj object to check
     * @return true if UUIDs are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Journey) {
            Journey journey = (Journey) obj;
            return this.getUuid().equals((journey.getUuid()));
        } else {
            return false;
        }
    }

}
