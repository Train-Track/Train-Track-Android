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

    private LayoutInflater inflater = null;
    private List<Journey> rowList;
    private Context context;

    /**
     * @param inflater
     * @param journeys
     * @param context
     */
    public JourneyRowAdapter(LayoutInflater inflater,
                             ArrayList<Journey> journeys, Context context) {
        this.inflater = inflater;
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
        View v = inflater.inflate(R.layout.row_journey, null);
        Journey journey = rowList.get(position);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(journey.toString());
        return v;
    }

    public void refresh(ArrayList<Journey> journeys) {
        this.rowList = journeys;
        this.notifyDataSetChanged();
    }

}
