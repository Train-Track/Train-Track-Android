package uk.co.traintrackapp.traintrack.adapter;


import java.util.ArrayList;
import java.util.List;

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

    private List<Station> rowList;
    private List<Station> origRowList;
    private LayoutInflater inflater = null;

    public StationRowAdapter(LayoutInflater inflater,
                             ArrayList<Station> stations) {
        this.rowList = stations;
        this.inflater = inflater;
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
        View v = inflater.inflate(R.layout.row_station, null);
        Station station = rowList.get(position);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(station.getName());
        TextView crsCode = (TextView) v.findViewById(R.id.crs_code);
        crsCode.setText(station.getCrsCode());
        if (station.getDistance() > 0) {
            TextView distance = (TextView) v.findViewById(R.id.distance);
            distance.setText(station.getDistanceText());
        }
        return v;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
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
        return filter;

    }

    public Filter getFavouriteFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
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
        return filter;

    }

    public Filter getNearbyFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
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
        return filter;

    }

    public void refresh(ArrayList<Station> stations) {
        this.rowList = stations;
        this.notifyDataSetChanged();
    }

}