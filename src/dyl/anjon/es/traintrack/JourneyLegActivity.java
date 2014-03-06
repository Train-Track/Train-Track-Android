package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import dyl.anjon.es.traintrack.models.JourneyLeg;

public class JourneyLegActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		int journeyLegId = intent.getIntExtra("journey_leg_id", 0);

		if (journeyLegId != 0) {

			setContentView(R.layout.activity_journey_leg);
			JourneyLeg journeyLeg = JourneyLeg.get(this, journeyLegId);
			final TextView name = (TextView) findViewById(R.id.name);
			name.setText(journeyLeg.getOrigin().toString() + " to "
					+ journeyLeg.getDestination().toString());

		} else {

			setContentView(R.layout.activity_journey_leg_form);
			int scheduleId = intent.getIntExtra("schedule_id", 0);
			int originStationId = intent.getIntExtra("origin_station_id", 0);
			int destinationStationId = intent.getIntExtra(
					"destination_station_id", 0);
			String departureTime = intent.getStringExtra("departure_time");
			String arrivalTime = intent.getStringExtra("arrival_time");

		}

	}

}
