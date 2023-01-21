package com.example.weather.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    protected String getPageTitle() {
        return "設定";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerViewSettings = findViewById(R.id.recycler_view_settings);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(this));
        sharedPrefUtils = new SharedPrefUtils(this);

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
                        toSetCounty();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //顯示位置選擇彈窗
    AlertDialog alert;
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
                Route route = new Route(Settings.this);
                switch (which) {
                    case 0:
                        alert.dismiss();
                        //設定偏好為GPS定位
                        sharedPrefUtils.setRegionMode(1);
                        //回首頁
                        route.toIndex();
                        break;
                    case 1:
                        alert.dismiss();
                        if("".equals(sharedPrefUtils.getRegionCounty()) || "".equals(sharedPrefUtils.getRegionCity())){
                            //代表還沒設定過地區
                            Toast.makeText(Settings.this, "當前尚未設定過區域，請先設定區域", Toast.LENGTH_SHORT).show();
                            toSetCounty();
                        }else{
                            //代表已經設定過地區
                            //設定偏好為手動選取地區
                            sharedPrefUtils.setRegionMode(2);
                            //回首頁
                            route.toIndex();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    //跳轉至選擇縣市頁面
    private void toSetCounty(){
        Route route = new Route(Settings.this);
        route.toSetCounty();
    }
}
