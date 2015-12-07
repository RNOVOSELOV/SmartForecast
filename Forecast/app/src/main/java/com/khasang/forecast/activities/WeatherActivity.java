package com.khasang.forecast.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.khasang.forecast.PositionManager;
import com.khasang.forecast.R;
import com.khasang.forecast.Weather;
import com.khasang.forecast.adapters.ForecastPageAdapter;
import com.khasang.forecast.sqlite.SQLiteProcessData;

import java.util.Calendar;
import java.util.Map;

/**
 * Данные которые необходимо отображать в WeatherActivity (для первого релиза):
 * город, температура, давление, влажность, ветер, временная метка.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * ViewPager для отображения нижних вкладок прогноза: по часам и по дням
     */
    private TabLayout tabLayout;
    private ViewPager pager;

    String TAG = this.getClass().getSimpleName();

    private TextView city;
    private TextView temperature;
    private TextView description;
    private TextView pressure;
    private TextView wind;
    private TextView humidity;
    private TextView timeStamp;
    private ImageButton syncBtn;
    private ImageButton cityPickerBtn;

    private Animation animationRotateCenter;
    private Animation animScale;

    private final int CHOOSE_CITY = 1;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        context = getApplicationContext();

        PositionManager.getInstance().initManager(this);

        city = (TextView) findViewById(R.id.city);
        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.precipitation);
        pressure = (TextView) findViewById(R.id.pressure);
        wind = (TextView) findViewById(R.id.wind);
        humidity = (TextView) findViewById(R.id.humidity);
        timeStamp = (TextView) findViewById(R.id.timeStamp);
        syncBtn = (ImageButton) findViewById(R.id.syncBtn);
        cityPickerBtn = (ImageButton) findViewById(R.id.cityPickerBnt);

        /** Анимация кнопки */
        animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);

        /** Слушатели нажатий кнопкок */
        syncBtn.setOnClickListener(this);
        city.setOnClickListener(this);
        cityPickerBtn.setOnClickListener(this);

        /**
         * Код для фрагментов
         */
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        ForecastPageAdapter adapter = new ForecastPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_by_hour_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_by_date_24);


        city.setText("--/--");
        temperature.setText("--/--");
        description.setText("--/--");
        pressure.setText("--/--");
        wind.setText("--/--");
        humidity.setText("--/--");
        timeStamp.setText("--/--");
        if (PositionManager.getInstance().getPositions().size() == 0) {
            Toast.makeText(this, "Для продолжения работы необходимо добавить город.", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(this, CityPickerActivity.class), CHOOSE_CITY);
        } else {
            if (!PositionManager.getInstance().getCurrentPositionName().isEmpty()) {
                PositionManager.getInstance().getCurrentForecast();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PositionManager.getInstance().saveSettings();
    }

    /**
     * Обработчик нажатия кнопки
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.syncBtn:
                syncBtn.startAnimation(animationRotateCenter);
                PositionManager.getInstance().getCurrentForecast();
                break;
            case R.id.city:
                startActivityForResult(new Intent(this, CityPickerActivity.class), CHOOSE_CITY);
                break;
            case R.id.cityPickerBnt:
                startActivityForResult(new Intent(this, CityPickerActivity.class), CHOOSE_CITY);
                break;
        }
    }

    /**
     * Обновление интерфейса Activity
     */
    public void updateInterface(Calendar date, Weather wCurent) {

        //TODO нужно перепроверить
        if (wCurent == null) {
            Log.i(TAG, "Weather is null!");
            return;
        }
        /** Получаем текущее время
         * TODO minutes в формате 13:04, сейчас выводит 13:4
         * TODO UPDATE Check fixes
         * */
        int hours = date.get(Calendar.HOUR_OF_DAY);
        int minutes = date.get(Calendar.MINUTE);

        city.setText(PositionManager.getInstance().getCurrentPositionName()); // отображаем имя текущей локации
        temperature.setText(String.format("%.0f°C", wCurent.getTemperature()));

        description.setText(String.format("%s", wCurent.getDescription()
                .substring(0, 1)
                .toUpperCase() + wCurent
                .getDescription()
                .substring(1)));

        pressure.setText(String.format("%s %.0f %s",
                getString(R.string.pressure),
                wCurent.getPressure(),
                getString(R.string.pressure_measure)));

        wind.setText(Html.fromHtml(String.format("%s %s %.0f%s",
                getString(R.string.wind),
                wCurent.getWindDirection(),
                wCurent.getWindPower(),
                getString(R.string.wind_measure))));

        humidity.setText(String.format("%s %s%%",
                getString(R.string.humidity),
                wCurent.getHumidity()));

        timeStamp.setText(String.format("%s %d:%02d",
                getString(R.string.timeStamp),
                hours,
                minutes));

        PositionManager.getInstance().getHourlyForecast();
    }

    //TODO Реализовать метод получения прогноза по часам
    public void updateHourForecast(Map<Calendar, Weather> hourlyForecast) {
        ((ForecastPageAdapter) pager.getAdapter()).setHourForecast(hourlyForecast);
        PositionManager.getInstance().getDailyForecast();
    }

    //TODO Реализовать метод получения прогноза по дням
    public void updateDayForecast(Map<Calendar, Weather> weeklyForecast) {
        ((ForecastPageAdapter) pager.getAdapter()).setDayForecast(weeklyForecast);
    }

    /**
     * Получаем город из CityPickActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CITY) {
            if (resultCode == RESULT_OK) {
                String newCity = data.getStringExtra(CityPickerActivity.CITY_PICKER_TAG);
                city.setText(newCity);
                Log.d(TAG, newCity);
                PositionManager.getInstance().setCurrentPosition(newCity);
                PositionManager.getInstance().saveCurrPosition();
                PositionManager.getInstance().getCurrentForecast();
                syncBtn.setVisibility(View.VISIBLE);
            } else {
                if (!PositionManager.getInstance().positionIsPresent(PositionManager.getInstance().getCurrentPositionName())) {
                    syncBtn.setVisibility(View.GONE);
                }
            }
        }
    }
}

