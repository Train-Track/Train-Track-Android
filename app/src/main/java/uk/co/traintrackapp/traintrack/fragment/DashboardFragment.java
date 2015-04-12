package uk.co.traintrackapp.traintrack.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.traintrackapp.traintrack.MainActivity;
import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
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
        final User user = app.getUser();

        CardView newJourney = (CardView) v.findViewById(R.id.new_journey);
        newJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Choose a departing station", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).updateFragment(MainActivity.STATIONS_FRAGMENT);
            }
        });

        CardView workStation = (CardView) v.findViewById(R.id.work_station);
        if (user.getWorkStation().getId() != 0) {
            TextView workStationName = (TextView) v.findViewById(work_station_name);
            workStationName.setText(user.getWorkStation().toString());
            workStation.setVisibility(View.VISIBLE);
            workStation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent().setClass(v.getContext(),
                            StationActivity.class);
                    intent.putExtra("station_uuid", user.getWorkStation().getUuid());
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            workStation.setVisibility(View.GONE);
        }

        CardView homeStation = (CardView) v.findViewById(R.id.home_station);
        if (user.getHomeStation().getId() != 0) {
            TextView homeStationName = (TextView) v.findViewById(home_station_name);
            homeStationName.setText(user.getHomeStation().toString());
            homeStation.setVisibility(View.VISIBLE);
            homeStation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent().setClass(v.getContext(),
                            StationActivity.class);
                    intent.putExtra("station_uuid", user.getHomeStation().getUuid());
                    v.getContext().startActivity(intent);
                }
            });
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
            return TubeLine.getStatuses();
        }

        @Override
        protected void onPostExecute(ArrayList<TubeLine> tubeLines) {
            super.onPostExecute(tubeLines);
            Utils.log("Got tube status.");

            FrameLayout.LayoutParams linearLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParams.setMargins(12, 0, 12, 0);
            TableLayout.LayoutParams textViewParams = new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            TableLayout.LayoutParams statusTextParams = new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);

            for (TubeLine tubeLine : tubeLines) {

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setLayoutParams(linearLayoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setBackgroundColor(Color.parseColor("#" + tubeLine.getBackgroundColour()));
                linearLayout.setPadding(12, 12, 12, 12);

                TextView name = new TextView(getActivity());
                name.setLayoutParams(textViewParams);
                name.setText(tubeLine.getName());
                name.setTextSize(18);
                name.setTextColor(Color.parseColor("#" + tubeLine.getTextColour()));
                linearLayout.addView(name);

                TextView status = new TextView(getActivity());
                status.setLayoutParams(statusTextParams);
                status.setText(tubeLine.getStatus());
                status.setTextSize(18);
                status.setTextColor(Color.parseColor("#" + tubeLine.getTextColour()));
                status.setGravity(Gravity.END);
                linearLayout.addView(status);

                tubeLinesView.addView(linearLayout);
            }
        }

    }

}
