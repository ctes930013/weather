package com.example.weather;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

public abstract class BaseActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        imgBack = findViewById(R.id.img_back);
        txtTitle = findViewById(R.id.txt_title);

        //設定狀態欄顏色
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)   //避免畫面上方和通知欄重疊到
                .navigationBarDarkIcon(true)
                .navigationBarColor(R.color.white)
                .init();

        //返回上一頁
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //取得layout
    @LayoutRes
    protected abstract int getContentId();

    //設定標題
    protected void setTitle(String title) {
        txtTitle.setText(title);
    }
}
