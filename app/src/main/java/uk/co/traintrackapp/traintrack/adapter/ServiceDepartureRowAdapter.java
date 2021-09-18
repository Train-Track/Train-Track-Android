package uk.co.traintrackapp.traintrack.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.adapter.viewholders.ServiceViewHolder;
import uk.co.traintrackapp.traintrack.model.Service;
import uk.co.traintrackapp.traintrack.model.Station;

public class ServiceDepartureRowAdapter extends RecyclerView.Adapter<ServiceViewHolder> {

    private List<Service> services;
    private Station station;

    /**
     * A row adapter that is for departures
     * @param services the services
     * @param station the station where the services are departing from
     */
    public ServiceDepartureRowAdapter(List<Service> services, Station station) {
        this.services = services;
        this.station = station;
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
        holder.station = station;
        DateTime std = service.getScheduledTimeDeparture();
        if (std != null) {
            holder.scheduledTime.setText(std.toString("HH:mm"));
        }
        DateTime eta = service.getEstimatedTimeDeparture();
        if (eta != null) {
            holder.estimatedTime.setText(eta.toString("HH:mm"));
        }
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
            holder.subtitle.setText("From " + service.getOrigin());
        }
        holder.subtitle.setText(holder.subtitle.getText() + " - "
                + service.getOperator());
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
