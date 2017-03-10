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

public class Badge {

    private int id;
    private String uuid;
    private String name;
    private String description;
    private String imageUrl;
    private int points;
    private String googleAchievementId;

    public Badge() {
        this.id = 0;
        this.uuid = "";
        this.name = "Unknown";
        this.description = "";
        this.imageUrl = "";
        this.points = 0;
        this.googleAchievementId = "";
    }

    /**
     * Converts JSON object to badge
     * @param json
     */
    public Badge(JSONObject json) {
        this();
        try {
            this.id = json.getInt("id");
            this.uuid = json.getString("uuid");
            this.name = json.getString("name");
            this.description = json.getString("description");
            this.imageUrl = json.getString("image_url");
            this.points = json.getInt("points");
            this.googleAchievementId = json.getString("google_achievement_id");
        } catch (JSONException e) {
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
     * @return the UUID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the title
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the image
     */
    public Bitmap getImage() {
        try {
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Utils.log(e.getMessage());
            return null;
        }
    }

    /**
     * @return the number points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return the ID of the badge on Google Play
     */
    public String getGoogleAchievementId() {
        return googleAchievementId;
    }

    /**
     * @return the title
     */
    public String toString() {
        return getName();
    }

    /**
     * @param badge           the badge to add
     * @param googleApiClient the google api to use for games
     */
    public static void addBadgeForCurrentUser(Badge badge, GoogleApiClient googleApiClient) {
        if (googleApiClient.isConnected()) {
            Utils.log("Unlocking google achievement");
            Games.Achievements.unlock(googleApiClient, badge.getGoogleAchievementId());
        }
    }

}
