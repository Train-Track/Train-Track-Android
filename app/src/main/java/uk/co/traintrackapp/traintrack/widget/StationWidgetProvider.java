package uk.co.traintrackapp.traintrack.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import uk.co.traintrackapp.traintrack.utils.Utils;

public class StationWidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Utils.log("HELLO FROM THE WIDGET!" + appWidgetId);

            //appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}