package uk.co.traintrackapp.tracktrack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Station {
    public String crsCode;
    public String name;

    public Station(String crsCode, String name) {
        this.crsCode = crsCode;
        this.name = name;
    }

    /**
     * An array of stations.
     */
    public static List<Station> STATIONS = new ArrayList<Station>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Station> STATIONS_MAP = new HashMap<String, Station>();

    static {
        // Add 3 sample items.
        addItem(new Station("PAD", "London Paddington"));
        addItem(new Station("CDF", "Cardiff Central"));
        addItem(new Station("BRI", "Bristol Temple Meads"));
    }

    private static void addItem(Station item) {
        STATIONS.add(item);
        STATIONS_MAP.put(item.crsCode, item);
    }

    @Override
    public String toString() {
        return name;
    }
}