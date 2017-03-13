package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.adapter.CallingPointRowAdapter;
import uk.co.traintrackapp.traintrack.model.CallingPoint;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;
import uk.co.traintrackapp.traintrack.model.Service;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class ServiceActivity extends AppCompatActivity {

    private CallingPointRowAdapter adapter;
    private ProgressBar progress;
    private List<CallingPoint> callingPoints = new ArrayList<>();
    private TextView disruptionReason;
    private TextView toc;
    private TextView trainId;
    private TextView category;
    private TextView uid;
    private Service service;
    private Boolean viewExtraData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        service = (Service) intent.getExtras().getSerializable(Utils.ARGS_SERVICE);
        if (service == null) {
            finish();
            return;
        }

        new GetServiceRequest().execute(service.getServiceId());

        //TODO These are both currently empty but we need them for later
        final String journeyUuid = intent.getStringExtra(Utils.ARGS_JOURNEY_UUID);
        final String stationUuid = intent.getStringExtra(Utils.ARGS_STATION_UUID);

        progress = (ProgressBar) findViewById(R.id.progress);
        disruptionReason = (TextView) findViewById(R.id.disruption_reason);
        toc = (TextView) findViewById(R.id.toc);
        trainId = (TextView) findViewById(R.id.train_id);
        category = (TextView) findViewById(R.id.category);
        uid = (TextView) findViewById(R.id.uid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(service.getOrigin() + " to " + service.getDestination());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        viewExtraData = prefs.getBoolean("pref_view_extra_data", false);
        callingPoints.addAll(service.getCallingPoints());
        adapter = new CallingPointRowAdapter(callingPoints, stationUuid, this);

        updateUIComponents();

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {

                if (!view.isEnabled()) {
                    return;
                }

                CallingPoint callingPoint = adapter.getItem(index);
                // Find the calling point the journey leg is starting from
                CallingPoint here = new CallingPoint();
                for (CallingPoint cp : service.getCallingPoints()) {
                    if ((cp.getStation() != null) && (cp.getStation().getUuid().equals(stationUuid))) {
                        here = cp;
                    }
                }

                JourneyLeg journeyLeg = new JourneyLeg();
                journeyLeg.setOrigin(here.getStation());
                journeyLeg.setScheduledDeparture(here.getScheduledTimeDeparture());
                journeyLeg.setActualDeparture(here.getActualTimeDeparture());
                journeyLeg.setDeparturePlatform(here.getPlatform());
                journeyLeg.setDestination(callingPoint.getStation());
                journeyLeg.setScheduledArrival(callingPoint.getScheduledTimeArrival());
                journeyLeg.setActualArrival(callingPoint.getActualTimeArrival());
                journeyLeg.setArrivalPlatform(callingPoint.getPlatform());
                journeyLeg.setOperator(service.getOperator());

                Intent intent = new Intent().setClass(getApplicationContext(),
                        JourneyLegActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utils.ARGS_JOURNEY_LEG, journeyLeg);
                intent.putExtras(bundle);
                intent.putExtra(Utils.ARGS_JOURNEY_UUID, journeyUuid);
                startActivityForResult(intent, 1);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.service, menu);
        MenuItem item = menu.findItem(R.id.view_extended_data);
        item.setChecked(viewExtraData);
        if (item.isChecked()) {
            item.setTitle("Hide extended data");
        } else {
            item.setTitle("View extended data");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                progress.setVisibility(View.VISIBLE);
                new GetServiceRequest().execute(service.getServiceId());
                return true;
            case R.id.map:
                Intent intent = new Intent().setClass(this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Utils.ARGS_SERVICE, service);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.view_extended_data:
                if (item.isChecked()) {
                    viewExtraData = false;
                    item.setChecked(false);
                    item.setTitle("View extended data");
                } else {
                    viewExtraData = true;
                    item.setChecked(true);
                    item.setTitle("Hide extended data");
                }
                updateUIComponents();
                return true;
            case android.R.id.home:
                finish();
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

    private class GetServiceRequest extends AsyncTask<String, String, Service> {

        @Override
        protected Service doInBackground(String... service) {
            Utils.log("Getting service...");
            return Service.getByServiceId(service[0]);
        }

        @Override
        protected void onPostExecute(Service s) {
            super.onPostExecute(s);
            Utils.log("Got service.");
            service = s;
            callingPoints.clear();
            callingPoints.addAll(s.getCallingPoints());
            updateUIComponents();
        }
    }

    private void updateUIComponents() {
        adapter.notifyDataSetChanged();
        adapter.getFilter().filter(viewExtraData.toString());
        progress.setVisibility(View.GONE);
        if (viewExtraData) {
            //TODO Show the extra data
            trainId.setText(service.getTrainId());
            category.setText(service.getCategory());
            uid.setText(service.getUid());
        } else {
            //TODO Hide the extra data
        }
        if (service.getDisruptionReason() != null) {
            disruptionReason.setText(service.getDisruptionReason());
        }
        toc.setText(service.getOperator() + " - @" + service.getOperator().getTwitter());
        toc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=@" + service.getOperator().getTwitter();
                Intent tweet = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
                startActivity(tweet);
            }
        });
    }

}
