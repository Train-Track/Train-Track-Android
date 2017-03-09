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
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
import uk.co.traintrackapp.traintrack.adapter.ServiceDepartureRowAdapter;
import uk.co.traintrackapp.traintrack.api.Service;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationDeparturesFragment extends Fragment {

    private static final String PAGE_TITLE = "Departures";
    private ServiceDepartureRowAdapter adapter;
    private PullRefreshLayout refresh;
    private ArrayList<Service> services;
    private TextView nrccMessage;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, PAGE_TITLE);
        StationDeparturesFragment fragment = new StationDeparturesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_board, container, false);
        StationActivity activity = ((StationActivity) getActivity());
        final Station station = activity.getStation();
        StationBoard board = activity.getDeparturesBoard();

        services = new ArrayList<>();
        nrccMessage = (TextView) v.findViewById(R.id.nrcc_messages);

        RecyclerView list = (RecyclerView) v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new ServiceDepartureRowAdapter(services);
        list.setAdapter(adapter);

        refresh = (PullRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDepartureBoardRequest().execute(station.getUuid());
            }
        });

        if (board == null) {
            refresh.setRefreshing(true);
            new GetDepartureBoardRequest().execute(station.getUuid());
        } else {
            updateBoard(board);
        }

        return v;
    }

    private void updateBoard(StationBoard board) {
        refresh.setRefreshing(false);
        services.clear();
        services.addAll(board.getTrainServices());
        adapter.notifyDataSetChanged();

        ArrayList<String> nrccMessages = board.getNrccMessages();
        //TODO: Loop through all messages
        if (nrccMessages.size() > 0) {
            nrccMessage.setVisibility(View.VISIBLE);
            nrccMessage.setText(nrccMessages.get(0));
        }
    }

    class GetDepartureBoardRequest extends AsyncTask<String, String, StationBoard> {

        @Override
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting departures board...");
            return StationBoard.getDepartures(uuid[0]);
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got departures board.");
            StationActivity activity = ((StationActivity) getActivity());
            if (activity != null) {
                activity.setDeparturesBoard(board);
            }
            updateBoard(board);
        }
    }

}
