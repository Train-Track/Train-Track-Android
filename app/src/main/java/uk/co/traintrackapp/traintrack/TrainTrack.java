package uk.co.traintrackapp.traintrack;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.model.Badge;
import uk.co.traintrackapp.traintrack.model.Image;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;
import uk.co.traintrackapp.traintrack.model.Operator;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.UserBadge;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class TrainTrack extends Application {

    private ArrayList<Station> stations;
    public GoogleApiClient googleApiClient;

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Station.class);
        ParseObject.registerSubclass(Operator.class);
        ParseObject.registerSubclass(Journey.class);
        ParseObject.registerSubclass(JourneyLeg.class);
        ParseObject.registerSubclass(Image.class);
        ParseObject.registerSubclass(Badge.class);
        ParseObject.registerSubclass(UserBadge.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "rvI3JMtQbb3saBrKk5blfeTGdc1uCVs3ueVqwGq2",
                "3uYi6iO1mE1KMfVm6mqXjHq43Ov9iPmi1u9Htk4q");
        ParsePush.subscribeInBackground("broadcast", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Utils.log("successfully subscribed to the broadcast channel.");
                } else {
                    Utils.log("failed to subscribe to the broadcast channel.");
                }
            }
        });
        ParseUser.enableAutomaticUser();
        setStations(new ArrayList<Station>());
    }

}