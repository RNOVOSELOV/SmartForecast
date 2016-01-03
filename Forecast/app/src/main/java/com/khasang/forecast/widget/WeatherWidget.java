package com.khasang.forecast.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.RemoteViews;

import com.khasang.forecast.Position;
import com.khasang.forecast.PositionManager;
import com.khasang.forecast.Weather;
import com.khasang.forecast.R;
import com.khasang.forecast.activities.WeatherActivity;

import java.util.Calendar;
import java.util.Date;
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
    private static HashMap<Integer, Position> widgetToCityMap = new HashMap<Integer, Position>();

    private static String temp_measure = "°C"; // TODO: сделать смену единиц измерения температуры

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
//        SharedPreferences.Editor editor = context.getSharedPreferences(
//                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
//        for (int widgetID : appWidgetIds) {
//            editor.remove(WidgetConfigActivity.KEY_CURRENT_POSITION_ID + widgetID);
//            editor.remove(WidgetConfigActivity.WIDGET_COUNT + widgetID);
//        }
//        editor.commit();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
//        action.equalsIgnoreCase(AppWidgetManager.)
        if (action.equalsIgnoreCase(UpdateService.MY_ACTION_UPDATE)) {
            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            Calendar date = null;
            int cityId = -1;
            Weather weather = null;
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                // TODO: дата не извлекается, разобраться
                date = (Calendar) extras.getParcelable(Weather2Extra.KEY_DATE);
                cityId = extras.getInt(Weather2Extra.KEY_CURRENT_POSITION_ID);
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
                updateAppWidget(context, appWidgetManager, widgetId, date, null, weather);

            }
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        updateAppWidget(context, appWidgetManager, appWidgetId, null, null, null/*, false*/);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId
            , Calendar date, String cityName, Weather weather/*, boolean updateCity*/) {

        SharedPreferences sp = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        if (cityName == null) {
            cityName = sp.getString(Weather2Extra.KEY_CURRENT_POSITION
                    + appWidgetId, null);
        }

        if (weather != null/* && cityName != null*/) {
            updateCurrentWeather(remoteViews, date, cityName, weather);
        }
//        // Конфигурационный экран
//        PendingIntent pIntentOpenConfig = getPendingIntentOpenConfig(context, appWidgetId, cityId);
//        remoteViews.setOnClickPendingIntent(R.id.btnCityPicker, pIntentOpenConfig);

        // запрос на обновление
        PendingIntent pIntentAskForUpdate = getPendingIntentAskForUpdate(context, appWidgetId, date, -1/*cityId*/);
        remoteViews.setOnClickPendingIntent(R.id.id_update, pIntentAskForUpdate);


        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateCurrentWeather(RemoteViews remoteViews, Calendar date, String cityName, Weather weather) {
    // TODO: getString не работает. получить значения из strings.xml

        if (weather == null) {
            Log.i(TAG, "Weather is null!");
            return;
        }

        /** Получаем текущее время */
        int hours = 0;
        int minutes = 0;
        if (date != null) {
            hours = date.get(Calendar.HOUR_OF_DAY);
            minutes = date.get(Calendar.MINUTE);
        } else { //TODO: временно, чтобы увидеть, что виджет обновился
            Date d = new Date();
            hours = d.getHours();
            minutes = d.getMinutes();
        }

        cityName = PositionManager.getInstance().getCurrentPositionName().split(",")[0]; // todo
        remoteViews.setTextViewText(R.id.tv_city, cityName);
        remoteViews.setTextViewText(R.id.tv_temperature, String.format("%.0f%s", weather.getTemperature(), temp_measure));

        String description = String.format("%s", weather.getDescription()
                .substring(0, 1)
                .toUpperCase() + weather
                .getDescription()
                .substring(1));
        remoteViews.setTextViewText(R.id.tv_description, description);

        String pressure = String.format("%s %.0f %s",
                "Давление", //getString(R.string.pressure),
                weather.getPressure(),
                R.string.pressure_measure//getString(R.string.pressure_measure)
                );
        remoteViews.setTextViewText(R.id.tv_pressure, pressure);

        Spanned wind = Html.fromHtml(String.format("%s %s %.0f%s",
                "Ветер", //getString(R.string.wind),
                weather.getWindDirection().getDirectionString(),
                weather.getWindPower(),
                "м/с"//getString(R.string.wind_measure)
                ));
        remoteViews.setTextViewText(R.id.tv_wind, wind);

        String humidity = String.format("%s %s%%",
                "Влажность",//getString(R.string.humidity),
                weather.getHumidity());
        remoteViews.setTextViewText(R.id.tv_humidity, humidity);

        String timeStamp = String.format("%s %d:%02d",
                "Обновлено",//getString(R.string.timeStamp),
                hours,
                minutes);
        remoteViews.setTextViewText(R.id.tv_date_time, timeStamp);

    }

    private static PendingIntent getPendingIntentAskForUpdate(Context context, int appWidgetId, Calendar date, int cityId) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                .putExtra(Weather2Extra.KEY_DATE, date)
                .putExtra(Weather2Extra.KEY_CURRENT_POSITION_ID, cityId);
        return PendingIntent.getService(context, 0, intent, 0);
    }

    private static PendingIntent getPendingIntentOpenConfig(Context ctx, int appWidgetId, Calendar date, int cityId) {
        Intent intent = new Intent(ctx, WidgetConfigActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                .putExtra(Weather2Extra.KEY_DATE, date)
                .putExtra(Weather2Extra.KEY_CURRENT_POSITION_ID, cityId);
        return PendingIntent.getActivity(ctx, appWidgetId, intent, 0);
    }

    public static void setWeather(Context context, int cityId, Map<Calendar, Weather> calendarWeatherMap) {
        // формируем и отправляем Intent виджетам
        HashMap.Entry<Calendar, Weather> firstEntry = (Map.Entry<Calendar, Weather>) calendarWeatherMap.entrySet().iterator().next();
        Weather weather = firstEntry.getValue();
        Calendar date = firstEntry.getKey();
        if (weather == null) {
            Log.i(TAG, "Weather is null!");
            return;
        }
        // отправляем результат виджетам
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UpdateService.MY_ACTION_UPDATE);
        Weather2Extra.putWeatherToExtra(intent, weather)
                .putExtra(Weather2Extra.KEY_CURRENT_POSITION_ID, cityId)
                .putExtra(Weather2Extra.KEY_DATE, date);
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherActivity.class));
        // TODO: пока различать погоду для виджетов будем по cityId, не по id виджетов
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}


