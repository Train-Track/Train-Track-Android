package uk.co.traintrackapp.traintrack.api;

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
    private ArrayList<ServiceItem> services;
    private String status;

    public TubeLine() {
        id = 0;
        uuid = "";
        name = "";
        number = "";
        backgroundColour = "";
        textColour = "";
        services = new ArrayList<>();
        status = "";
    }

    public TubeLine(JSONObject json) {
        this();
        try {
            id = json.getInt("id");
            uuid = json.getString("uuid");
            name = json.getString("name");
            number = json.getString("number");
            backgroundColour = json.getString("background_colour");
            textColour = json.getString("text_colour");
            if (!json.isNull("services")) {
                JSONArray jsonServices = json.getJSONArray("services");
                for (int i = 0; i < jsonServices.length(); i++) {
                    ServiceItem item = new ServiceItem(jsonServices.getJSONObject(i));
                    services.add(item);
                }
            }
            if (!json.isNull("status")) {
                status = json.getString("status");
            }
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

    public ArrayList<ServiceItem> getServices() {
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
