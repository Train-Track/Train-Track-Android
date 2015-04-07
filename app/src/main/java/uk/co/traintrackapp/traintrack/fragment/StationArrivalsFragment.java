package uk.co.traintrackapp.traintrack.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.ServiceActivity;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.ServiceItemRowAdapter;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationArrivalsFragment extends Fragment {

    private static final String ARGS_STATION_UUID = "ARGS_STATION_UUID";
    private static final String ARGS_JOURNEY_UUID = "ARGS_JOURNEY_UUID";
    private static final String PAGE_TITLE = "Arrivals";
    private Station station;
    private String journeyUuid;
    private ServiceItemRowAdapter adapter;
    private ArrayList<ServiceItem> serviceItems;
    private ProgressBar progress;
    private TextView nrccMessage;

    public static Fragment newInstance(String stationUuid, String journeyUuid) {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, PAGE_TITLE);
        args.putString(ARGS_STATION_UUID, stationUuid);
        args.putString(ARGS_JOURNEY_UUID, journeyUuid);
        StationArrivalsFragment fragment = new StationArrivalsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stationUuid = getArguments().getString(ARGS_STATION_UUID);
        journeyUuid = getArguments().getString(ARGS_JOURNEY_UUID);
        TrainTrack app = (TrainTrack) getActivity().getApplication();
        station = app.getStation(stationUuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_board, container, false);

        progress = (ProgressBar) v.findViewById(R.id.progress);
        nrccMessage = (TextView) v.findViewById(R.id.nrcc_messages);
        serviceItems = new ArrayList<>();
        adapter = new ServiceItemRowAdapter(serviceItems, station, getActivity());

        final ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                ServiceItem serviceItem = adapter.getItem(index);
                Intent intent = new Intent().setClass(getActivity(),
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

        new GetArrivalsBoardRequest().execute(station.getUuid());

        return v;
    }

    class GetArrivalsBoardRequest extends AsyncTask<String, String, StationBoard> {

        @Override
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting station board...");
            return StationBoard.getArrivals(uuid[0]);
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got station board.");
            progress.setVisibility(View.GONE);
            serviceItems.clear();
            if (!station.isUnderground()) {
                serviceItems.addAll(board.getTrainServices());
            } else {
                Utils.log(board.getTubeLines().toString());
                serviceItems.addAll(board.getTubeLines().get(0).getServices());
            }
            adapter.notifyDataSetChanged();

            ArrayList<String> nrccMessages = board.getNrccMessages();
            if (nrccMessages.size() > 0) {
                nrccMessage.setVisibility(View.VISIBLE);
                nrccMessage.setText(nrccMessages.get(0));
            }

        }
    }
}
