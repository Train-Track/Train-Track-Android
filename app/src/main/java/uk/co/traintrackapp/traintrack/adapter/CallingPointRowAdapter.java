package uk.co.traintrackapp.traintrack.adapter;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.api.CallingPoint;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class CallingPointRowAdapter extends BaseAdapter {

    private ArrayList<CallingPoint> callingPoints;
    private String stationCrs = null;
    private Context context;

    /**
     * @param callingPoints
     *            to be displayed
     * @param stationCrs
     *            the station it is being seen from
     * @param context
     *            the context the adapter is being used in
     */
    public CallingPointRowAdapter(ArrayList<CallingPoint> callingPoints,
                                  String stationCrs, Context context) {
        this.callingPoints = callingPoints;
        this.stationCrs = stationCrs;
        this.context = context;
    }

    public int getCount() {
        return callingPoints.size();
    }

    public CallingPoint getItem(int position) {
        return callingPoints.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_calling_point, null);
            holder = new ViewHolder();
            holder.icon = (TextView) convertView.findViewById(R.id.icon);
            holder.scheduledTime = (TextView) convertView
                    .findViewById(R.id.scheduled_time);
            holder.station = (TextView) convertView.findViewById(R.id.station);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CallingPoint callingPoint = callingPoints.get(position);
        holder.icon.setBackgroundDrawable(context.getResources().getDrawable(
                callingPoint.getIcon()));
        holder.scheduledTime.setText(callingPoint.getScheduledTime());
        holder.station.setText(callingPoint.getStationName());
        if (callingPoint.getStationCrs().equals(this.stationCrs)) {
            holder.station.setTextColor(Utils.BLUE);
            convertView.setEnabled(false);
        } else if (callingPoint.hasArrived()) {
            holder.station.setTextColor(Color.GRAY);
            convertView.setEnabled(false);
        } else {
            holder.station.setTextColor(Color.BLACK);
            convertView.setEnabled(true);
        }
        if (callingPoint.hasArrived()) {
            holder.time.setText(callingPoint.getActualTime());
        } else {
            holder.time.setText(callingPoint.getEstimatedTime());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView icon;
        TextView scheduledTime;
        TextView station;
        TextView time;
    }
}
