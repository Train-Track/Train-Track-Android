package uk.co.traintrackapp.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.adapter.ServiceItemRowAdapter;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationActivity extends ActionBarActivity {

    private Station station;
    private ServiceItemRowAdapter adapter;
    private ArrayList<ServiceItem> serviceItems;
    private ProgressBar progress;
    private TextView nrccMessage;
    static final int TAKE_PHOTO = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        final Intent intent = getIntent();
        final String journeyUuid = intent.getStringExtra("journey_uuid");
        final String stationUuid = intent.getStringExtra("station_uuid");
        TrainTrack app = (TrainTrack) getApplication();
        station = app.getStation(stationUuid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(station.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = (ProgressBar) findViewById(R.id.progress);
        nrccMessage = (TextView) findViewById(R.id.nrcc_messages);
        serviceItems = new ArrayList<>();
        adapter = new ServiceItemRowAdapter(serviceItems, station, this);

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                ServiceItem serviceItem = adapter.getItem(index);
                Intent intent = new Intent().setClass(getApplicationContext(),
                        ServiceActivity.class);
                intent.putExtra("journey_uuid", journeyUuid);
                intent.putExtra("service_id", serviceItem.getServiceId());
                intent.putExtra("origin_name", serviceItem.getOrigin().getName());
                intent.putExtra("station_uuid", station.getUuid());
                intent.putExtra("station_name", station.getName());
                intent.putExtra("destination_name", serviceItem.getDestination().getName());
                intent.putExtra("operator_code", serviceItem.getOperator().getCode());
                intent.putExtra("operator_name", serviceItem.getOperator().getName());
                intent.putExtra("platform", serviceItem.getPlatform());
                intent.putExtra("time", serviceItem.getTime());
                startActivityForResult(intent, 1);
            }

        });

        //TODO do some cool image stuff
        /*
        final ParseImageView image = (ParseImageView) findViewById(R.id.image);
        image.setPlaceholder(getResources().getDrawable(R.drawable.platform));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
            }
        });
        */

        //TODO some fancy image thing
        /*
        GetCallback<Image> imageCallback = new GetCallback<Image>() {
            @Override
            public void done(Image stationImage, ParseException e) {
                if ((e == null) && (stationImage != null)) {
                    image.setParseFile(stationImage.getFile());
                    image.loadInBackground(null);
                } else {
                    Utils.log("Station image problem for " + station);
                }
            }
        };
        savedImageCallback = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Utils.log("Image saved");
                } else {
                    Utils.log("Image save: " + e.getMessage());
                }
            }
        };
        savedFileCallback = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Utils.log("File saved");
                    newImage = new Image();
                    newImage.setTitle(station.getName());
                    newImage.setFile(imageFile);
                    newImage.setUser(ParseUser.getCurrentUser());
                    newImage.saveInBackground(savedImageCallback);
                } else {
                    Utils.log("File save: " + e.getMessage());
                }
            }
        };

        if (station.getImage() != null) {
            station.getImage().fetchInBackground(imageCallback);
        }
        */

        new GetBoardRequest().execute(station.getUuid());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station, menu);
        TrainTrack app = (TrainTrack) getApplication();
        if (app.getUser().getFavouriteStations().contains(station)) {
            menu.findItem(R.id.favourite).setIcon(
                    android.R.drawable.btn_star_big_on);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TrainTrack app = (TrainTrack) getApplication();
        switch (item.getItemId()) {
            case R.id.favourite:
                if (app.getUser().getFavouriteStations().contains(station)) {
                    item.setIcon(android.R.drawable.btn_star_big_off);
                    app.getUser().getFavouriteStations().remove(station);
                } else {
                    item.setIcon(android.R.drawable.btn_star_big_on);
                    app.getUser().getFavouriteStations().add(station);
                }
                app.getUser().save(this);
                return true;
            case R.id.refresh:
                new GetBoardRequest().execute(station.getUuid());
                return true;
            case R.id.map:
                Intent intent = new Intent().setClass(this, MapActivity.class);
                intent.putExtra("station_uuid", station.getUuid());
                startActivity(intent);
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
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //TODO do something with the bytes
        } else if (resultCode == Activity.RESULT_OK) {
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
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting station board...");
            return StationBoard.getByUuid(uuid[0]);
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got station board.");
            progress.setVisibility(View.GONE);
            serviceItems.clear();
            serviceItems.addAll(board.getTrainServices());
            adapter.notifyDataSetChanged();

            ArrayList<String> nrccMessages = board.getNrccMessages();
            if (nrccMessages.size() > 0) {
                nrccMessage.setVisibility(View.VISIBLE);
                nrccMessage.setText(nrccMessages.get(0));
            }

        }
    }

}
