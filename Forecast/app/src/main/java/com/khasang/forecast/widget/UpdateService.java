package com.khasang.forecast.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.khasang.forecast.PositionManager;

/**
 * Service для обновления погоды в фоне
 * // TODO: пока только как передает в PositionManager запрос на получение текущей погоды (без автоапдейта)
 *
 */
public class UpdateService extends IntentService {
    public static final String MY_ACTION_SERVICE_START = "com.khasang.forecast.widget.service_start";
    public static final String MY_ACTION_UPDATE = "com.khasang.forecast.widget.update_weather";

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private Handler handler;

    boolean success;
    boolean stopped;

    public UpdateService() {
        super("UpdateService");
        // TODO Auto-generated constructor stub
        success = false;
        stopped = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (action.equalsIgnoreCase(MY_ACTION_SERVICE_START)) {
            myServiceStart();
        } else if (action.equalsIgnoreCase(MY_ACTION_UPDATE)) {
            askForUpdate(intent);
        }

    }

    private void askForUpdate(Intent intent) {
        // TODO: askForUpdate()
        Bundle extras = intent.getExtras();
        int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        int cityId = -1;
//        Weather weather = null;
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

                cityId = extras.getInt(Weather2Extra.KEY_CURRENT_POSITION_ID);
            // TODO: если активити нет, ЕГГОР
            PositionManager.getInstance().updateWeather(getApplicationContext(), mAppWidgetId, cityId);
        }
    }

    private void myServiceStart() {
        for (int i = 0; !stopped; i++) {
            try {
                Thread.sleep(50000); // TODO: период обновления
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (stopped) {
                break;
            }

            // TODO: запросить обновление погоды
//            askForUpdate();
            // посылаем промежуточные данные
//            Intent intentUpdate = new Intent();
//            intentUpdate.setAction(ACTION_UPDATE);
//            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
//            intentUpdate.putExtra(EXTRA_KEY_UPDATE, i);
//            sendBroadcast(intentUpdate);

//            NewAppWidget.setWeather(getApplicationContext(), i);

            success = true;

//            // формируем уведомление
//            String notificationText = String.valueOf((int) (100 * i / 10))
//                    + " %";
//            mNotification = new Notification.Builder(getApplicationContext())
//                    .setContentTitle("Progress")
//                    .setContentText(notificationText)
//                    .setTicker("Notification!")
//                    .setWhen(System.currentTimeMillis())
//                    .setDefaults(Notification.DEFAULT_SOUND)
//                    .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher)
//                    .build();
//
//            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }

        // возвращаем результат
//        Intent intentResponse = new Intent();
//        intentResponse.setAction(ACTION_MYINTENTSERVICE);
//        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
//        intentResponse.putExtra(EXTRA_KEY_OUT, extraOut);
//        sendBroadcast(intentResponse);

        // TODO: временно - handler
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Сервис отработал",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {

        String notice;

        stopped = true;

        if (success) {
            notice = "onDestroy with success";

        } else {
            notice = "onDestroy WITHOUT success!";

        }

        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_LONG)
                .show();

//        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();

    }
}
