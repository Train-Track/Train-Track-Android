package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
        final String journeyLegId = intent.getStringExtra("journey_leg_id");
        final String journeyId = intent.getStringExtra("journey_id");

        if (journeyLegId != null) {

            setContentView(R.layout.activity_journey_leg);
            final TextView departureStation = (TextView) findViewById(R.id.departure_station);
            final TextView departureTime = (TextView) findViewById(R.id.departure_time);
            final TextView departurePlatform = (TextView) findViewById(R.id.departure_platform);
            final TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
            final TextView arrivalTime = (TextView) findViewById(R.id.arrival_time);
            final TextView arrivalPlatform = (TextView) findViewById(R.id.arrival_platform);

            //TODO get journey leg
            /*
                journeyLeg = result;
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
                getSupportActionBar().setTitle(journeyLeg.toString());
             */

        } else {

            setContentView(R.layout.activity_journey_leg_form);
            journeyLeg = new JourneyLeg();
            getSupportActionBar().setTitle(getString(R.string.action_new_journey));

            final TextView departureStationTv = (TextView) findViewById(R.id.departure_station);
             final String departureStationCrs = intent
             .getStringExtra("departure_station_crs");
            final String departureStationName = intent
                    .getStringExtra("departure_station_name");
            final Station departureStation = app.getStation(departureStationCrs);
            departureStationTv.setText(departureStationName);

            final TextView departurePlatformTv = (TextView) findViewById(R.id.departure_platform);
            String departurePlatform = intent
                    .getStringExtra("departure_platform");
            departurePlatformTv.setText(departurePlatform);

            final TextView departureTimeTv = (TextView) findViewById(R.id.departure_time);
            String departureTime = intent.getStringExtra("departure_time");
            departureTimeTv.setText(departureTime);
            journeyLeg.setDepartureTime(Utils.getDateWithTime(departureTime));

            int departureHour = Integer.valueOf(departureTime.split(":")[0]);
            int departureMinute = Integer.valueOf(departureTime.split(":")[1]);

            final TimePickerDialog departureTimePicker = new TimePickerDialog(
                    this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    departureTimeTv.setText(Utils.zeroPadTime(
                            hourOfDay, minute));
                    journeyLeg.setDepartureTime(Utils.getDateWithTime(
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
             final String arrivalStationCrs = intent
             .getStringExtra("arrival_station_crs");
            final String arrivalStationName = intent
                    .getStringExtra("arrival_station_name");
            final Station arrivalStation = app.getStation(arrivalStationCrs);
            arrivalStationTv.setText(arrivalStationName);

            final TextView arrivalPlatformTv = (TextView) findViewById(R.id.arrival_platform);
            String arrivalPlatform = intent.getStringExtra("arrival_platform");
            arrivalPlatformTv.setText(arrivalPlatform);

            final TextView arrivalTimeTv = (TextView) findViewById(R.id.arrival_time);
            String arrivalTime = intent.getStringExtra("arrival_time");
            arrivalTimeTv.setText(arrivalTime);
            journeyLeg.setArrivalTime(Utils.getDateWithTime(arrivalTime));

            int arrivalHour = Integer.valueOf(arrivalTime.split(":")[0]);
            int arrivalMinute = Integer.valueOf(arrivalTime.split(":")[1]);

            final TimePickerDialog arrivalTimePicker = new TimePickerDialog(
                    this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    arrivalTimeTv.setText(Utils.zeroPadTime(hourOfDay,
                            minute));
                    journeyLeg.setArrivalTime(Utils.getDateWithTime(
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
                    if (journeyId == null) {
                        Utils.log("Creating new Journey");
                        journey = new Journey();
                        //journey.setUser(app.getUser());
                    } else {
                        journey = app.getJourney(journeyId);
                    }
                    journey.addJourneyLeg(journeyLeg);
                    //journey.saveEventually();

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
        switch (item.getItemId()) {
            case R.id.delete_journey_leg:
                //journeyLeg.deleteEventually();
                Toast.makeText(getApplicationContext(), "Journey leg was deleted",
                        Toast.LENGTH_SHORT).show();
                if (journey.getJourneyLegs().size() == 0) {
                    //journey.deleteEventually();
                }
                finish();
        }
        return true;
    }
}