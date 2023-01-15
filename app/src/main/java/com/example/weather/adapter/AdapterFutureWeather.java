package com.example.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.WeatherImg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 未來幾天天氣預報佈局
 *
 */
public class AdapterFutureWeather extends RecyclerView.Adapter<AdapterFutureWeather.ViewHolder> {

    private WeatherFutureModel weatherFutureModel;
    private List<String> maxTempArr;   //最高溫度
    private List<String> minTempArr;   //最低溫度
    private List<Integer> rainProbArr;   //降雨機率
    private List<String> phenomenonArr;   //天氣現象
    private final int predictDay = 7;    //要預測幾天

    public AdapterFutureWeather(WeatherFutureModel weatherFutureModel) {
        this.weatherFutureModel = weatherFutureModel;
        this.maxTempArr = new ArrayList<>();
        this.minTempArr = new ArrayList<>();
        this.rainProbArr = new ArrayList<>();
        this.phenomenonArr = new ArrayList<>();
        for (int i = 0; i < predictDay; i++) {
            String date = DateTimeUtils.getAddDistinctDate(i + 1);
            String maxTemp = weatherFutureModel.getMaxMinTempByDate(DateTimeUtils.convertStringToDate(date))[0];
            String minTemp = weatherFutureModel.getMaxMinTempByDate(DateTimeUtils.convertStringToDate(date))[1];
            maxTempArr.add(maxTemp);
            minTempArr.add(minTemp);
            String phenomenonValue = weatherFutureModel.getPhenomenonByDate(DateTimeUtils.convertStringToDate(date))[1];
            phenomenonArr.add(phenomenonValue);
            rainProbArr.add(weatherFutureModel.getMaxRainProbByDate(DateTimeUtils.convertStringToDate(date)));
        }
    }

    // 建立ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private ImageView imgWeather;
        private TextView txtDate, txtRain, txtTempRange;

        ViewHolder(View itemView) {
            super(itemView);
            imgWeather = (ImageView) itemView.findViewById(R.id.img_weather);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtRain = (TextView) itemView.findViewById(R.id.txt_rain);
            txtTempRange = (TextView) itemView.findViewById(R.id.txt_temp_range);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_future_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 顯示天氣資訊
        Date d = DateTimeUtils.convertStringToDate(DateTimeUtils.getAddDistinctDate(position + 1));
        String date = DateTimeUtils.convertDistinctFormat("MM/dd", d);
        holder.imgWeather.setImageResource(WeatherImg.getImgByWeather(Integer.parseInt(phenomenonArr.get(position))));
        holder.txtDate.setText(date
                + " (" + DateTimeUtils.getWeekOfDate(d) + ")");
        holder.txtTempRange.setText(maxTempArr.get(position) + "°C/" + minTempArr.get(position) + "°C");
        holder.txtRain.setText(rainProbArr.get(position) + "%");
    }

    @Override
    public int getItemCount() {
        // 設定在未來一週
        return predictDay;
    }
}
