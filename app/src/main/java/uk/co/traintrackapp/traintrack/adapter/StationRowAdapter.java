package uk.co.traintrackapp.traintrack.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.StationActivity;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;


public class StationRowAdapter extends RecyclerView.Adapter<StationRowAdapter.ViewHolder> implements Filterable {

    private List<Station> rowList;
    private List<Station> origRowList;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Station station;
        public TextView crsCode;
        public TextView name;
        public TextView distance;

        public ViewHolder(View v) {
            super(v);
            crsCode = (TextView) v.findViewById(R.id.crs_code);
            name = (TextView) v.findViewById(R.id.name);
            distance = (TextView) v.findViewById(R.id.distance);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (station != null) {
                Intent intent = new Intent().setClass(v.getContext(),
                        StationActivity.class);
                intent.putExtra("station_id", station.getId());
                intent.putExtra("station_crs", station.getCrsCode());
                intent.putExtra("station_name", station.getName());
                v.getContext().startActivity(intent);
            }
        }
    }

    public StationRowAdapter(ArrayList<Station> stations) {
        this.rowList = stations;
    }

    @Override
    public StationRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_station, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = rowList.get(position);
        holder.station = station;
        holder.crsCode.setText(station.getCrsCode());
        holder.name.setText(station.getName());
        holder.distance.setText(station.getDistanceText());
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                rowList = (List<Station>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Station> list = new ArrayList<>();

                if (origRowList == null) {
                    origRowList = new ArrayList<>(rowList);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = origRowList.size();
                    results.values = origRowList;
                } else {
                    for (int i = 0; i < origRowList.size(); i++) {
                        Station station = origRowList.get(i);
                        if (station.isNameSimilarTo(constraint)) {
                            list.add(station);
                        }
                    }
                    results.count = list.size();
                    results.values = list;
                }
                return results;
            }
        };

    }

    public Filter getFavouriteFilter() {
        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                rowList = (List<Station>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                User user = new User();
                try {
                    JSONObject jsonUser = new JSONObject(constraint.toString());
                    user = new User(jsonUser);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
                List<Station> list = new ArrayList<>();
                if (!user.getHomeStation().getCrsCode().isEmpty()) {
                    list.add(user.getHomeStation());
                }
                if (!user.getWorkStation().getCrsCode().isEmpty()) {
                    list.add(user.getWorkStation());
                }
                list.addAll(user.getFavouriteStations());
                results.count = list.size();
                results.values = list;
                return results;
            }
        };

    }

    public Filter getNearbyFilter() {
        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                rowList = (List<Station>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence gps) {
                FilterResults results = new FilterResults();
                List<Station> list = new ArrayList<>();
                String[] geo = gps.toString().split(",");

                if (origRowList == null) {
                    origRowList = new ArrayList<>(rowList);
                }

                for (Station station : origRowList) {
                    double distance = 0;
                    station.setDistance(distance);
                    list.add(station);
                }

                results.count = list.size();
                results.values = list;
                return results;
            }
        };

    }

    public void refresh(ArrayList<Station> stations) {
        this.rowList = stations;
        this.notifyDataSetChanged();
    }

}
