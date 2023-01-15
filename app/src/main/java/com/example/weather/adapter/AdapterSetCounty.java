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

public class AdapterSetCounty extends RecyclerView.Adapter<AdapterSetCounty.ViewHolder> {

    private List<String> countyArr;   //縣市陣列
    private OnItemClickListener listener; //點擊觸發事件

    public AdapterSetCounty(List<String> countyArr) {
        this.countyArr = countyArr;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtCounty;

        ViewHolder(View itemView) {
            super(itemView);
            txtCounty = (TextView) itemView.findViewById(R.id.txt_county);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpater_set_county, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtCounty.setText(countyArr.get(position));
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
        return countyArr.size();
    }
}
