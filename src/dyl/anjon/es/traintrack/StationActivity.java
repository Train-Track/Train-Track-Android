package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dyl.anjon.es.traintrack.adapters.ScheduleRowAdapter;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.Station;

public class StationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		final Intent intent = getIntent();
		final int stationId = intent.getIntExtra("station_id", 0);
		final Station station = Station.get(this, stationId);

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(station.getName());
		final TextView crsCode = (TextView) findViewById(R.id.crs_code);
		crsCode.setText(station.getCrsCode());

		final ScheduleRowAdapter adapter = new ScheduleRowAdapter(
				LayoutInflater.from(this), station.getSchedules(this), station);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Schedule schedule = (Schedule) adapter.getItem(index);
				Intent intent = new Intent().setClass(getApplicationContext(),
						ScheduleActivity.class);
				intent.putExtra("schedule_id", schedule.getId());
				intent.putExtra("station_id", station.getId());
				startActivityForResult(intent, 1);
				return;
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (getParent() == null) {
				setResult(Activity.RESULT_OK);
			} else {
				getParent().setResult(Activity.RESULT_OK);
			}
			finish();
		}
	}

}
