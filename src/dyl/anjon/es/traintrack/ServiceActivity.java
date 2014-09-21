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
	private TextView disruptionReason;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);

		final Intent intent = getIntent();
		final int journeyId = intent.getIntExtra("journey_id", 0);
		final String serviceId = intent.getStringExtra("service_id");
		final String time = intent.getStringExtra("time");
		final int originId = intent.getIntExtra("origin_id", 0);
		final Location origin = Location.get(originId);
		final int locationId = intent.getIntExtra("location_id", 0);
		final Location location = Location.get(locationId);
		final int destinationId = intent.getIntExtra("destination_id", 0);
		final Location destination = Location.get(destinationId);
		final String operator = intent.getStringExtra("operator");

		new GetServiceRequest().execute(serviceId);

		callingPoints = new ArrayList<CallingPoint>();

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(time + " " + origin + " to " + destination);

		final TextView toc = (TextView) findViewById(R.id.toc);
		toc.setText(operator);

		disruptionReason = (TextView) findViewById(R.id.disruption_reason);

		adapter = new CallingPointRowAdapter(LayoutInflater.from(this),
				callingPoints, location);
		ListView list = (ListView) findViewById(R.id.list);
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
				intent.putExtra("origin_id", locationId);
				intent.putExtra("origin_time", "12:00");
				intent.putExtra("origin_platform", "9");
				intent.putExtra("destination_id", callingPoint.getLocation()
						.getId());
				intent.putExtra("destination_time", "19:32");
				intent.putExtra("destination_platform", "2A");
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
			callingPoints.clear();
			callingPoints.addAll(s.getPreviousCallingPoints());
			CallingPoint thisCallingPoint = new CallingPoint();
			thisCallingPoint.setLocation(s.getLocation());
			thisCallingPoint.setScheduledTime(s.getScheduledTimeDeparture());
			thisCallingPoint.setEstimatedTime(s.getEstimatedTimeDeparture());
			thisCallingPoint.setActualTime(s.getActualTimeDeparture());
			callingPoints.add(thisCallingPoint);
			callingPoints.addAll(s.getSubsequentCallingPoints());
			adapter.notifyDataSetChanged();
			
			if (s.getDisruptionReason() != null) {
				disruptionReason.setText(s.getDisruptionReason());
				disruptionReason.setVisibility(View.VISIBLE);
			}

		}
	}

}
