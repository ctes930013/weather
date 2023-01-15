package com.example.weather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.utils.OnItemClickListener;
import com.example.weather.utils.SharedPrefUtils;

import java.util.List;

public class AdapterSetCity extends RecyclerView.Adapter<AdapterSetCity.ViewHolder> {

    private Context context;
    private SharedPrefUtils sharedPrefUtils;
    private List<String> cityArr;   //鄉鎮市區陣列
    private OnItemClickListener listener; //點擊觸發事件

    public AdapterSetCity(Context context, List<String> cityArr) {
        this.context = context;
        this.cityArr = cityArr;
        sharedPrefUtils = new SharedPrefUtils(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtCity;
        private ImageView imgMark;

        ViewHolder(View itemView) {
            super(itemView);
            txtCity = (TextView) itemView.findViewById(R.id.txt_city);
            imgMark = (ImageView) itemView.findViewById(R.id.img_mark);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_set_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String city = cityArr.get(position);
        holder.txtCity.setText(city);
        //比較該地區是否為上一次設定的地區
        if(!city.equals(sharedPrefUtils.getRegionCity())){
            holder.imgMark.setVisibility(View.GONE);
        }else{
            holder.imgMark.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityArr.size();
    }
}
