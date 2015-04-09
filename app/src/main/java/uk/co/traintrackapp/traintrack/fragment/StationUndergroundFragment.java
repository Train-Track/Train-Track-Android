package uk.co.traintrackapp.traintrack.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.adapter.ServiceItemUndergroundRowAdapter;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.api.TubeLine;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationUndergroundFragment extends Fragment {

    private static final String ARGS_STATION_UUID = "ARGS_STATION_UUID";
    private static final String PAGE_TITLE = "Underground";
    private Station station;
    private ServiceItemUndergroundRowAdapter adapter;
    private ArrayList<ServiceItem> serviceItems;
    private ProgressBar progress;
    private TextView nrccMessage;

    public static Fragment newInstance(String stationUuid) {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, PAGE_TITLE);
        args.putString(ARGS_STATION_UUID, stationUuid);
        StationUndergroundFragment fragment = new StationUndergroundFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stationUuid = getArguments().getString(ARGS_STATION_UUID);
        TrainTrack app = (TrainTrack) getActivity().getApplication();
        station = app.getStation(stationUuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_board, container, false);

        progress = (ProgressBar) v.findViewById(R.id.progress);
        nrccMessage = (TextView) v.findViewById(R.id.nrcc_messages);
        serviceItems = new ArrayList<>();

        RecyclerView list = (RecyclerView) v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new ServiceItemUndergroundRowAdapter(serviceItems);
        list.setAdapter(adapter);

        new GetUndergroundBoardRequest().execute(station.getUuid());

        return v;
    }

    class GetUndergroundBoardRequest extends AsyncTask<String, String, StationBoard> {

        @Override
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting underground board...");
            return StationBoard.getUnderground(uuid[0]);
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got underground board.");
            progress.setVisibility(View.GONE);
            serviceItems.clear();
            //TODO: put them in some form of vertical tabs
            for (TubeLine tubeLine : board.getTubeLines()) {
                serviceItems.addAll(tubeLine.getServices());
            }
            adapter.notifyDataSetChanged();

            ArrayList<String> nrccMessages = board.getNrccMessages();
            //TODO: Loop through all messages
            if (nrccMessages.size() > 0) {
                nrccMessage.setVisibility(View.VISIBLE);
                nrccMessage.setText(nrccMessages.get(0));
            }

        }
    }
}