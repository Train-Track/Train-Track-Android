package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.UUID;

import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class JourneyLegActivity extends AppCompatActivity {

    private JourneyLeg journeyLeg;
    private Journey journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TrainTrack app = (TrainTrack) getApplication();
        Intent intent = getIntent();
        final String journeyLegUuid = intent.getStringExtra(Utils.ARGS_JOURNEY_LEG_UUID);
        journey = app.getJourney(intent.getStringExtra(Utils.ARGS_JOURNEY_UUID));
        if (journeyLegUuid == null) {
            Utils.log("This is a new, unsaved journey leg");
            journeyLeg = (JourneyLeg) intent.getExtras().getSerializable(Utils.ARGS_JOURNEY_LEG);
            setContentView(R.layout.activity_journey_leg_form);
        } else {
            Utils.log("This is an existing saved journey leg: " + journeyLegUuid);
            journeyLeg = journey.getJourneyLeg(journeyLegUuid);
            setContentView(R.layout.activity_journey_leg);
        }

        if (journeyLeg == null) {
            finish();
            return;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(journeyLeg.toString());

        final TextView departureStation = (TextView) findViewById(R.id.departure_station);
        final TextView actualDepartureTime = (TextView) findViewById(R.id.departure_time);
        final TextView departurePlatform = (TextView) findViewById(R.id.departure_platform);
        final TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
        final TextView actualArrivalTime = (TextView) findViewById(R.id.arrival_time);
        final TextView arrivalPlatform = (TextView) findViewById(R.id.arrival_platform);

        DateTime atd = journeyLeg.getActualDeparture();
        if (atd == null) {
            atd = DateTime.now();
        }

        DateTime ata = journeyLeg.getActualArrival();
        if (ata == null) {
            ata = DateTime.now();
        }

        departureStation.setText(journeyLeg.getDepartureStation().toString());
        departurePlatform.setText(journeyLeg.getDeparturePlatform());
        actualDepartureTime.setText(atd.toString("HH:mm"));
        arrivalStation.setText(journeyLeg.getArrivalStation().toString());
        arrivalPlatform.setText(journeyLeg.getArrivalPlatform());
        actualArrivalTime.setText(ata.toString("HH:mm"));

        final TimePickerDialog actualDepartureTimePicker = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateTime atd = new DateTime().withTime(hourOfDay, minute, 0, 0);
                actualDepartureTime.setText(atd.toString("HH:mm"));
                journeyLeg.setActualDeparture(atd);
            }
        }, atd.getHourOfDay(), atd.getMinuteOfDay(), true);

        actualDepartureTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualDepartureTimePicker.show();
            }
        });

        final TimePickerDialog actualArrivalTimePicker = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateTime ata = new DateTime().withTime(hourOfDay, minute, 0, 0);
                actualArrivalTime.setText(ata.toString("HH:mm"));
                journeyLeg.setActualArrival(ata);
            }
        }, ata.getHourOfDay(), ata.getMinuteOfDay(), true);

        actualArrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualArrivalTimePicker.show();
            }
        });

        final Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journeyLeg.setDeparturePlatform(departurePlatform.getText().toString());
                journeyLeg.setArrivalPlatform(arrivalPlatform.getText().toString());
                if (journey == null) {
                    Utils.log("Creating new journey and adding new journey leg");
                    journey = new Journey();
                    app.getUser().getJourneys().add(journey);
                } else if (journeyLegUuid == null) {
                    Utils.log("Adding new journey leg to existing journey");
                    journeyLeg.setUuid(UUID.randomUUID().toString());
                    journeyLeg.setJourneyId(journey.getId());
                    journey.addJourneyLeg(journeyLeg);
                } else {
                    Utils.log("Updating existing journey leg");
                }
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
