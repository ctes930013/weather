package com.example.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.WeatherImg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 未來幾小時天氣預報佈局
 *
 */
public class AdapterHourWeather extends RecyclerView.Adapter<AdapterHourWeather.ViewHolder> {

    private WeatherFutureModel weatherFutureModel;
    private List<Map<String, String>> tempArr;   //平均溫度
    private List<String> rainProbArr;   //降雨機率
    private List<String []> phenomenonArr;   //天氣現象
    private final int predictCount = 5;    //要預測幾個

    public AdapterHourWeather(WeatherFutureModel weatherFutureModel) {
        this.weatherFutureModel = weatherFutureModel;
        tempArr = weatherFutureModel.getTempList(predictCount);
        rainProbArr = weatherFutureModel.getRainProbList(predictCount);
        phenomenonArr = weatherFutureModel.getPhenomenonList(predictCount);
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private ImageView imgWeather;
        private TextView txtDate, txtRain, txtTemp;

        ViewHolder(View itemView) {
            super(itemView);
            imgWeather = (ImageView) itemView.findViewById(R.id.img_weather);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtRain = (TextView) itemView.findViewById(R.id.txt_rain);
            txtTemp = (TextView) itemView.findViewById(R.id.txt_temp);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_hour_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 顯示天氣資訊
        Date d = DateTimeUtils.convertStringToDate(tempArr.get(position).get("start_time"));
        String date = DateTimeUtils.convertDistinctFormat("MM/dd HH:mm", d);
        holder.imgWeather.setImageResource(WeatherImg.getImgByWeather(Integer.parseInt(phenomenonArr.get(position)[1])));
        holder.txtDate.setText(date);
        holder.txtTemp.setText(tempArr.get(position).get("temp") + "°C");
        holder.txtRain.setText(rainProbArr.get(position) + "%");
    }

    @Override
    public int getItemCount() {
        return predictCount;
    }
}
