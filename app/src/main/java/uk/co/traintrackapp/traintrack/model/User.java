package uk.co.traintrackapp.traintrack.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class User {

    private int id;
    private String email;
    private String username;
    private int points;
    private String imageUrl;

    public User() {
        id = 0;
        email = "";
        username = "";
        points = 0;
        imageUrl = "";
    }

    /**
     * Converts JSON object to user
     * @param json json
     */
    public User(JSONObject json) {
        super();
        try {
            this.id = json.getInt("id");
            this.username = json.getString("username");
            this.email = json.getString("email");
            this.points = json.getInt("points");
            this.imageUrl = json.getString("image_url");
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public int getId() {
        return id;
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

    /**
     * @return the title
     */
    public String toString() {
        return getUsername();
    }

}
