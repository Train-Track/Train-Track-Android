package uk.co.traintrackapp.traintrack.model;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import uk.co.traintrackapp.traintrack.utils.Utils;

@ParseClassName("Badge")
public class Badge extends ParseObject {

    public Badge() {
    }

    /**
     * @return the title
     */
    public String getName() {
        return getString("name");
    }

    /**
     * @return the title
     */
    public String toString() {
        return getName();
    }

    // adds a badge for current user
    public static void addBadgeForCurrentUser(Badge badge, GoogleApiClient googleApiClient) {
        UserBadge userBadge = new UserBadge();
        userBadge.put("badge", "I37qAX0WNr");
        userBadge.setUser(ParseUser.getCurrentUser());
        userBadge.saveEventually();
        if (googleApiClient.isConnected()) {
            Utils.log("Unlocking google achievement");
            Games.Achievements.unlock(googleApiClient, "CgkI3tvxjcUJEAIQAQ");
        }
    }

}