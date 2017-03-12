package uk.co.traintrackapp.traintrack.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class User implements Serializable {

    public static final String FILENAME = "user.json";
    private int id;
    private String uuid;
    private String email;
    private String username;
    private int points;
    private String imageUrl;
    private ArrayList<Journey> journeys;
    private Station homeStation;
    private Station workStation;
    private ArrayList<Station> favouriteStations;
    private ArrayList<Station> recentStations;

    public User() {
        this.id = 0;
        this.uuid = UUID.randomUUID().toString();
        this.email = "";
        this.username = "";
        this.points = 0;
        this.imageUrl = "";
        this.journeys = new ArrayList<>();
        this.homeStation = new Station();
        this.workStation = new Station();
        this.favouriteStations = new ArrayList<>();
        this.recentStations = new ArrayList<>();
    }

    /**
     * Converts JSON object to user
     * @param json json
     */
    public User(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            this.username = json.getString("username");
            this.email = json.getString("email");
            this.points = json.getInt("points");
            this.imageUrl = json.getString("image_url");
            JSONArray journeys = json.getJSONArray("journeys");
            for (int i = 0; i < journeys.length(); i++) {
                this.journeys.add(new Journey(journeys.getJSONObject(i)));
            }
            this.homeStation = new Station(json.getJSONObject("home_station"));
            this.workStation = new Station(json.getJSONObject("work_station"));
            JSONArray favouriteStations = json.getJSONArray("favourite_stations");
            for (int i = 0; i < favouriteStations.length(); i++) {
                this.favouriteStations.add(new Station(favouriteStations.getJSONObject(i)));
            }
            JSONArray recentStations = json.getJSONArray("recent_stations");
            for (int i = 0; i < recentStations.length(); i++) {
                this.recentStations.add(new Station(recentStations.getJSONObject(i)));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList<Journey> journeys) {
        this.journeys = journeys;
    }

    public Station getHomeStation() {
        return homeStation;
    }

    public void setHomeStation(Station homeStation) {
        this.homeStation = homeStation;
    }

    public Station getWorkStation() {
        return workStation;
    }

    public void setWorkStation(Station workStation) {
        this.workStation = workStation;
    }

    public ArrayList<Station> getFavouriteStations() {
        return favouriteStations;
    }

    public void setFavouriteStations(ArrayList<Station> favouriteStations) {
        this.favouriteStations = favouriteStations;
    }

    public ArrayList<Station> getRecentStations() {
        return recentStations;
    }

    public void addRecentStation(Station station) {
        this.recentStations.remove(station);
        this.recentStations.add(0, station);
        if (this.recentStations.size() > 10) {
            this.recentStations.remove(10);
        }
    }

    /**
     * @return the image
     */
    public Bitmap getImage() {

        try {
            URL url = new URL(getImageUrl());
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Utils.log(e.getMessage());
            return null;
        }
    }

    public boolean isLoggedIn() {
        return getId() > 0;
    }

    /**
     * @return the title
     */
    public String toString() {
        return getUsername();
    }

    /**
     *
     * @return jsonObject the representation of the user as JSON
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("uuid", getUuid());
            json.put("email", getEmail());
            json.put("username", getUsername());
            json.put("points", getPoints());
            json.put("image_url", getImageUrl());
            JSONArray journeys = new JSONArray();
            for (Journey journey : getJourneys()) {
                journeys.put(journey.toJson());
            }
            json.put("journeys", journeys);
            json.put("home_station", getHomeStation().toJson());
            json.put("work_station", getWorkStation().toJson());
            JSONArray favouriteStations = new JSONArray();
            for (Station station : getFavouriteStations()) {
                favouriteStations.put(station.toJson());
            }
            json.put("favourite_stations", favouriteStations);
            JSONArray recentStations = new JSONArray();
            for (Station station : getRecentStations()) {
                recentStations.put(station.toJson());
            }
            json.put("recent_stations", recentStations);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return json;
    }

    /**
     * Saves user to file
     * @param context the context in which we are saving
     */
    public void save(Context context) {
        String output = this.toJson().toString();
        Utils.log("SAVING: " + output);
        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }
    }

    /**
     * Set attributes back to default constructor
     * @param context the context in which we are saving
     */
    public void logout(Context context) {
        id = 0;
        uuid = UUID.randomUUID().toString();
        email = "";
        username = "";
        points = 0;
        imageUrl = "";
        journeys = new ArrayList<>();
        homeStation = new Station();
        workStation = new Station();
        favouriteStations = new ArrayList<>();
        recentStations = new ArrayList<>();
        save(context);
    }

}
