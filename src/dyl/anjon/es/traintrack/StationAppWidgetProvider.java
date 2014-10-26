package dyl.anjon.es.traintrack;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import dyl.anjon.es.traintrack.utils.Utils;

public class StationAppWidgetProvider extends AppWidgetProvider {
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
