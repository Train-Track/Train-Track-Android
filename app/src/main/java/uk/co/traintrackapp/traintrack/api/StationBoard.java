package uk.co.traintrackapp.traintrack.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationBoard {

    private ArrayList<String> nrccMessages;
    private ArrayList<Service> trainServices;
    private ArrayList<TubeLine> tubeLines;

    private StationBoard() {
        this.nrccMessages = new ArrayList<>();
        this.trainServices = new ArrayList<>();
        this.tubeLines = new ArrayList<>();
    }

    private StationBoard(JSONObject json) {
        this();
        try {

            if (json.has("nrcc_messages")) {
                JSONArray jsonNrccMessages = json.getJSONArray("nrcc_messages");
                for (int i = 0; i < jsonNrccMessages.length(); i++) {
                    this.nrccMessages.add(jsonNrccMessages.getString(i));
                }
            }

            if (json.has("train_services")) {
                JSONArray jsonTrainServices = json.getJSONArray("train_services");
                for (int i = 0; i < jsonTrainServices.length(); i++) {
                    Service service = new Service(jsonTrainServices.getJSONObject(i));
                    this.trainServices.add(service);
                }
            }

            if (json.has("tube_lines")) {
                JSONArray jsonTubeLines = json.getJSONArray("tube_lines");
                for (int i = 0; i < jsonTubeLines.length(); i++) {
                    TubeLine line = new TubeLine(jsonTubeLines.getJSONObject(i));
                    this.tubeLines.add(line);
                }
            }

        } catch (JSONException e) {
            Utils.log("Station Board: " + e.getMessage());
        }
    }

    public ArrayList<String> getNrccMessages() {
        return nrccMessages;
    }

    public ArrayList<Service> getTrainServices() {
        return trainServices;
    }

    public ArrayList<TubeLine> getTubeLines() {
        return tubeLines;
    }

    public static StationBoard getDepartures(String uuid) {
        return getStationBoard(uuid + "/departures");
    }

    public static StationBoard getArrivals(String uuid) {
        return getStationBoard(uuid + "/arrivals");
    }

    public static StationBoard getUnderground(String uuid) {
        return getStationBoard(uuid + "/tube");
    }

    private static StationBoard getStationBoard(String path) {
        JSONObject json = new JSONObject();
        String jsonString = Utils.httpGet(Utils.API_BASE_URL + "/stations/" + path);
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return new StationBoard(json);
    }

    @Override
    public String toString() {
        return "StationBoard{" +
                "nrccMessages=" + nrccMessages +
                ", trainServices=" + trainServices +
                ", tubeLines=" + tubeLines +
                '}';
    }
}
