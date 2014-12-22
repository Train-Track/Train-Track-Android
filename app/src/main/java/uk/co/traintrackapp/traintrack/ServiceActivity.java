package uk.co.traintrackapp.traintrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.adapter.CallingPointRowAdapter;
import uk.co.traintrackapp.traintrack.api.CallingPoint;
import uk.co.traintrackapp.traintrack.api.Service;
import uk.co.traintrackapp.traintrack.utils.Utils;


public class ServiceActivity extends ActionBarActivity {

    private CallingPointRowAdapter adapter;
    private ProgressBar progress;
    private ArrayList<CallingPoint> callingPoints;
    private TextView disruptionReason;
    private TextView generatedAt;
    private String serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        final Intent intent = getIntent();
        serviceId = intent.getStringExtra("service_id");
        new GetServiceRequest().execute(serviceId);

        final String journeyId = intent.getStringExtra("journey_id");
        final String originName = intent.getStringExtra("origin_name");
        final String stationId = intent.getStringExtra("station_id");
        final String stationCrs = intent.getStringExtra("station_crs");
        final String stationName = intent.getStringExtra("station_name");
        final String destinationName = intent
                .getStringExtra("destination_name");
        final String operatorCode = intent.getStringExtra("operator_code");
        final String operatorName = intent.getStringExtra("operator_name");

        callingPoints = new ArrayList<CallingPoint>();

        final TextView name = (TextView) findViewById(R.id.name);
        name.setText(originName + " to " + destinationName);
        final TextView toc = (TextView) findViewById(R.id.toc);
        toc.setText(operatorName);

        progress = (ProgressBar) findViewById(R.id.progress);
        disruptionReason = (TextView) findViewById(R.id.disruption_reason);
        generatedAt = (TextView) findViewById(R.id.generated_at);

        adapter = new CallingPointRowAdapter(callingPoints, stationCrs, this);
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
                intent.putExtra("departure_station_id", stationId);
                intent.putExtra("departure_station_crs", stationCrs);
                intent.putExtra("departure_station_name", stationName);
                intent.putExtra("departure_time", "12:00");
                intent.putExtra("departure_platform", "9");
                intent.putExtra("arrival_station_crs",
                        callingPoint.getStationCrs());
                intent.putExtra("arrival_time", "19:32");
                intent.putExtra("arrival_platform", "3A");
                intent.putExtra("operator_code", operatorCode);
                intent.putExtra("operator_name", operatorName);
                startActivityForResult(intent, 1);
                return;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.service, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new GetServiceRequest().execute(serviceId);
                return true;
            case R.id.map:
                Intent intent = new Intent().setClass(this, MapActivity.class);
                intent.putExtra("service_id", serviceId);
                startActivity(intent);
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

    class GetServiceRequest extends AsyncTask<String, String, Service> {

        @Override
        protected Service doInBackground(String... service) {
            Utils.log("Getting service...");
            return Service.getByServiceId(service[0]);
        }

        @Override
        protected void onPostExecute(Service s) {
            super.onPostExecute(s);
            Utils.log("Got service.");
            progress.setVisibility(View.GONE);
            callingPoints.clear();
            callingPoints.addAll(s.getCallingPoints());
            adapter.notifyDataSetChanged();

            if (s.getDisruptionReason() != null) {
                disruptionReason.setText(s.getDisruptionReason());
                disruptionReason.setVisibility(View.VISIBLE);
            }

            generatedAt.setText(s.getGeneratedAtString());

        }
    }

}