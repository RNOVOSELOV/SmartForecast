package com.khasang.forecast.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.khasang.forecast.Weather;
import com.khasang.forecast.R;
import com.khasang.forecast.activities.WeatherActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Widget, показывающий текущую погоду
 *
 *
 */

// TODO: доделать:
// открывать активити при нажатии на кнопку
// возможно, стоит добавить конфигурационный активити с выбором города (чтобы на каждом виджете выбирать свой)
// сделать смену единиц измерения температуры

public class WeatherWidget extends AppWidgetProvider {
    private String temp_measure = "°C"; // TODO: сделать смену единиц измерения температуры

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // TODO: разобраться!!!
        String action = intent.getAction();
        if (action.equalsIgnoreCase(UpdateService.MY_ACTION_UPDATE)) {
            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            String city = null;
            Weather weather = null;
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

                city = extras.getString(Weather2Extra.KEY_CITY);
                weather = Weather2Extra.getWeatherFromExtra(extras);
            }
//            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
//                city = extras.getFloat(MyIntentService.KEY_CITY);

            // Обновляем виджет
//                updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);

            // TODO: пока обновляем все виджеты
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //getComponentName
            ComponentName thisWidget = new ComponentName(context, WeatherWidget.class);
            //get the IDs for all the instances of this widget
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            //update all of the widgets
            for (int widgetId : allWidgetIds) {
                //update the widget with any change we just made
                updateAppWidget(context, appWidgetManager, widgetId, city, weather);

            }
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        updateAppWidget(context, appWidgetManager, appWidgetId, null, null);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId
            , String city, Weather weather) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        // запрос на обновление
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getService(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.id_update, pIntent);

        if (city != null) {
            remoteViews.setTextViewText(R.id.tv_city, city);
        }
        if (weather != null) {
            // TODO: привести в порядок, по аналогии с активити
            String precipitationString = weather.getPrecipitation().toString();
            remoteViews.setTextViewText(R.id.tv_precipitation, precipitationString);
            String windString = "Ветер " + weather.getWindDirection().getDirectionString() + " "
                    + weather.getWindPower() + "м/с";
            remoteViews.setTextViewText(R.id.tv_wind, windString);
            String humidityString = "Влажность " + weather.getHumidity() + "%";
            remoteViews.setTextViewText(R.id.tv_humidity, humidityString);

            String temperatureString = String.format("%.0f%s", weather.getTemperature(), temp_measure);
            remoteViews.setTextViewText(R.id.tv_temperature, temperatureString);


        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void setWeather(Context context, String city, Map<Calendar, Weather> calendarWeatherMap) {
        // формируем и отправляем Intent виджетам
        HashMap.Entry<Calendar, Weather> firstEntry = (Map.Entry<Calendar, Weather>) calendarWeatherMap.entrySet().iterator().next();
        Weather weather = firstEntry.getValue();

        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE);
        Weather2Extra.putWeatherToExtra(intent, weather)
                .putExtra(Weather2Extra.KEY_CITY, city);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherActivity.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}

