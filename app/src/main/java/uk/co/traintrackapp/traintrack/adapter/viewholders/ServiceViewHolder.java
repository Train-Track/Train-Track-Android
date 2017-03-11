package uk.co.traintrackapp.traintrack.adapter.viewholders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.ServiceActivity;
import uk.co.traintrackapp.traintrack.model.Service;

public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView scheduledTime;
    public TextView estimatedTime;
    public TextView title;
    public TextView subtitle;
    public TextView platform;
    public Service service;

    public ServiceViewHolder(View v) {
        super(v);
        scheduledTime = (TextView) v.findViewById(R.id.scheduled_time);
        estimatedTime = (TextView) v.findViewById(R.id.estimated_time);
        title = (TextView) v.findViewById(R.id.title);
        subtitle = (TextView) v.findViewById(R.id.subtitle);
        platform = (TextView) v.findViewById(R.id.platform);

        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (service != null) {
            Intent intent = new Intent().setClass(v.getContext(),
                    ServiceActivity.class);
            //intent.putExtra("journey_uuid", journeyUuid);
            intent.putExtra("service_id", service.getServiceId());
            intent.putExtra("origin_name", service.getOrigin().getName());
            //intent.putExtra("station_uuid", station.getUuid());
            //intent.putExtra("station_name", station.getName());
            intent.putExtra("destination_name", service.getDestination().getName());
            intent.putExtra("operator_code", service.getOperator().getCode());
            intent.putExtra("operator_name", service.getOperator().getName());
            intent.putExtra("platform", service.getPlatform());
            intent.putExtra("time", service.getTime());
            v.getContext().startActivity(intent);
        }
    }
}
