package uk.co.traintrackapp.traintrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.traintrackapp.traintrack.adapter.JourneyLegRowAdapter;
import uk.co.traintrackapp.traintrack.model.Journey;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;

public class JourneyActivity extends ActionBarActivity {

    private Journey journey;
    private JourneyLegRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        TrainTrack app = (TrainTrack) getApplication();

        final Intent intent = getIntent();
        final String journeyUuid = intent.getStringExtra("journey_uuid");
        journey = app.getJourney(journeyUuid);

        final TextView name = (TextView) findViewById(R.id.name);
        name.setText(journey.toString());
        final TextView date = (TextView) findViewById(R.id.date);
        //TODO change to journey date
        date.setText(journey.getOrigin().getCrsCode());
        adapter = new JourneyLegRowAdapter(LayoutInflater.from(this),
                journey.getJourneyLegs());
        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                JourneyLeg journeyLeg = adapter.getItem(index);
                Intent intent = new Intent().setClass(getApplicationContext(),
                        JourneyLegActivity.class);
                intent.putExtra("journey_leg_uuid", journeyLeg.getUuid());
                intent.putExtra("journey_uuid", journeyUuid);
                startActivity(intent);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_journey_leg:
                Toast.makeText(getApplicationContext(), "Add Journey Leg",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent().setClass(getApplicationContext(),
                        StationActivity.class);
                intent.putExtra("station_uuid", journey.getDestination()
                        .getUuid());
                intent.putExtra("journey_uuid", journey.getUuid());
                startActivity(intent);
                finish();
                return true;
            case R.id.delete_journey:
                //TODO Delete this journey
                Toast.makeText(getApplicationContext(), "Journey was deleted",
                        Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
