package com.example.weather.page;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.BaseActivity;
import com.example.weather.R;
import com.example.weather.adapter.AdapterSetCity;
import com.example.weather.model.CityModel;
import com.example.weather.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定鄉鎮市區頁面
 *
 */
public class SetCity extends BaseActivity {

    private RecyclerView recyclerViewSetCity;
    private AdapterSetCity adapterSetCity;
    private String county;   //紀錄剛剛選取得縣市
    private CityModel cityModel;
    private List<String> cityArr = new ArrayList<>();
    @Override
    protected int getContentId() {
        return R.layout.set_city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewSetCity = findViewById(R.id.recycler_view_set_city);
        recyclerViewSetCity.setLayoutManager(new LinearLayoutManager(this));
        cityModel = new CityModel();

        county = getIntent().getExtras().getString("county");

        setTitle(county);

        cityArr = cityModel.getAllCityByCounty(county);

        adapterSetCity = new AdapterSetCity(cityArr);
        recyclerViewSetCity.setAdapter(adapterSetCity);
        adapterSetCity.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }
}
