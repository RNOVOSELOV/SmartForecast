package com.khasang.forecast.widget;

/**
 * Created by 1 on 18.12.2015.
 */
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.khasang.forecast.Logger;
import com.khasang.forecast.PositionManager;
import com.khasang.forecast.R;
import com.khasang.forecast.activities.CityPickerActivity;
import com.khasang.forecast.activities.WeatherActivity;

public class WidgetConfigActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_CURRENT_POSITION = "widget_current_position_";
    public final static String WIDGET_COUNT = "widget_count_";

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sp;
    TextView tvCityName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);

        sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvCityName.setText(sp.getString(WIDGET_CURRENT_POSITION + widgetID, "HH:mm:ss"));

//        int cnt = sp.getInt(WidgetConfigActivity.WIDGET_COUNT + widgetID, -1);
//        if (cnt == -1) sp.edit().putInt(WIDGET_COUNT + widgetID, 0);
    }

//    @Override
    public void onClickOk(View v){
        sp.edit().putString(WIDGET_CURRENT_POSITION + widgetID, tvCityName.getText().toString()).commit();
//        WeatherWidget.updateWidget(this, AppWidgetManager.getInstance(this), widgetID); //
        setResult(RESULT_OK, resultValue);
        finish();
    }
//    @Override
    public void onClickAddCity(View v) {
        startActivityForResult(new Intent(this, CityPickerActivity.class), WeatherActivity.CHOOSE_CITY);
//        PositionManager.getInstance().updateWeather();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WeatherActivity.CHOOSE_CITY) {
            if (resultCode == RESULT_OK) {
                String newCity = data.getStringExtra(CityPickerActivity.CITY_PICKER_TAG);
                tvCityName.setText(newCity.split(",")[0]);
                Logger.println(TAG, newCity);
//                PositionManager.getInstance().setCurrentPosition(newCity);
//                PositionManager.getInstance().saveCurrPosition();
//                onRefresh();
//                syncBtn.setVisibility(View.VISIBLE);
            } else {
//                if (!PositionManager.getInstance().positionIsPresent(PositionManager.getInstance().getCurrentPositionName())) {
//                    stopRefresh();
//                    syncBtn.setVisibility(View.GONE);
//                }
            }
        }
    }
}