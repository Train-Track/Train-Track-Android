package uk.co.traintrackapp.traintrack.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.TrainTrack;
import uk.co.traintrackapp.traintrack.api.TubeLine;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;

import static uk.co.traintrackapp.traintrack.R.id.home_station_name;
import static uk.co.traintrackapp.traintrack.R.id.work_station_name;

public class DashboardFragment extends Fragment {

    private LinearLayout tubeLinesView;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TrainTrack app = (TrainTrack) getActivity().getApplication();
        User user = app.getUser();

        CardView workStation = (CardView) v.findViewById(R.id.work_station);
        if (user.getWorkStation() != null) {
            TextView workStationName = (TextView) v.findViewById(work_station_name);
            workStationName.setText(user.getWorkStation().toString());
            workStation.setVisibility(View.VISIBLE);
        } else {
            workStation.setVisibility(View.GONE);
        }

        CardView homeStation = (CardView) v.findViewById(R.id.home_station);
        if (user.getHomeStation() != null) {
            TextView homeStationName = (TextView) v.findViewById(home_station_name);
            homeStationName.setText(user.getHomeStation().toString());
            homeStation.setVisibility(View.VISIBLE);
        } else {
            homeStation.setVisibility(View.GONE);
        }

        tubeLinesView = (LinearLayout) v.findViewById(R.id.tube_lines);
        new GetTubeStatusRequest().execute();

        return v;
    }

    class GetTubeStatusRequest extends AsyncTask<String, String, ArrayList<TubeLine>> {

        @Override
        protected ArrayList<TubeLine> doInBackground(String... bob) {
            Utils.log("Getting tube status...");
            return TubeLine.getStatus();
        }

        @Override
        protected void onPostExecute(ArrayList<TubeLine> tubeLines) {
            super.onPostExecute(tubeLines);
            Utils.log("Got tube status.");

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(12, 12, 12, 12);

            for (TubeLine line : tubeLines) {
                TextView tubeLine = new TextView(getActivity());
                tubeLine.setText(line.getName());
                tubeLine.setTextColor(Color.parseColor("#" + line.getTextColour()));
                tubeLine.setBackgroundColor(Color.parseColor("#" + line.getBackgroundColour()));
                tubeLine.setLayoutParams(params);
                tubeLinesView.addView(tubeLine);
            }
        }

    }

}
