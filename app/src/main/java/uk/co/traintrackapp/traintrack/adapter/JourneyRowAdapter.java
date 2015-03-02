package uk.co.traintrackapp.traintrack.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Journey;

public class JourneyRowAdapter extends BaseAdapter {

    private List<Journey> rowList;
    private Context context;

    /**
     * @param journeys journey list
     * @param context the application context
     */
    public JourneyRowAdapter(ArrayList<Journey> journeys, Context context) {
        this.rowList = journeys;
        this.context = context;
    }

    public int getCount() {
        return rowList.size();
    }

    public Journey getItem(int position) {
        return rowList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_journey, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Journey journey = rowList.get(position);
        holder.name.setText(journey.toString());
        return convertView;
    }

    /*
    public void refresh(ArrayList<Journey> journeys) {
        this.rowList = journeys;
        this.notifyDataSetChanged();
    }
    */

    static class ViewHolder {
        TextView name;
    }

}
