package uk.co.traintrackapp.traintrack;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.Operator;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

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

    public void setStations(List<Station> stations) {
        if (this.stations == null) {
            this.stations = new LinkedHashMap<>();
        }
        this.stations.clear();
        for (Station s: stations) {
            this.stations.put(s.getCrsCode(), s);
        }
    }

    public ArrayList<Station> getStations() {
        return new ArrayList<>(stations.values());
    }

    public Station getStation(String crsCode) {
        return stations.get(crsCode);
    }

    public Journey getJourney(String uuid) {
        for (Journey journey : user.getJourneys()) {
            if (journey.getUuid().equals(uuid)) {
                return journey;
            }
        }
        return null;
    }

    public void onCreate() {
        super.onCreate();
        setStations(new ArrayList<Station>());
        setUser(new User());
    }

}
