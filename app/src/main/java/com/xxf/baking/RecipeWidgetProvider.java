package com.xxf.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence string;
//        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent intent = new Intent(context, MyRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_list,intent);
//
//        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_name), 0);
//        String recipeName = prefs.getString(context.getString(R.string.RecipeName),"");
//        int position = prefs.getInt(context.getString(R.string.pref_position),0);
//
//        Intent intent = new Intent(context, RecipeDetailActivity.class);
//        intent.putExtra("position",position);
//        intent.putExtra(context.getString(R.string.RecipeName),recipeName);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,FLAG_UPDATE_CURRENT);
//        views.setOnClickPendingIntent(R.id.lv_appwidget,pendingIntent);
//        views.setTextViewText(R.id.lv_appwidget, "aa");
//
//        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        // 获取Widget的组件名
        for (int appWidgetId : appWidgetIds) {

//            ComponentName thisWidget = new ComponentName(context,
//                    RecipeWidgetProvider.class);

            // 创建一个RemoteView
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.recipe_widget_provider);

            // 把这个Widget绑定到RemoteViewsService
            Intent intent = new Intent(context, MyRemoteViewsService.class);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

            // 设置适配器
            remoteViews.setRemoteAdapter(R.id.widget_list, intent);

            // 设置当显示的widget_list为空显示的View
//        remoteViews.setEmptyView(R.id.widget_list, R.layout.none_data);

            // 点击列表触发事件
            Intent clickIntent = new Intent(context, RecipeWidgetProvider.class);
            // 设置Action，方便在onReceive中区别点击事件
//        clickIntent.setAction(clickAction);
//        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                    context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.widget_list,
                    pendingIntentTemplate);

            // 刷新按钮
//        final Intent refreshIntent = new Intent(context,
//                MyAppListWidgetProvider.class);
//        refreshIntent.setAction("refresh");
//        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
//                context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.button_refresh,
//                refreshPendingIntent);

            // 更新Wdiget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

