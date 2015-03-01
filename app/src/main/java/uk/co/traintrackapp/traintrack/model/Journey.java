package uk.co.traintrackapp.traintrack.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Journey {

    private int id;
    private ArrayList<JourneyLeg> journeyLegs;

    public Journey() {
        id = 0;
        journeyLegs = new ArrayList<>();
    }

    public Journey (JSONObject json) {
        journeyLegs = new ArrayList<>();
        try {
            id = json.getInt("id");
            JSONArray legs = json.getJSONArray("journey_legs");
            for (int i = 0; i < legs.length(); i++) {
                journeyLegs.add(new JourneyLeg(legs.getJSONObject(i)));
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
     * @param journeyLeg the leg to add to the journey
     */
    public void addJourneyLeg(JourneyLeg journeyLeg) {
        journeyLegs.add(journeyLeg);
    }

    /**
     * @return a string representation
     */
    public String toString() {
        return getOrigin() + " to " + getDestination();
    }

}
