package com.example.weather.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.utils.OnItemClickListener;

import java.util.List;

public class AdapterSetCity extends RecyclerView.Adapter<AdapterSetCity.ViewHolder> {

    private List<String> cityArr;   //鄉鎮市區陣列
    private OnItemClickListener listener; //點擊觸發事件

    public AdapterSetCity(List<String> cityArr) {
        this.cityArr = cityArr;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtCity;

        ViewHolder(View itemView) {
            super(itemView);
            txtCity = (TextView) itemView.findViewById(R.id.txt_city);
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
        holder.txtCity.setText(cityArr.get(position));
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
