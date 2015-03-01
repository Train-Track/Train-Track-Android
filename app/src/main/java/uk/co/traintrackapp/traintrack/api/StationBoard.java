package uk.co.traintrackapp.traintrack.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationBoard {

    private Boolean isPlatformAvailable;
    private Boolean areServicesAvailable;
    private ArrayList<String> nrccMessages;
    private ArrayList<ServiceItem> trainServices;

    public StationBoard(JSONObject json) {
        trainServices = new ArrayList<ServiceItem>();
        nrccMessages = new ArrayList<String>();
        try {
            isPlatformAvailable = json.getBoolean("platform_available");
            areServicesAvailable = json.getBoolean("services_available");
            JSONArray jsonNrccMessages = json.getJSONArray("nrcc_messages");
            for (int i = 0; i < jsonNrccMessages.length(); i++) {
                nrccMessages.add(jsonNrccMessages.getString(i));
            }
            JSONArray jsonTrainServices = json.getJSONArray("train_services");
            for (int i = 0; i < jsonTrainServices.length(); i++) {
                ServiceItem item = new ServiceItem(jsonTrainServices.getJSONObject(i));
                trainServices.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public Boolean isPlatformAvailable() {
        return isPlatformAvailable;
    }

    public Boolean areServicesAvailable() {
        return areServicesAvailable;
    }

    public ArrayList<String> getNrccMessages() {
        return nrccMessages;
    }

    public ArrayList<ServiceItem> getTrainServices() {
        return trainServices;
    }

    public String toString() {
        return trainServices.toString();
    }

}