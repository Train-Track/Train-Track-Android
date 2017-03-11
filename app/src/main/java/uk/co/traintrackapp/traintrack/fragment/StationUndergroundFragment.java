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
import uk.co.traintrackapp.traintrack.adapter.ServiceUndergroundRowAdapter;
import uk.co.traintrackapp.traintrack.model.Service;
import uk.co.traintrackapp.traintrack.model.StationBoard;
import uk.co.traintrackapp.traintrack.model.TubeLine;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationUndergroundFragment extends Fragment {

    private static final String PAGE_TITLE = "Underground";
    private ServiceUndergroundRowAdapter adapter;
    private PullRefreshLayout refresh;
    private ArrayList<Service> services;
    private TextView nrccMessage;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        args.putString(Utils.ARGS_PAGE_TITLE, PAGE_TITLE);
        StationUndergroundFragment fragment = new StationUndergroundFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_station_board, container, false);
        StationActivity activity = ((StationActivity) getActivity());
        final Station station = activity.getStation();
        StationBoard board = activity.getUndergroundBoard();

        services = new ArrayList<>();
        nrccMessage = (TextView) v.findViewById(R.id.nrcc_messages);

        RecyclerView list = (RecyclerView) v.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new ServiceUndergroundRowAdapter(services);
        list.setAdapter(adapter);

        refresh = (PullRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetUndergroundBoardRequest().execute(station.getUuid());
            }
        });

        if (board == null) {
            refresh.setRefreshing(true);
            new GetUndergroundBoardRequest().execute(station.getUuid());
        } else {
            updateBoard(board);
        }

        return v;
    }

    private void updateBoard(StationBoard board) {
        refresh.setRefreshing(false);
        services.clear();
        //TODO: put them in some form of vertical tabs
        for (TubeLine tubeLine : board.getTubeLines()) {
            services.addAll(tubeLine.getServices());
        }
        adapter.notifyDataSetChanged();

        ArrayList<String> nrccMessages = board.getNrccMessages();
        //TODO: Loop through all messages
        if (nrccMessages.size() > 0) {
            nrccMessage.setVisibility(View.VISIBLE);
            nrccMessage.setText(nrccMessages.get(0));
        }
    }

    private class GetUndergroundBoardRequest extends AsyncTask<String, String, StationBoard> {

        @Override
        protected StationBoard doInBackground(String... uuid) {
            Utils.log("Getting underground board...");
            return StationBoard.getUnderground(uuid[0]);
        }

        @Override
        protected void onPostExecute(StationBoard board) {
            super.onPostExecute(board);
            Utils.log("Got underground board.");
            StationActivity activity = ((StationActivity) getActivity());
            if (activity != null) {
                activity.setUndergroundBoard(board);
            }
            updateBoard(board);
        }
    }

}
