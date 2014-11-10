package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dyl.anjon.es.traintrack.utils.Utils;

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
		return new Station();
	}

	/**
	 * @return the destination
	 */
	public Station getDestination() {
		return new Station();
	}

	public ArrayList<JourneyLeg> getJourneyLegs() {
		ArrayList<JourneyLeg> journeyLegs = new ArrayList<JourneyLeg>();
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
			JourneyLeg.pinAllInBackground(results);
		} catch (ParseException e) {
			Utils.log(e.getMessage());
		}
		return journeyLegs;
	}

}
