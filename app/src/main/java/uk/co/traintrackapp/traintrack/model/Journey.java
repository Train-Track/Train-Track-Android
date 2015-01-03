package uk.co.traintrackapp.traintrack.model;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import uk.co.traintrackapp.traintrack.utils.Utils;


@ParseClassName("Journey")
public class Journey extends ParseObject {

    public Journey() {
    }

    /**
     * @return the user
     */
    public ParseUser getUser() {
        return getParseUser("user");
    }

    /**
     * @param user
     *            the person who did the journey
     */
    public void setUser(ParseUser user) {
        put("user", user);
    }

    /**
     * @return the origin
     */
    public Station getOrigin() {
        ArrayList<JourneyLeg> journeyLegs = getJourneyLegs();
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
        ArrayList<JourneyLeg> journeyLegs = getJourneyLegs();
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
        ArrayList<JourneyLeg> journeyLegs = new ArrayList<>();
        ParseQuery<JourneyLeg> query = ParseQuery.getQuery(JourneyLeg.class);
        query.whereEqualTo("journey", this);
        query.fromLocalDatastore();
        try {
            if (query.count() == 0) {
                query = ParseQuery.getQuery(JourneyLeg.class);
                query.whereEqualTo("journey", this);
            }
            List<JourneyLeg> results = query.find();
            journeyLegs.addAll(results);
            JourneyLeg.pinAllInBackground("JourneyLegs", results);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return journeyLegs;
    }

    /**
     * @return a string representation
     */
    public String toString() {
        return getOrigin() + " to " + getDestination();
    }

}
