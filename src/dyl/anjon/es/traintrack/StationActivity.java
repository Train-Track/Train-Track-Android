package dyl.anjon.es.traintrack;

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
import dyl.anjon.es.traintrack.adapters.ScheduleRowAdapter;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.Station;

public class StationActivity extends Activity {

	private Station station;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		final Intent intent = getIntent();
		final int stationId = intent.getIntExtra("station_id", 0);
		station = Station.get(this, stationId);
		final int journeyId = intent.getIntExtra("journey_id", 0);

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
				intent.putExtra("journey_id", journeyId);
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
		if (station.isFavourite()) {
			menu.getItem(0).setIcon(android.R.drawable.btn_star_big_on);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.favourite:
			if (!station.isFavourite()) {
				station.setFavourite(true);
				item.setIcon(android.R.drawable.btn_star_big_on);
			} else {
				item.setIcon(android.R.drawable.btn_star_big_off);
				station.setFavourite(false);
			}
			station.save(getApplicationContext());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
