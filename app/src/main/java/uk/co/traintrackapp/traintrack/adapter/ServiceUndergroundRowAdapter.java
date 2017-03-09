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
import uk.co.traintrackapp.traintrack.adapter.viewholders.ServiceViewHolder;
import uk.co.traintrackapp.traintrack.api.Service;

public class ServiceUndergroundRowAdapter extends RecyclerView.Adapter<ServiceViewHolder> {

    private List<Service> services;

    /**
     * A row adapter that is for underground services
     * @param services the services
     */
    public ServiceUndergroundRowAdapter(ArrayList<Service> services) {
        this.services = services;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_service, parent, false);
        return new ServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.service = service;
        holder.scheduledTime.setText(service.getScheduledTimeDeparture());
        holder.estimatedTime.setText(service.getEstimatedTimeDeparture());
        if (service.terminatesHere()) {
            holder.title.setText("Terminates Here");
            if (service.isDelayedDeparting()) {
                holder.estimatedTime.setTextColor(Color.RED);
                holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.estimatedTime.setTextColor(Color.BLACK);
                holder.scheduledTime.setPaintFlags(0);
            }
        } else {
            holder.title.setText(service.getDestination().toString());
            if (service.isDelayedDeparting()) {
                holder.estimatedTime.setTextColor(Color.RED);
                holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.estimatedTime.setTextColor(Color.BLACK);
                holder.scheduledTime.setPaintFlags(0);
            }
        }
        if (service.startsHere()) {
            holder.subtitle.setText("Starts Here");
        } else {
            holder.subtitle.setText("From " + service.getOrigin().toString());
        }
        holder.subtitle.setText(holder.subtitle.getText() + " - "
                + service.getOperator().toString());
        holder.platform.setText(service.getPlatform());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void refresh(ArrayList<Service> services) {
        this.services = services;
        this.notifyDataSetChanged();
    }

}
