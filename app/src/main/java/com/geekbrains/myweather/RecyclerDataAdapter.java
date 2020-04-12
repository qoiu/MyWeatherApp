package com.geekbrains.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private CityData city;
    private WeatherInfo[] weatherList;

    public RecyclerDataAdapter(CityData city) {
        this.city = city;
        weatherList = city.getWeatherListByDay();
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
        holder.itemName.setText(weatherList[position].getDate());
        holder.itemIco.setImageResource(city.getForecastInfo(position).getIco());
        holder.itemTemperature.setText(city.getForecastInfo(position).getTemperature());
        holder.itemHumidity.setText(city.getForecastInfo(position).getHumidity());
        holder.itemPressure.setText(city.getForecastInfo(position).getPressure());
        holder.itemWindSpeed.setText(city.getForecastInfo(position).getWindSpeed());
        holder.itemWindDirection.setText(city.getForecastInfo(position).getWindDirection());
        if (!Singleton.getInstance().isSettingHumidity()) {
            holder.hintHumidity.setVisibility(View.GONE);
            holder.itemHumidity.setVisibility(View.GONE);
        }
        if (!Singleton.getInstance().isSettingPressure()) {
            holder.hintPressure.setVisibility(View.GONE);
            holder.itemPressure.setVisibility(View.GONE);
        }
        if (!Singleton.getInstance().isSettingWnd()) {
            holder.itemWindDirection.setVisibility(View.GONE);
            holder.itemWindSpeed.setVisibility(View.GONE);
            holder.hintWind.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return weatherList == null ? 0 : weatherList.length;
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
            itemWindDirection = view.findViewById(R.id.itemDayInfoWindDirrection);
            hintHumidity = view.findViewById(R.id.itemDayInfoHintHumidity);
            hintWind = view.findViewById(R.id.itemDayInfoHintWind);
            hintPressure = view.findViewById(R.id.itemDayInfoHintPressure);
        }
    }
}
