package com.example.weather.page;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.BaseActivity;
import com.example.weather.R;
import com.example.weather.adapter.AdapterSetCounty;
import com.example.weather.model.CityModel;
import com.example.weather.utils.OnItemClickListener;
import com.example.weather.utils.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定縣市頁面
 *
 */
public class SetCounty extends BaseActivity {

    private RecyclerView recyclerViewSetCounty;
    private AdapterSetCounty adapterSetCounty;
    private CityModel cityModel;
    private List<String> countyArr = new ArrayList<>();
    @Override
    protected int getContentId() {
        return R.layout.set_county;
    }

    @Override
    protected String getPageTitle() {
        return "縣市設定";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewSetCounty = findViewById(R.id.recycler_view_set_county);
        recyclerViewSetCounty.setLayoutManager(new LinearLayoutManager(this));
        cityModel = new CityModel();

        countyArr = cityModel.getAllCounty();

        adapterSetCounty = new AdapterSetCounty(countyArr);
        recyclerViewSetCounty.setAdapter(adapterSetCounty);
        adapterSetCounty.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Route route = new Route(SetCounty.this);
                route.toSetCity(countyArr.get(position));
            }
        });
    }
}
