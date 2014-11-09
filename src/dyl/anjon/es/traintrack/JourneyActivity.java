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
import dyl.anjon.es.traintrack.adapters.JourneyLegRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;

public class JourneyActivity extends Activity {

	private int journeyId = 0;
	private Journey journey;
	private JourneyLegRowAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey);

		final Intent intent = getIntent();
		journeyId = intent.getIntExtra("journey_id", 0);

		journey = Journey.get(this, journeyId);
		if (journey == null) {
			finish();
		}

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(journey.getOrigin().toString() + " to "
				+ journey.getDestination().toString());

		adapter = new JourneyLegRowAdapter(LayoutInflater.from(this),
				journey.getJourneyLegs());
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				JourneyLeg journeyLeg = (JourneyLeg) adapter.getItem(index);
				Intent intent = new Intent().setClass(getApplicationContext(),
						JourneyLegActivity.class);
				intent.putExtra("journey_leg_id", journeyLeg.getId());
				intent.putExtra("journey_id", journeyId);
				startActivity(intent);
				return;
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
			intent.putExtra("station_id", journey.getDestination().getObjectId());
			intent.putExtra("journey_id", journeyId);
			startActivity(intent);
			finish();
			return true;
		case R.id.delete_journey:
			boolean success = journey.delete(getApplicationContext());
			if (success) {
				Toast.makeText(getApplicationContext(), "Journey was deleted",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		super.onResume();
		ArrayList<JourneyLeg> journeyLegs = journey.getJourneyLegs();
		if (journeyLegs.size() == 0) {
			finish();
		}
		adapter.refresh(journeyLegs);
	}

}
