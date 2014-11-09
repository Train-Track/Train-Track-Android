package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.utils.Utils;

public class JourneyLegActivity extends Activity {

	private JourneyLeg journeyLeg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final Context context = getApplicationContext();
		final int journeyLegId = intent.getIntExtra("journey_leg_id", 0);
		final int journeyId = intent.getIntExtra("journey_id", 0);

		if (journeyLegId != 0) {

			setContentView(R.layout.activity_journey_leg);
			journeyLeg = JourneyLeg.get(journeyLegId);

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
			journeyLeg = new JourneyLeg();

			/* Departure from origin */
			int originId = intent.getIntExtra("origin_id", 0);
			final Station origin = Station.get(originId);
			final TextView departureStation = (TextView) findViewById(R.id.departure_station);
			departureStation.setText(origin.toString());
			String originTime = intent.getStringExtra("origin_time");
			Utils.log("Origin time: " + originTime);
			final TextView departureTime = (TextView) findViewById(R.id.departure_time);
			departureTime.setText(originTime);
			int departureHour = Integer.valueOf(originTime.split(":")[0]);
			int departureMinute = Integer.valueOf(originTime.split(":")[1]);
			final TimePickerDialog departureTimePicker = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							journeyLeg.setDepartureTime(hourOfDay + ":"
									+ minute);
						}
					}, departureHour, departureMinute, true);
			departureTime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					departureTimePicker.show();
				}
			});
			String originPlatform = intent.getStringExtra("origin_platform");
			final EditText departurePlatform = (EditText) findViewById(R.id.departure_platform);
			departurePlatform.setText(originPlatform);

			/* Arrival into destination */
			int destinationId = intent.getIntExtra("destination_id", 0);
			final Station destination = Station.get(destinationId);
			final TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
			arrivalStation.setText(destination.toString());
			String destinationTime = intent.getStringExtra("destination_time");
			Utils.log("Destination time: " + destinationTime);
			final TextView arrivalTime = (TextView) findViewById(R.id.departure_time);
			arrivalTime.setText(destinationTime);
			int arrivalHour = Integer.valueOf(destinationTime.split(":")[0]);
			int arrivalMinute = Integer.valueOf(destinationTime.split(":")[1]);
			final TimePickerDialog arrivalTimePicker = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							journeyLeg.setArrivalTime(hourOfDay + ":" + minute);
						}
					}, arrivalHour, arrivalMinute, true);
			arrivalTime.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					arrivalTimePicker.show();
				}
			});
			String destinationPlatform = intent
					.getStringExtra("destination_platform");
			final EditText arrivalPlatform = (EditText) findViewById(R.id.arrival_platform);
			arrivalPlatform.setText(destinationPlatform);

			final Button saveButton = (Button) findViewById(R.id.save);
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Journey journey = new Journey();
					if (journeyId == 0) {
						journey = journey.save(context);
					} else {
						journey = Journey.get(context, journeyId);
					}

					journeyLeg.setJourneyId(journey.getId());

					journeyLeg.setOriginId(origin.getId());
					journeyLeg.setOrigin(origin);
					journeyLeg.setDeparturePlatform(departurePlatform.getText()
							.toString());

					journeyLeg.setDestinationId(destination.getId());
					journeyLeg.setDestination(destination);
					journeyLeg.setArrivalPlatform(arrivalPlatform.getText()
							.toString());

					journeyLeg = journeyLeg.save(context);

					if (journeyId == 0) {
						Intent intent = new Intent().setClass(
								getApplicationContext(), JourneyActivity.class);
						intent.putExtra("journey_id", journey.getId());
						startActivity(intent);
					}

					if (getParent() == null) {
						setResult(Activity.RESULT_OK);
					} else {
						getParent().setResult(Activity.RESULT_OK);
					}
					finish();

				}
			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.journey_leg, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_journey_leg:
			boolean success = journeyLeg.delete(getApplicationContext());
			if (success) {
				Toast.makeText(getApplicationContext(),
						"Journey leg was deleted", Toast.LENGTH_SHORT).show();
				Journey journey = Journey.get(getApplicationContext(),
						journeyLeg.getJourneyId());
				if (journey.getJourneyLegs().size() == 0) {
					journey.delete(getApplicationContext());
				}
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
