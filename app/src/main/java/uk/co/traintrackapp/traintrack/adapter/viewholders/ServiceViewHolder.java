package uk.co.traintrackapp.traintrack.adapter.viewholders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.ServiceActivity;
import uk.co.traintrackapp.traintrack.model.Service;
import uk.co.traintrackapp.traintrack.utils.Utils;

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
            Bundle bundle = new Bundle();
            bundle.putSerializable(Utils.ARGS_SERVICE, service);
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        }
    }

}
