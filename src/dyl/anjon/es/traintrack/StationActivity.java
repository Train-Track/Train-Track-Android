package dyl.anjon.es.traintrack;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import dyl.anjon.es.traintrack.adapters.ServiceItemRowAdapter;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.api.StationBoard;
import dyl.anjon.es.traintrack.models.Image;
import dyl.anjon.es.traintrack.models.Station;
import dyl.anjon.es.traintrack.utils.Utils;

public class StationActivity extends Activity {

	private String stationId;
	private Station station;
	private ServiceItemRowAdapter adapter;
	private ArrayList<ServiceItem> serviceItems;
	private ProgressBar progress;
	private TextView nrccMessage;
	private TextView generatedAt;
	private SaveCallback savedFileCallback;
	private SaveCallback savedImageCallback;
	private ParseFile imageFile;
	private Image newImage;
	static final int TAKE_PHOTO = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station);

		final Intent intent = getIntent();
		stationId = intent.getStringExtra("station_id");
		final String stationName = intent.getStringExtra("station_name");
		final String stationCrs = intent.getStringExtra("station_crs");
		final String journeyId = intent.getStringExtra("journey_id");
		final TextView crsCode = (TextView) findViewById(R.id.crs_code);
		crsCode.setText(stationCrs);
		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(stationName);
		progress = (ProgressBar) findViewById(R.id.progress);
		generatedAt = (TextView) findViewById(R.id.generated_at);
		nrccMessage = (TextView) findViewById(R.id.nrcc_messages);
		serviceItems = new ArrayList<ServiceItem>();
		adapter = new ServiceItemRowAdapter(serviceItems, station, this);

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
				intent.putExtra("origin_name", serviceItem.getOriginName());
				intent.putExtra("station_id", stationId);
				intent.putExtra("station_crs", stationCrs);
				intent.putExtra("station_name", stationName);
				intent.putExtra("destination_name", serviceItem.getDestinationName());
				intent.putExtra("operator_code", serviceItem.getOperatorCode());
				intent.putExtra("operator_name", serviceItem.getOperatorName());
				startActivityForResult(intent, 1);
				return;
			}

		});

		final ParseImageView image = (ParseImageView) findViewById(R.id.image);
		image.setPlaceholder(getResources().getDrawable(R.drawable.platform));
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, TAKE_PHOTO);
				}
			}
		});

		final GetCallback<Image> imageCallback = new GetCallback<Image>() {
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

		ParseQuery<Station> query = ParseQuery.getQuery(Station.class);
		query.getInBackground(stationId, new GetCallback<Station>() {
			@Override
			public void done(Station result, ParseException e) {
				if (e == null) {
					station = result;
					name.setText(station.getName());
					crsCode.setText(station.getCrsCode());
					if (station.getImage() != null) {
						station.getImage().fetchInBackground(imageCallback);
					} else {
						Utils.log("No image for " + station);
					}
					result.pinInBackground();
					new GetBoardRequest().execute(station.getCrsCode());
				} else {
					Utils.log("Getting station: " + e.getMessage());
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.station, menu);
		if ((station != null) && (station.isFavourite())) {
			menu.findItem(R.id.favourite).setIcon(
					android.R.drawable.btn_star_big_on);
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
			// station.save();
			return true;
		case R.id.refresh:
			new GetBoardRequest().execute(station.getCrsCode());
			return true;
		case R.id.map:
			Intent intent = new Intent().setClass(this, MapActivity.class);
			intent.putExtra("station_id", stationId);
			startActivity(intent);
			return true;
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
			imageFile = new ParseFile(station.getCrsCode() + ".png", byteArray);
			imageFile.saveInBackground(savedFileCallback);
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
		protected StationBoard doInBackground(String... crs) {
			Utils.log("Getting station board...");
			return StationBoard.getByCrs(crs[0]);
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
				nrccMessage.setText(nrccMessages.get(0).toString());
			}

			generatedAt.setText(board.getGeneratedAtString());

		}
	}

}
