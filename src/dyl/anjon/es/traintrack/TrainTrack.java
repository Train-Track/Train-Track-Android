package dyl.anjon.es.traintrack;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import dyl.anjon.es.traintrack.models.Station;

public class TrainTrack extends Application {

	public void onCreate() {
		super.onCreate();
		ParseObject.registerSubclass(Station.class);
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "rvI3JMtQbb3saBrKk5blfeTGdc1uCVs3ueVqwGq2",
				"3uYi6iO1mE1KMfVm6mqXjHq43Ov9iPmi1u9Htk4q");
		ParsePush.subscribeInBackground("broadcast", new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d("com.parse.push",
							"successfully subscribed to the broadcast channel.");
				} else {
					Log.e("com.parse.push", "failed to subscribe for push", e);
				}
			}
		});
		ParseUser.enableAutomaticUser();
	}

}
