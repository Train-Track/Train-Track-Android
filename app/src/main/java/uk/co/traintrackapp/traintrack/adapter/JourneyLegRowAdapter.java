package uk.co.traintrackapp.traintrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.JourneyLeg;

public class JourneyLegRowAdapter extends BaseAdapter {

    private List<JourneyLeg> rowList;
    private LayoutInflater inflater = null;

    /**
     * @param inflater
     * @param journeyLegs
     */
    public JourneyLegRowAdapter(LayoutInflater inflater,
                                ArrayList<JourneyLeg> journeyLegs) {
        this.rowList = journeyLegs;
        this.inflater = inflater;
    }

    public int getCount() {
        return rowList.size();
    }

    public JourneyLeg getItem(int position) {
        return rowList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.row_journey_leg, null);
        JourneyLeg journeyLeg = rowList.get(position);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(journeyLeg.toString());
        return v;
    }

    public void refresh(ArrayList<JourneyLeg> journeyLegs) {
        this.rowList = journeyLegs;
        this.notifyDataSetChanged();
    }

}
