package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.adapter.AdapterFutureWeather;
import com.example.weather.adapter.AdapterHourWeather;
import com.example.weather.api.WeatherApi;
import com.example.weather.app_widget.MyWeatherWidget;
import com.example.weather.data.WeatherData;
import com.example.weather.data.WeatherEventData;
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.page.Settings;
import com.example.weather.service.WeatherBinder;
import com.example.weather.service.WeatherService;
import com.example.weather.service.WeatherServiceConnect;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.GeocoderMgr;
import com.example.weather.utils.Route;
import com.example.weather.utils.ServiceUtils;
import com.example.weather.utils.SharedPrefUtils;
import com.example.weather.utils.WeatherImg;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private SharedPrefUtils sharedPrefUtils;
    private CityModel cityModel;
    private WeatherFutureModel weatherFutureModel;
    private LinearLayout linMain;
    private ImageView imgCurrentWeather, imgSetting;
    private TextView txtTown, txtCurrentTemp, txtCurrentDesc, txtCurrentTempRange;
    private RecyclerView recyclerViewWeatherHour, recyclerViewWeather;
    private AdapterHourWeather adapterHourWeather;
    private AdapterFutureWeather adapterFutureWeather;
    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        linMain = findViewById(R.id.lin_main);
        imgCurrentWeather = findViewById(R.id.img_current_weather);
        imgSetting = findViewById(R.id.img_setting);
        txtTown = findViewById(R.id.txt_town);
        txtCurrentTemp = findViewById(R.id.txt_current_temp);
        txtCurrentDesc = findViewById(R.id.txt_current_desc);
        txtCurrentTempRange = findViewById(R.id.txt_current_temp_range);
        recyclerViewWeatherHour = findViewById(R.id.recycler_view_weather_hour);
        recyclerViewWeather = findViewById(R.id.recycler_view_weather);
        recyclerViewWeatherHour.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWeather.setNestedScrollingEnabled(true);
        sharedPrefUtils = new SharedPrefUtils(this);

        //??????????????????????????????
        DateTimeUtils.setCurrentMode(this);
        getCurrentMode();


        cityModel = new CityModel(this);
        weatherFutureModel = new WeatherFutureModel();

        //????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        //??????????????????
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Route route = new Route(MainActivity.this);
                route.toSettings();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        //???????????????????????????????????????
        runService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //??????????????????????????????
        getLocal();
    }

    //????????????api?????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeatherEventData eventData) {
        if(eventData.getCode() == 1){
            //??????
            //??????????????????????????????
            WeatherData data = eventData.getWeatherData();
            weatherFutureModel.setLocation(data.getWeatherRecord().getLocations().get(0).getLocation());
            weatherFutureModel.setWeatherElements(cityModel.getMyCity());
            //??????????????????
            imgCurrentWeather.setImageResource(
                    WeatherImg.getImgByWeather(Integer.parseInt(weatherFutureModel.getNowPhenomenon()[1])));
            txtCurrentTemp.setText(weatherFutureModel.getNowTemp() + "??C");
            txtCurrentDesc.setText(weatherFutureModel.getNowPhenomenon()[0]);
            //????????????????????????
            txtCurrentTempRange.setText(weatherFutureModel.getNowRealTemp() + "??C");

            //?????????????????????????????????
            adapterHourWeather = new AdapterHourWeather(weatherFutureModel);
            recyclerViewWeatherHour.setAdapter(adapterHourWeather);

            //??????????????????????????????
            adapterFutureWeather = new AdapterFutureWeather(weatherFutureModel);
            recyclerViewWeather.setAdapter(adapterFutureWeather);
        }else{
            //??????
            Toast.makeText(MainActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //????????????????????????
    private void getLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //????????????
                //??????????????????
                cityModel.setLocalRegion();
            } else {
                //??????????????????????????????
                cityModel.setLocationByGPS();
            }
        } else {
            //??????????????????????????????
            cityModel.setLocationByGPS();
        }


        txtTown.setText(cityModel.getMyCountry()+cityModel.getMyCity());

        //????????????????????????api
        runService();
        //????????????????????????????????????????????????????????????
//        if(sharedPrefUtils.getIsChangeCity()){
//            notifyAppWidget();
//            sharedPrefUtils.setIsChangeCity(false);
//        }
    }

    //?????????????????????????????????
    private void notifyAppWidget(){
        Intent intent = new Intent(this, MyWeatherWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int [] ids = AppWidgetManager.getInstance(
                getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MyWeatherWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    //???????????????????????????api
    private void runService(){
        //???????????????
        Boolean isRun = ServiceUtils.isServiceRun(MainActivity.this, WeatherService.class.getName());
        Intent intent = new Intent(MainActivity.this, WeatherService.class);
        //?????????????????????????????????????????????
        intent.addCategory(Constants.MainAppCreate);
        if (!isRun) {
            //?????????????????????????????????????????????
            ServiceUtils.startRunService(MainActivity.this, intent);
        } else {
            //????????????????????????????????????????????????
            WeatherServiceConnect weatherServiceConnect = new WeatherServiceConnect();
            ServiceUtils.bindService(MainActivity.this, intent, weatherServiceConnect);
        }
    }

    //???????????????????????????????????????
    private void getCurrentMode(){
        int bgColor;
        if(sharedPrefUtils.getLastMode() == 0)
            bgColor = R.color.light_bg;
        else
            bgColor = R.color.night_bg;

        //??????????????????
        linMain.setBackgroundColor(getResources().getColor(bgColor));
        //?????????????????????
        ImmersionBar.with(this)
                .statusBarDarkFont(sharedPrefUtils.getLastMode() == 0)
                .statusBarColor(bgColor)
                .fitsSystemWindows(true)   //???????????????????????????????????????
                .navigationBarDarkIcon(sharedPrefUtils.getLastMode() == 0)
                .navigationBarColor(bgColor)
                .init();
    }
}