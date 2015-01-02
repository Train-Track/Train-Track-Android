package uk.co.traintrackapp.traintrack.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserBadge")
public class UserBadge extends ParseObject {

    public UserBadge() {
    }

    /**
     * @return the badge
     */
    public Badge getBadge() {
        return (Badge) getParseObject("badge");
    }

    /**
     * @param badge
     *            the badge
     */
    public void setBadge(Badge badge) {
        put("badge", badge);
    }

    /**
     * @param user
     *            the badge holder
     */
    public void setUser(ParseUser user) {
        put("user", user);
    }

    /**
     * @return the title
     */
    public String toString() {
        return getBadge().toString();
    }

}