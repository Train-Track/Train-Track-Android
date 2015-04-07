package uk.co.traintrackapp.traintrack.adapter.viewholders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.ServiceActivity;
import uk.co.traintrackapp.traintrack.api.ServiceItem;

public class ServiceItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView scheduledTime;
    public TextView estimatedTime;
    public TextView title;
    public TextView subtitle;
    public TextView platform;
    public ServiceItem serviceItem;

    public ServiceItemViewHolder(View v) {
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
        if (serviceItem != null) {
            Intent intent = new Intent().setClass(v.getContext(),
                    ServiceActivity.class);
            //intent.putExtra("journey_uuid", journeyUuid);
            intent.putExtra("service_id", serviceItem.getServiceId());
            intent.putExtra("origin_name", serviceItem.getOrigin().getName());
            //intent.putExtra("station_uuid", station.getUuid());
            //intent.putExtra("station_name", station.getName());
            intent.putExtra("destination_name", serviceItem.getDestination().getName());
            intent.putExtra("operator_code", serviceItem.getOperator().getCode());
            intent.putExtra("operator_name", serviceItem.getOperator().getName());
            intent.putExtra("platform", serviceItem.getPlatform());
            intent.putExtra("time", serviceItem.getTime());
            v.getContext().startActivity(intent);
        }
    }
}
