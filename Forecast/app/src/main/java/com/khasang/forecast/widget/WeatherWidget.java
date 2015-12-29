package com.khasang.forecast.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.khasang.forecast.Position;
import com.khasang.forecast.PositionManager;
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
    // TODO: назначить обработчики для кнопки выбора города.

public class WeatherWidget extends AppWidgetProvider {
    private static String TAG = WeatherWidget.class.getSimpleName();
    private String temp_measure = "°C"; // TODO: сделать смену единиц измерения температуры

    private Position currPosition;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(WidgetConfigActivity.WIDGET_CURRENT_POSITION + widgetID);
            editor.remove(WidgetConfigActivity.WIDGET_COUNT + widgetID);
        }
        editor.commit();
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

//                city = extras.getString(Weather2Extra.KEY_CITY);
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
                updateAppWidget(context, appWidgetManager, widgetId);

            }
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        updateAppWidget(context, appWidgetManager, appWidgetId, -1, null);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId
            , int cityId, Weather weather) {
        SharedPreferences sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);


//        String city = sp.getString(WidgetConfigActivity.WIDGET_CURRENT_POSITION
//                + appWidgetId, null);
//        int cityId = sp.getInt(WidgetConfigActivity.WIDGET_CURRENT_POSITION
//                + appWidgetId, -1);
        if (cityId > -1) {
            Position city = PositionManager.getInstance().getPosition(cityId);
            String cityName = city.getLocationName();
            if (cityName != null) {
                remoteViews.setTextViewText(R.id.tv_city, cityName);
            }
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

            // todo
//            String temperatureString = String.format("%.0f%s", weather.getTemperature(), temp_measure);
//            remoteViews.setTextViewText(R.id.tv_temperature, temperatureString);


        }
        // Конфигурационный экран
        PendingIntent pIntentOpenConfig = getPendingIntentOpenConfig(context, appWidgetId, cityId);
        remoteViews.setOnClickPendingIntent(R.id.btnCityPicker, pIntentOpenConfig);

        // запрос на обновление
        PendingIntent pIntentAskForUpdate = getPendingIntentAskForUpdate(context, appWidgetId, cityId);
        remoteViews.setOnClickPendingIntent(R.id.id_update, pIntentAskForUpdate);


        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static PendingIntent getPendingIntentAskForUpdate(Context context, int appWidgetId, int cityId) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(WidgetConfigActivity.WIDGET_CURRENT_POSITION, cityId);
        return PendingIntent.getService(context, 0, intent, 0);
    }

    private static PendingIntent getPendingIntentOpenConfig(Context ctx, int appWidgetId, int cityId) {
        Intent intent = new Intent(ctx, WidgetConfigActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(WidgetConfigActivity.WIDGET_CURRENT_POSITION, cityId);
        return PendingIntent.getActivity(ctx, appWidgetId, intent, 0);
    }

    public static void setWeather(Context context, String city, Map<Calendar, Weather> calendarWeatherMap) {
        // формируем и отправляем Intent виджетам
        HashMap.Entry<Calendar, Weather> firstEntry = (Map.Entry<Calendar, Weather>) calendarWeatherMap.entrySet().iterator().next();
        Weather weather = firstEntry.getValue();
        Calendar date = firstEntry.getKey();
        if (weather == null) {
            Log.i(TAG, "Weather is null!");
            return;
        }

        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE);
        Weather2Extra.putWeatherToExtra(intent, weather)
                .putExtra(Weather2Extra.KEY_CITY, city) // TODO: переделать city на cityId
                .putExtra(Weather2Extra.KEY_DATE, date);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherActivity.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}


