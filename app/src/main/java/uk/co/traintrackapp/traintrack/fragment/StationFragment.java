package uk.co.traintrackapp.traintrack.fragment;

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
import uk.co.traintrackapp.traintrack.adapter.ServiceItemRowAdapter;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.api.StationBoard;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationFragment extends Fragment {

    private Station station;
    private ServiceItemRowAdapter adapter;
    private ArrayList<ServiceItem> serviceItems;
    private ProgressBar progress;
    private TextView nrccMessage;
    private TextView generatedAt;

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_station, container, false);

        progress = (ProgressBar) v.findViewById(R.id.progress);
        generatedAt = (TextView) v.findViewById(R.id.generated_at);
        nrccMessage = (TextView) v.findViewById(R.id.nrcc_messages);
        serviceItems = new ArrayList<ServiceItem>();
        adapter = new ServiceItemRowAdapter(serviceItems, station, getActivity());

        final ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {
                ServiceItem serviceItem = (ServiceItem) adapter.getItem(index);
                return;
            }
        });

        new GetBoardRequest().execute(station.crsCode);

        return v;
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