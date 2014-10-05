package dyl.anjon.es.traintrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dyl.anjon.es.traintrack.adapters.ServiceItemRowAdapter;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.api.StationBoard;
import dyl.anjon.es.traintrack.models.Station;

public class StationActivity extends Activity {

	private Station station;
	private ServiceItemRowAdapter adapter;
	private ArrayList<ServiceItem> serviceItems;
	private TextView nrccMessage;
	private TextView generatedAt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		final Intent intent = getIntent();
		final int stationId = intent.getIntExtra("station_id", 0);
		station = Station.get(stationId);
		final int journeyId = intent.getIntExtra("journey_id", 0);

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(station.getName());
		final TextView crsCode = (TextView) findViewById(R.id.crs_code);
		crsCode.setText(station.getCrsCode());

		generatedAt = (TextView) findViewById(R.id.generated_at);
		nrccMessage = (TextView) findViewById(R.id.nrcc_messages);
		serviceItems = new ArrayList<ServiceItem>();
		adapter = new ServiceItemRowAdapter(LayoutInflater.from(this),
				serviceItems, station);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				ServiceItem serviceItem = (ServiceItem) adapter.getItem(index);
				Intent intent = new Intent().setClass(getApplicationContext(),
						ServiceActivity.class);
				intent.putExtra("journey_id", journeyId);
				intent.putExtra("service_id", serviceItem.getServiceId());
				intent.putExtra("time", serviceItem.getScheduledTimeDeparture());
				intent.putExtra("origin_id", serviceItem.getOrigin().getId());
				intent.putExtra("station_id", station.getId());
				intent.putExtra("destination_id", serviceItem.getDestination()
						.getId());
				intent.putExtra("operator", serviceItem.getOperator()
						.toString());
				startActivityForResult(intent, 1);
				return;
			}

		});

		new GetBoardRequest().execute(station.getCrsCode());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station, menu);
		if (station.isFavourite()) {
			menu.findItem(R.id.favourite).setIcon(android.R.drawable.btn_star_big_on);
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
			station.save();
			return true;
		case R.id.refresh:
			new GetBoardRequest().execute(station.getCrsCode());
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

	class GetBoardRequest extends AsyncTask<String, String, StationBoard> {

		@Override
		protected StationBoard doInBackground(String... crs) {
			return StationBoard.getByCrs(crs[0]);
		}

		@Override
		protected void onPostExecute(StationBoard board) {
			super.onPostExecute(board);
			serviceItems.clear();
			serviceItems.addAll(board.getTrainServices());
			adapter.notifyDataSetChanged();

			ArrayList<String> nrccMessages = board.getNrccMessages();
			if (nrccMessages.size() > 0) {
				nrccMessage.setVisibility(View.VISIBLE);
				nrccMessage.setText(nrccMessages.get(0).toString());
			}

			generatedAt.setText(board.getGeneratedAtString());

		}
	}

}
