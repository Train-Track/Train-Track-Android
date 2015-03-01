package uk.co.traintrackapp.traintrack.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class Journey {

    public static final String FILENAME = "journeys.json";
    private int id;
    private ArrayList<JourneyLeg> journeyLegs;

    public Journey() {
        id = 0;
        journeyLegs = new ArrayList<>();
    }

    public Journey (JSONObject json) {
        super();
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

    /**
     *
     * @return jsonObject the representation of the journey as JSON
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
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
     * Saves list of journeys file
     * @param journeys the list of journeys to save
     * @param context the context in which we are saving them
     */
    public static void saveAll(ArrayList<Journey> journeys, Context context) {
        JSONArray jsonArray = new JSONArray();
        for (Journey journey : journeys) {
            jsonArray.put(journey.toJson());
        }
        Utils.log("SAVING: " + jsonArray.toString());
        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(jsonArray.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }

    }

}
