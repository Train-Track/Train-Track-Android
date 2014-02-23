package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dyl.anjon.es.traintrack.adapters.ScheduleRowAdapter;
import dyl.anjon.es.traintrack.db.StationDatabaseHandler;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.Station;

public class StationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		Intent intent = getIntent();
		int id = intent.getIntExtra("id", 0);
		StationDatabaseHandler db = new StationDatabaseHandler(this);
		Station station = db.getStation(id);

		TextView name = (TextView) findViewById(R.id.name);
		name.setText(station.getName());
		TextView crsCode = (TextView) findViewById(R.id.crs_code);
		crsCode.setText(station.getCrsCode());

		ListView list = (ListView) findViewById(R.id.list);
		final ScheduleRowAdapter adapter = new ScheduleRowAdapter(
				LayoutInflater.from(this), station.getSchedules());

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				Schedule schedule = (Schedule) adapter.getItem(index);
				Log.i("CLICK!", schedule.toString());
				return;
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station, menu);
		return true;
	}

}
