package com.dex.redditreader.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.dex.redditreader.R;
import com.dex.redditreader.sync.PostSyncJob;
import com.dex.redditreader.ui.PostDetailActivity;
import com.dex.redditreader.ui.PostListActivity;


public class PostWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.posts_widget);

            Intent intent = new Intent(context, PostListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.title, pendingIntent);

            views.setRemoteAdapter(R.id.list,
                    new Intent(context, PostWidgetRemoteViewService.class));

            PendingIntent clickPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(new Intent(context, PostDetailActivity.class))
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list, clickPendingIntent);

            views.setEmptyView(R.id.list, R.id.widget_empty);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                || intent.getAction().equals(PostSyncJob.ACTION_DATA_UPDATED)) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
        }
    }
}
