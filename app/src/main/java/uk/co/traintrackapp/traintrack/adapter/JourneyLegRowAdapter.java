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
     * @param inflater layout this is going in
     * @param journeyLegs list of journey legs to show
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

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_journey_leg, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JourneyLeg journeyLeg = rowList.get(position);
        holder.name.setText(journeyLeg.toString());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
    }

}
