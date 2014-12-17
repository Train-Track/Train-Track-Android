package uk.co.traintrackapp.traintrack.fragment;

import android.app.Activity;
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
import uk.co.traintrackapp.traintrack.adapter.CallingPointRowAdapter;
import uk.co.traintrackapp.traintrack.api.CallingPoint;
import uk.co.traintrackapp.traintrack.api.Service;
import uk.co.traintrackapp.traintrack.api.ServiceItem;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class ServiceFragment extends Fragment {

    private OnServiceFragmentInteractionListener mListener;
    private ServiceItem serviceItem;
    private Station station;
    private CallingPointRowAdapter adapter;
    private ProgressBar progress;
    private ArrayList<CallingPoint> callingPoints;
    private TextView disruptionReason;
    private TextView generatedAt;

    public void setService(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }
    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service, container, false);

        progress = (ProgressBar) v.findViewById(R.id.progress);
        disruptionReason = (TextView) v.findViewById(R.id.disruption_reason);
        generatedAt = (TextView) v.findViewById(R.id.generated_at);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(serviceItem.getOriginName() + " to " + serviceItem.getDestinationName());
        TextView toc = (TextView) v.findViewById(R.id.toc);
        toc.setText(serviceItem.getOperatorName());

        callingPoints = new ArrayList<CallingPoint>();
        adapter = new CallingPointRowAdapter(callingPoints, station.crsCode, getActivity());
        ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long x) {

            }
        });

        new GetServiceRequest().execute(serviceItem.getServiceId());

        return v;
    }

    class GetServiceRequest extends AsyncTask<String, String, Service> {

        @Override
        protected Service doInBackground(String... service) {
            Utils.log("Getting service...");
            return Service.getByServiceId(service[0]);
        }

        @Override
        protected void onPostExecute(Service s) {
            super.onPostExecute(s);
            Utils.log("Got service.");
            progress.setVisibility(View.GONE);
            callingPoints.clear();
            callingPoints.addAll(s.getCallingPoints());
            adapter.notifyDataSetChanged();

            if (s.getDisruptionReason() != null) {
                disruptionReason.setText(s.getDisruptionReason());
                disruptionReason.setVisibility(View.VISIBLE);
            }

            generatedAt.setText(s.getGeneratedAtString());

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnServiceFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnServiceFragmentInteractionListener {
        public void onServiceFragmentInteractionListener(CallingPoint callingPoint);
    }


}