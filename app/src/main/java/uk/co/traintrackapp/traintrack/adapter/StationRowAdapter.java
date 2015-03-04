package uk.co.traintrackapp.traintrack.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.model.User;
import uk.co.traintrackapp.traintrack.utils.Utils;


public class StationRowAdapter extends BaseAdapter implements Filterable {

    private List<Station> rowList;
    private List<Station> origRowList;
    private Context context;

    public StationRowAdapter(ArrayList<Station> stations, Context context) {
        this.rowList = stations;
        this.context = context;
    }

    public int getCount() {
        return rowList.size();
    }

    public Station getItem(int position) {
        return rowList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_station, null);
            holder = new ViewHolder();
            holder.crsCode = (TextView) convertView
                    .findViewById(R.id.crs_code);
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.distance = (TextView) convertView
                    .findViewById(R.id.distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Station station = rowList.get(position);
        holder.crsCode.setText(station.getCrsCode());
        holder.name.setText(station.getName());
        if (station.getDistance() > 0) {
            holder.distance.setText(station.getDistanceText());
        }

        return convertView;
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

                results.count = user.getFavouriteStations().size();
                results.values = user.getFavouriteStations();
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

    static class ViewHolder {
        TextView crsCode;
        TextView name;
        TextView distance;
    }

}