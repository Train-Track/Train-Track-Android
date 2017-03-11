package uk.co.traintrackapp.traintrack.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class TubeLine {

    private int id;
    private String uuid;
    private String name;
    private String number;
    private String backgroundColour;
    private String textColour;
    private ArrayList<Service> services;
    private String status;

    private TubeLine() {
        this.id = 0;
        this.uuid = "";
        this.name = "";
        this.number = "";
        this.backgroundColour = "";
        this.textColour = "";
        this.services = new ArrayList<>();
        this.status = "";
    }

    protected TubeLine(JSONObject json) {
        this();
        try {

            if (json.has("id")) {
                this.id = json.getInt("id");
            }

            if (json.has("uuid")) {
                this.uuid = json.getString("uuid");
            }

            if (json.has("name")) {
                this.name = json.getString("name");
            }

            if (json.has("number")) {
                this.number = json.getString("number");
            }

            if (json.has("background_colour")) {
                this.backgroundColour = json.getString("background_colour");
            }

            if (json.has("text_colour")) {
                this.textColour = json.getString("text_colour");
            }

             if (!json.isNull("services")) {
                JSONArray jsonServices = json.getJSONArray("services");
                for (int i = 0; i < jsonServices.length(); i++) {
                    Service service = new Service(jsonServices.getJSONObject(i));
                    services.add(service);
                }
            }

            if (json.has("status")) {
                status = json.getString("status");
            }

        } catch (JSONException e) {
            Utils.log("Tube Line: " + e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getBackgroundColour() {
        return backgroundColour;
    }

    public String getTextColour() {
        return textColour;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        return name;
    }

    /**
     * Get the live status of the underground lines
     * @return the list of tube lines
     */
    public static ArrayList<TubeLine> getStatuses() {
        ArrayList<TubeLine> tubeLines = new ArrayList<>();
        String jsonString = Utils.httpGet(Utils.API_BASE_URL + "/tube");
        try {
            JSONArray jsonTubeLines = new JSONArray(jsonString);
            for (int i = 0; i < jsonTubeLines.length(); i++) {
                tubeLines.add(new TubeLine(jsonTubeLines.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return tubeLines;
    }

}
