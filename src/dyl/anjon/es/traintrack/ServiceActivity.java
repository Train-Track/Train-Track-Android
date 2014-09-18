package dyl.anjon.es.traintrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dyl.anjon.es.traintrack.adapters.CallingPointRowAdapter;
import dyl.anjon.es.traintrack.api.CallingPoint;
import dyl.anjon.es.traintrack.api.Service;
import dyl.anjon.es.traintrack.models.Location;

public class ServiceActivity extends Activity {

	private CallingPointRowAdapter adapter;
	private ArrayList<CallingPoint> callingPoints;
	private Service service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		final Intent intent = getIntent();
		final int journeyId = intent.getIntExtra("journey_id", 0);
		final String serviceId = intent.getStringExtra("service_id");

		// get service out of db
		// service = Service.get(serviceId);
		// in the mean time
		final String time = intent.getStringExtra("time");
		final String origin = intent.getStringExtra("origin");
		final String destination = intent.getStringExtra("destination");
		final String operator = intent.getStringExtra("operator");
		// ////

		new GetServiceRequest().execute(serviceId);

		final int locationId = intent.getIntExtra("location_id", 0);
		Location location = Location.get(locationId);

		callingPoints = new ArrayList<CallingPoint>();

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(time + " " + origin + " to " + destination);

		final TextView toc = (TextView) findViewById(R.id.toc);
		toc.setText(operator);

		adapter = new CallingPointRowAdapter(LayoutInflater.from(this),
				callingPoints, location);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {

				if (!view.isEnabled()) {
					return;
				}

				CallingPoint callingPoint = (CallingPoint) adapter
						.getItem(index);

				Intent intent = new Intent().setClass(getApplicationContext(),
						JourneyLegActivity.class);
				intent.putExtra("journey_id", journeyId);
				intent.putExtra("service_id", serviceId);
				intent.putExtra("origin_location_id", locationId);
				intent.putExtra("destination_location_id", callingPoint
						.getLocation().getId());
				startActivityForResult(intent, 1);
				return;
			}

		});

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

	class GetServiceRequest extends AsyncTask<String, String, Service> {

		@Override
		protected Service doInBackground(String... service) {
			return Service.getByServiceId(service[0]);
		}

		@Override
		protected void onPostExecute(Service s) {
			super.onPostExecute(s);
			service = s;
			callingPoints.clear();
			callingPoints.addAll(s.getPreviousCallingPoints());
			callingPoints.addAll(s.getSubsequentCallingPoints());
			adapter.notifyDataSetChanged();
		}
	}

}
