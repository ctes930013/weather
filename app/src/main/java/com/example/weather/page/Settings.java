package com.example.weather.page;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.BaseActivity;
import com.example.weather.R;
import com.example.weather.adapter.AdapterSettings;
import com.example.weather.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定頁面
 *
 */
public class Settings extends BaseActivity {

    private RecyclerView recyclerViewSettings;
    private AdapterSettings adapterSettings;
    private List<String> settingsArr = new ArrayList<>();
    @Override
    protected int getContentId() {
        return R.layout.settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewSettings = findViewById(R.id.recycler_view_settings);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(this));

        setTitle("設定");

        settingsArr.add("位置選擇");
        settingsArr.add("地區選擇");

        adapterSettings = new AdapterSettings(settingsArr);
        recyclerViewSettings.setAdapter(adapterSettings);
        adapterSettings.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("Main", "" + position + "被点击了！！");
            }
        });
    }
}
