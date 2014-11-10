package dyl.anjon.es.traintrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import dyl.anjon.es.traintrack.adapters.JourneyLegRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.utils.Utils;

public class JourneyActivity extends Activity {

	private String journeyId = "";
	private Journey journey;
	private JourneyLegRowAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey);

		final Intent intent = getIntent();
		journeyId = intent.getStringExtra("journey_id");

		final TextView name = (TextView) findViewById(R.id.name);
		final ArrayList<JourneyLeg> journeyLegs = new ArrayList<JourneyLeg>();
		adapter = new JourneyLegRowAdapter(LayoutInflater.from(this),
				journeyLegs);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				JourneyLeg journeyLeg = (JourneyLeg) adapter.getItem(index);
				Intent intent = new Intent().setClass(getApplicationContext(),
						JourneyLegActivity.class);
				intent.putExtra("journey_leg_id", journeyLeg.getObjectId());
				intent.putExtra("journey_id", journeyId);
				startActivity(intent);
				return;
			}

		});

		ParseQuery<Journey> query = ParseQuery.getQuery(Journey.class);
		query.fromLocalDatastore();
		query.getInBackground(journeyId, new GetCallback<Journey>() {
			@Override
			public void done(Journey result, ParseException e) {
				if (e != null) {
					Utils.log(e.getMessage());
				} else {
					journey = result;
					name.setText(journey.getOrigin().toString() + " to "
							+ journey.getDestination().toString());
					journeyLegs.addAll(journey.getJourneyLegs());
					adapter.notifyDataSetChanged();
				}

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
			Toast.makeText(getApplicationContext(), "Add journey leg",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent().setClass(getApplicationContext(),
					StationActivity.class);
			intent.putExtra("station_id", journey.getDestination()
					.getObjectId());
			intent.putExtra("journey_id", journeyId);
			startActivity(intent);
			finish();
			return true;
		case R.id.delete_journey:
			journey.deleteEventually();
			Toast.makeText(getApplicationContext(), "Journey was deleted",
					Toast.LENGTH_SHORT).show();
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		super.onResume();
		/*
		ArrayList<JourneyLeg> journeyLegs = journey.getJourneyLegs();
		if (journeyLegs.size() == 0) {
			finish();
		}
		adapter.refresh(journeyLegs);
		*/
	}

}
