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
    private LinkedHashMap<String, Journey> journeys;
    private LinkedHashMap<String, Operator> operators;
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
        return this.stations.get(crsCode);
    }

    public void setJourneys(List<Journey> journeys) {
        if (this.journeys == null) {
            this.journeys = new LinkedHashMap<>();
        }
        this.journeys.clear();
        for (Journey j: journeys) {
            this.journeys.put(String.valueOf(j.getUuid()), j);
        }
    }

    public void addJourney(Journey journey) {
        ArrayList<Journey> journeys = getJourneys();
        journeys.add(journey);
        setJourneys(journeys);
    }

    public void removeJourney(Journey journey) {
        ArrayList<Journey> journeys = getJourneys();
        journeys.remove(journey);
        setJourneys(journeys);
    }

    public ArrayList<Journey> getJourneys() {
        return new ArrayList<>(journeys.values());
    }

    public Journey getJourney(String uuid) {
        return this.journeys.get(uuid);
    }

    public Operator getOperator(String operatorCode) {
        return this.operators.get(operatorCode);
    }

    public void setOperators(List<Operator> operators) {
        if (this.operators == null) {
            this.operators = new LinkedHashMap<>();
        }
        this.operators.clear();
        for (Operator o: operators) {
            this.operators.put(o.getCode(), o);
        }
    }

    public void onCreate() {
        super.onCreate();
        setStations(new ArrayList<Station>());
        setJourneys(new ArrayList<Journey>());
        setOperators(new ArrayList<Operator>());
    }

}
