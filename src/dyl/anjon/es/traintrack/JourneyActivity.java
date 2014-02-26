package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import dyl.anjon.es.traintrack.adapters.JourneyLegRowAdapter;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;

public class JourneyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey);

		final Intent intent = getIntent();
		int journeyId = intent.getIntExtra("journey_id", 0);

		if (journeyId != 0) {

			Journey journey = Journey.get(this, journeyId);
			final TextView name = (TextView) findViewById(R.id.name);
			name.setText(journey.getOrigin(this).toString() + " to "
					+ journey.getDestination(this).toString());

			final JourneyLegRowAdapter adapter = new JourneyLegRowAdapter(
					LayoutInflater.from(this), journey.getJourneyLegs(this));
			final ListView list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View view,
						int index, long x) {
					JourneyLeg journeyLeg = (JourneyLeg) adapter.getItem(index);
					Log.i("CLICK!", "Journey leg " + journeyLeg.getId());
					return;
				}

			});

		}
		// else this is a new journey - show new journey layout

	}

}
