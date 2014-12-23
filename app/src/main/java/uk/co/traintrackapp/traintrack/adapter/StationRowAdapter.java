package uk.co.traintrackapp.traintrack.adapter;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.parse.ParseGeoPoint;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Station;


public class StationRowAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater inflater;
    private List<Station> rowList;
    private List<Station> origRowList;
    private Context context;

    public StationRowAdapter(LayoutInflater inflater,
                             ArrayList<Station> stations, Context context) {
        this.inflater = inflater;
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
                List<Station> list = new ArrayList<Station>();

                if (origRowList == null) {
                    origRowList = new ArrayList<Station>(rowList);
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
                List<Station> list = new ArrayList<Station>();

                if (origRowList == null) {
                    origRowList = new ArrayList<Station>(rowList);
                }

                for (Station station : origRowList) {
                    if (station.isFavourite()) {
                        list.add(station);
                    }
                }
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
                List<Station> list = new ArrayList<Station>();
                String[] geo = gps.toString().split(",");
                ParseGeoPoint location = new ParseGeoPoint(
                        Double.valueOf(geo[0]), Double.valueOf(geo[1]));

                if (origRowList == null) {
                    origRowList = new ArrayList<Station>(rowList);
                }

                for (Station station : origRowList) {
                    double distance = station.getLocation()
                            .distanceInKilometersTo(location);
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