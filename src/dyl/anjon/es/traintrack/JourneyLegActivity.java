package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.ScheduleLocation;

public class JourneyLegActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final int journeyLegId = intent.getIntExtra("journey_leg_id", 0);
		final int journeyId = intent.getIntExtra("journey_id", 0);

		if (journeyLegId != 0) {

			setContentView(R.layout.activity_journey_leg);
			JourneyLeg journeyLeg = JourneyLeg.get(this, journeyLegId);

			TextView departureStation = (TextView) findViewById(R.id.departure_station);
			departureStation.setText(journeyLeg.getOrigin().toString());			

			TextView departureTime = (TextView) findViewById(R.id.departure_time);
			departureTime.setText(journeyLeg.getDepartureTime());
			
			TextView departurePlatform = (TextView) findViewById(R.id.departure_platform);
			departurePlatform.setText(journeyLeg.getDeparturePlatform());
			
			TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
			arrivalStation.setText(journeyLeg.getDestination().toString());
			
			TextView arrivalTime = (TextView) findViewById(R.id.arrival_time);
			arrivalTime.setText(journeyLeg.getArrivalTime());

			TextView arrivalPlatform = (TextView) findViewById(R.id.arrival_platform);
			arrivalPlatform.setText(journeyLeg.getArrivalPlatform());

		} else {

			setContentView(R.layout.activity_journey_leg_form);
			final Button saveButton = (Button) findViewById(R.id.save);
			final int scheduleId = intent.getIntExtra("schedule_id", 0);

			int originScheduleLocationId = intent.getIntExtra(
					"origin_schedule_location_id", 0);
			ScheduleLocation originScheduleLocation = ScheduleLocation.get(
					this, originScheduleLocationId);

			TextView departureStation = (TextView) findViewById(R.id.departure_station);
			departureStation.setText(originScheduleLocation.getStation()
					.toString());

			TimePicker departureTime = (TimePicker) findViewById(R.id.departure_time);
			int departureHour = Integer.valueOf(originScheduleLocation
					.getTime().split(":")[0]);
			departureTime.setCurrentHour(departureHour);
			int departureMinute = Integer.valueOf(originScheduleLocation
					.getTime().split(":")[1]);
			departureTime.setCurrentMinute(departureMinute);

			EditText departurePlatform = (EditText) findViewById(R.id.departure_platform);
			departurePlatform.setText(originScheduleLocation.getPlatform());

			int destinationScheduleLocationId = intent.getIntExtra(
					"destination_schedule_location_id", 0);
			ScheduleLocation destinationScheduleLocation = ScheduleLocation
					.get(this, destinationScheduleLocationId);

			TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
			arrivalStation.setText(destinationScheduleLocation.getStation()
					.toString());

			TimePicker arrivalTime = (TimePicker) findViewById(R.id.arrival_time);
			int arrivalHour = Integer.valueOf(destinationScheduleLocation
					.getTime().split(":")[0]);
			arrivalTime.setCurrentHour(arrivalHour);
			int arrivalMinute = Integer.valueOf(destinationScheduleLocation
					.getTime().split(":")[1]);
			arrivalTime.setCurrentMinute(arrivalMinute);

			EditText arrivalPlatform = (EditText) findViewById(R.id.arrival_platform);
			arrivalPlatform.setText(destinationScheduleLocation.getPlatform());

			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (journeyId == 0) {
						// create new journey
					}

					// create new journey leg

				}
			});

		}

	}

}
