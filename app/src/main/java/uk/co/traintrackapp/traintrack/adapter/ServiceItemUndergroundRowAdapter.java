package uk.co.traintrackapp.traintrack.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.adapter.viewholders.ServiceItemViewHolder;
import uk.co.traintrackapp.traintrack.api.ServiceItem;

public class ServiceItemUndergroundRowAdapter extends RecyclerView.Adapter<ServiceItemViewHolder> {

    private List<ServiceItem> serviceItems;

    /**
     * A row adapter that is for underground services
     * @param serviceItems the service items
     */
    public ServiceItemUndergroundRowAdapter(ArrayList<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    @Override
    public ServiceItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_service, parent, false);
        return new ServiceItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ServiceItemViewHolder holder, int position) {
        ServiceItem serviceItem = serviceItems.get(position);
        holder.serviceItem = serviceItem;
        holder.scheduledTime.setText(serviceItem.getScheduledTime());
        holder.estimatedTime.setText(serviceItem.getEstimatedTime());
        if (serviceItem.terminatesHere()) {
            holder.title.setText("Terminates Here");
            if (serviceItem.isDelayedDeparting()) {
                holder.estimatedTime.setTextColor(Color.RED);
                holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.estimatedTime.setTextColor(Color.BLACK);
                holder.scheduledTime.setPaintFlags(0);
            }
        } else {
            holder.title.setText(serviceItem.getDestination().toString());
            if (serviceItem.isDelayedDeparting()) {
                holder.estimatedTime.setTextColor(Color.RED);
                holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.estimatedTime.setTextColor(Color.BLACK);
                holder.scheduledTime.setPaintFlags(0);
            }
        }
        if (serviceItem.startsHere()) {
            holder.subtitle.setText("Starts Here");
        } else {
            holder.subtitle.setText("From " + serviceItem.getOrigin().toString());
        }
        holder.subtitle.setText(holder.subtitle.getText() + " - "
                + serviceItem.getOperator().toString());
        holder.platform.setText(serviceItem.getPlatform());
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }

    public void refresh(ArrayList<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
        this.notifyDataSetChanged();
    }

}
