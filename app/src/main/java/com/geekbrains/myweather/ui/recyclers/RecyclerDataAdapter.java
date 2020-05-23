package com.geekbrains.myweather.ui.recyclers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.AppSettings;
import com.geekbrains.myweather.Converter;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private List<WeatherInfo> weatherList;

    public RecyclerDataAdapter() {
        weatherList=App.getInstance().getWeatherDao().getForecast(AppSettings.get().getCityName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_info, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherInfo posWeather =weatherList.get(position);
        holder.itemName.setText(Converter.convertDateToString(posWeather.date));
        if(position==0){
            holder.itemName.setText("Сейчас");
            holder.itemName.setTextSize(16);
        }
        holder.itemIco.setImageResource(Weather.getIcoFromString(posWeather.clouds));
        holder.itemTemperature.setText(posWeather.getTemperatureString());
        holder.itemHumidity.setText(String.valueOf(posWeather.humidity));
        holder.itemPressure.setText(String.valueOf(posWeather.pressure));
        holder.itemWindSpeed.setText(posWeather.getWindSpeed());
        holder.itemWindDirection.setText(posWeather.getWindDirection());
        if (!AppSettings.get().isSettingHumidity()) {
            holder.hintHumidity.setVisibility(View.GONE);
            holder.itemHumidity.setVisibility(View.GONE);
        }
        if (!AppSettings.get().isSettingPressure()) {
            holder.hintPressure.setVisibility(View.GONE);
            holder.itemPressure.setVisibility(View.GONE);
        }
        if (!AppSettings.get().isSettingWnd()) {
            holder.itemWindDirection.setVisibility(View.GONE);
            holder.itemWindSpeed.setVisibility(View.GONE);
            holder.hintWind.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return weatherList == null ? 0 : weatherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemTemperature, itemPressure, itemHumidity, itemWindSpeed, itemWindDirection;
        TextView hintHumidity, hintPressure, hintWind;
        ImageView itemIco;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            itemName = view.findViewById(R.id.itemDayInfoName);
            itemIco = view.findViewById(R.id.itemDayInfoIco);
            itemTemperature = view.findViewById(R.id.itemDayInfoTemperature);
            itemPressure = view.findViewById(R.id.itemDayInfoPressure);
            itemHumidity = view.findViewById(R.id.itemDayInfoHumidity);
            itemWindSpeed = view.findViewById(R.id.itemDayInfoWind);
            itemWindDirection = view.findViewById(R.id.itemDayInfoWindDirection);
            hintHumidity = view.findViewById(R.id.itemDayInfoHintHumidity);
            hintWind = view.findViewById(R.id.itemDayInfoHintWind);
            hintPressure = view.findViewById(R.id.itemDayInfoHintPressure);
        }
    }
}
