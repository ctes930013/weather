package com.example.weather.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.BaseActivity;
import com.example.weather.R;
import com.example.weather.adapter.AdapterSettings;
import com.example.weather.utils.OnItemClickListener;
import com.example.weather.utils.Route;
import com.example.weather.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定頁面
 *
 */
public class Settings extends BaseActivity {

    private SharedPrefUtils sharedPrefUtils;
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
        sharedPrefUtils = new SharedPrefUtils(this);

        setTitle("設定");

        settingsArr.add("位置選擇");
        settingsArr.add("地區選擇");

        adapterSettings = new AdapterSettings(settingsArr);
        recyclerViewSettings.setAdapter(adapterSettings);
        adapterSettings.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        showRegionModeDialog();
                        break;
                    case 1:
                        Route route = new Route(Settings.this);
                        route.toSetCounty();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //顯示位置選擇彈窗
    private void showRegionModeDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("位置選擇");
        String[] items = {"依照GPS定位", "自行選擇地區"};
        int checkedItem = 0;
        if (sharedPrefUtils.getRegionMode() == 2)
            checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //設定偏好為GPS定位
                        sharedPrefUtils.setRegionMode(1);
                        //回首頁
                        Route route = new Route(Settings.this);
                        route.toIndex();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
