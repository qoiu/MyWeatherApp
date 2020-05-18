package com.geekbrains.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import java.util.ArrayList;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private ArrayList<WeatherListArray> weatherList=new ArrayList<>();

    public RecyclerDataAdapter(WeatherRequestRestModel body) {
        for (WeatherListArray array: body.listArray){
            if (array.getHour().equals("12")){
                weatherList.add(array);
            }
        }
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
        WeatherListArray posWeather = weatherList.get(position);
        holder.itemName.setText(posWeather.getDate());
        holder.itemIco.setImageResource(Weather.getIcoFromString(posWeather.weatherExtraData[0].main));
        holder.itemTemperature.setText(posWeather.weatherMainData.getTemperature());
        holder.itemHumidity.setText(posWeather.weatherMainData.getHumidity());
        holder.itemPressure.setText(posWeather.weatherMainData.getPressure());
        holder.itemWindSpeed.setText(posWeather.windRestModel.getWindSpeed());
        holder.itemWindDirection.setText(posWeather.windRestModel.getWindDirection());
        if (!SettingsSingleton.getInstance().isSettingHumidity()) {
            holder.hintHumidity.setVisibility(View.GONE);
            holder.itemHumidity.setVisibility(View.GONE);
        }
        if (!SettingsSingleton.getInstance().isSettingPressure()) {
            holder.hintPressure.setVisibility(View.GONE);
            holder.itemPressure.setVisibility(View.GONE);
        }
        if (!SettingsSingleton.getInstance().isSettingWnd()) {
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
