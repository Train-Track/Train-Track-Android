package uk.co.traintrackapp.traintrack;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;

public class TrainTrack extends Application {

    private User user;
    private LinkedHashMap<String, Station> stations;
    public GoogleApiClient googleApiClient;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Caches all the stations so they are accessible by their UUID
     * @param stations the list of stations
     */
    public void setStations(List<Station> stations) {
        if (this.stations == null) {
            this.stations = new LinkedHashMap<>();
        }
        this.stations.clear();
        for (Station s: stations) {
            this.stations.put(s.getUuid(), s);
        }
    }

    /**
     * The list of all stations
     * @return all cached stations
     */
    public ArrayList<Station> getStations() {
        return new ArrayList<>(stations.values());
    }

    /**
     * Get a station by UUID
     * @param uuid the UUID of the station to return
     * @return the station
     */
    public Station getStation(String uuid) {
        return stations.get(uuid);
    }

    /**
     * Loops through the user journeys until it finds one with that UUID
     * @param uuid the UUID of the journey
     * @return the journey
     */
    public Journey getJourney(String uuid) {
        for (Journey journey : user.getJourneys()) {
            if (journey.getUuid().equals(uuid)) {
                return journey;
            }
        }
        return null;
    }

    /**
     * Set up some default empty objects
     */
    public void onCreate() {
        super.onCreate();
        setStations(new ArrayList<Station>());
        setUser(new User());
    }

}
