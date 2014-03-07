package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.ScheduleLocation;
import dyl.anjon.es.traintrack.models.Station;

public class JourneyLegActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		int journeyLegId = intent.getIntExtra("journey_leg_id", 0);

		if (journeyLegId != 0) {

			setContentView(R.layout.activity_journey_leg);
			JourneyLeg journeyLeg = JourneyLeg.get(this, journeyLegId);

			TextView origin = (TextView) findViewById(R.id.origin);
			origin.setText(journeyLeg.getOrigin().toString());
			TextView destination = (TextView) findViewById(R.id.destination);
			destination.setText(journeyLeg.getDestination().toString());

			TextView departureTime = (TextView) findViewById(R.id.departure_time);
			departureTime.setText(journeyLeg.getDepartureTime());
			TextView arrivalTime = (TextView) findViewById(R.id.arrival_time);
			arrivalTime.setText(journeyLeg.getArrivalTime());

		} else {

			setContentView(R.layout.activity_journey_leg_form);
			final int scheduleId = intent.getIntExtra("schedule_id", 0);

			int originScheduleLocationId = intent.getIntExtra(
					"origin_schedule_location_id", 0);
			ScheduleLocation originScheduleLocation = ScheduleLocation.get(
					this, originScheduleLocationId);
			TextView origin = (TextView) findViewById(R.id.origin);
			origin.setText(originScheduleLocation.getStation().toString());

			int destinationScheduleLocationId = intent.getIntExtra(
					"destination_schedule_location_id", 0);
			ScheduleLocation destinationScheduleLocation = ScheduleLocation
					.get(this, destinationScheduleLocationId);
			TextView destination = (TextView) findViewById(R.id.destination);
			destination.setText(destinationScheduleLocation.getStation()
					.toString());

		}

	}

}
