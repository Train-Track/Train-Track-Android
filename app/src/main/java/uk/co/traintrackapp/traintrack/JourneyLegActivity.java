package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;
import uk.co.traintrackapp.traintrack.model.Operator;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneyLegActivity extends ActionBarActivity {

    private JourneyLeg journeyLeg;
    private Journey journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TrainTrack app = (TrainTrack) getApplication();
        final Intent intent = getIntent();
        final String journeyUuid = intent.getStringExtra("journey_uuid");
        final String journeyLegUuid = intent.getStringExtra("journey_leg_uuid");

        if (journeyLegUuid != null) {

            setContentView(R.layout.activity_journey_leg);
            journey = app.getJourney(journeyUuid);
            journeyLeg = journey.getJourneyLeg(journeyLegUuid);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(journeyLeg.toString());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            final TextView departureStation = (TextView) findViewById(R.id.departure_station);
            final TextView departureTime = (TextView) findViewById(R.id.departure_time);
            final TextView departurePlatform = (TextView) findViewById(R.id.departure_platform);
            final TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
            final TextView arrivalTime = (TextView) findViewById(R.id.arrival_time);
            final TextView arrivalPlatform = (TextView) findViewById(R.id.arrival_platform);

            departureStation.setText(journeyLeg
                    .getDepartureStation().toString());
            departureTime.setText(journeyLeg
                    .getDepartureTimeAsString());
            departurePlatform.setText(journeyLeg
                    .getDeparturePlatform());
            arrivalStation.setText(journeyLeg.getArrivalStation()
                    .toString());
            arrivalTime.setText(journeyLeg.getArrivalTimeAsString());
            arrivalPlatform.setText(journeyLeg.getArrivalPlatform());

        } else {

            setContentView(R.layout.activity_journey_leg_form);
            journeyLeg = new JourneyLeg();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.action_new_journey));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            final TextView departureStationTv = (TextView) findViewById(R.id.departure_station);
            final String departureStationUuid = intent
                    .getStringExtra("departure_station_uuid");
            final Station departureStation = app.getStation(departureStationUuid);
            departureStationTv.setText(departureStation.getName());

            final TextView departurePlatformTv = (TextView) findViewById(R.id.departure_platform);
            String departurePlatform = intent
                    .getStringExtra("departure_platform");
            departurePlatformTv.setText(departurePlatform);

            final TextView departureTimeTv = (TextView) findViewById(R.id.departure_time);
            String departureTime = intent.getStringExtra("departure_time");
            departureTimeTv.setText(departureTime);
            journeyLeg.setScheduledDeparture(Utils.getDateWithTime(departureTime));
            journeyLeg.setActualDeparture(Utils.getDateWithTime(departureTime));

            int departureHour = Integer.valueOf(departureTime.split(":")[0]);
            int departureMinute = Integer.valueOf(departureTime.split(":")[1]);

            final TimePickerDialog departureTimePicker = new TimePickerDialog(
                    this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    departureTimeTv.setText(Utils.zeroPadTime(
                            hourOfDay, minute));
                    journeyLeg.setScheduledDeparture(Utils.getDateWithTime(
                            hourOfDay, minute));
                }
            }, departureHour, departureMinute, true);

            departureTimeTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    departureTimePicker.show();
                }
            });

            final TextView arrivalStationTv = (TextView) findViewById(R.id.arrival_station);
            final String arrivalStationUuid = intent
                    .getStringExtra("arrival_station_uuid");
            final Station arrivalStation = app.getStation(arrivalStationUuid);
            arrivalStationTv.setText(arrivalStation.getName());

            final TextView arrivalPlatformTv = (TextView) findViewById(R.id.arrival_platform);
            String arrivalPlatform = intent.getStringExtra("arrival_platform");
            arrivalPlatformTv.setText(arrivalPlatform);

            final TextView arrivalTimeTv = (TextView) findViewById(R.id.arrival_time);
            String arrivalTime = intent.getStringExtra("arrival_time");
            arrivalTimeTv.setText(arrivalTime);
            journeyLeg.setScheduledArrival(Utils.getDateWithTime(arrivalTime));
            journeyLeg.setActualArrival(Utils.getDateWithTime(arrivalTime));

            int arrivalHour = Integer.valueOf(arrivalTime.split(":")[0]);
            int arrivalMinute = Integer.valueOf(arrivalTime.split(":")[1]);

            final TimePickerDialog arrivalTimePicker = new TimePickerDialog(
                    this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    arrivalTimeTv.setText(Utils.zeroPadTime(hourOfDay,
                            minute));
                    journeyLeg.setScheduledArrival(Utils.getDateWithTime(
                            hourOfDay, minute));
                }
            }, arrivalHour, arrivalMinute, true);

            arrivalTimeTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrivalTimePicker.show();
                }
            });

            final Button saveButton = (Button) findViewById(R.id.save);
            saveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    journeyLeg.setDepartureStation(departureStation);
                    journeyLeg.setDeparturePlatform(departurePlatformTv
                            .getText().toString());
                    journeyLeg.setArrivalStation(arrivalStation);
                    journeyLeg.setArrivalPlatform(arrivalPlatformTv.getText()
                            .toString());
                    journeyLeg.setOperator(new Operator());
                    if (journeyUuid == null) {
                        Utils.log("Creating new Journey");
                        journey = new Journey();
                        app.getUser().getJourneys().add(journey);
                    } else {
                        journey = app.getJourney(journeyUuid);
                    }
                    journey.addJourneyLeg(journeyLeg);
                    app.getUser().save(getApplicationContext());

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
        if (journeyLeg != null) {
            getMenuInflater().inflate(R.menu.journey_leg, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final TrainTrack app = (TrainTrack) getApplication();
        switch (item.getItemId()) {
            case R.id.delete_journey_leg:
                //remove journey leg from journey and from app
                journey.removeJourneyLeg(journeyLeg);
                Toast.makeText(getApplicationContext(), "Journey leg was deleted",
                        Toast.LENGTH_SHORT).show();
                //remove journey if no legs left
                if (journey.getJourneyLegs().size() == 0) {
                    app.getUser().getJourneys().remove(journey);
                }
                app.getUser().save(getApplicationContext());
                finish();
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
