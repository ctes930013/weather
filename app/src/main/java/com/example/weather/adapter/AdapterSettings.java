package com.example.weather.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.utils.OnItemClickListener;

import java.util.List;

public class AdapterSettings extends RecyclerView.Adapter<AdapterSettings.ViewHolder> {

    private List<String> settingsArr;   //設定
    private OnItemClickListener listener; //點擊觸發事件

    public AdapterSettings(List<String> settingsArr) {
        this.settingsArr = settingsArr;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView txtSettings;
        private RelativeLayout viewSettings;

        ViewHolder(View itemView) {
            super(itemView);
            txtSettings = (TextView) itemView.findViewById(R.id.txt_settings);
            viewSettings = (RelativeLayout) itemView.findViewById(R.id.view_settings);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtSettings.setText(settingsArr.get(position));
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
        return settingsArr.size();
    }
}
