package com.geekbrains.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.List;


public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.ViewHolder> {
    private OnItemClickListener itemClickListener;
    private int currentSelection;
    private List<WeatherInfo> cities;

    public RecyclerCityAdapter(int sort) {
        switch (sort){
            case 0:
                cities= App.getInstance().getWeatherDao().getAllCities();
                break;
            case 1:
                cities= App.getInstance().getWeatherDao().getSortedTemperature();
                break;
            case 2:
                cities= App.getInstance().getWeatherDao().getSortedDate();
                break;
            case 3:
                cities= App.getInstance().getWeatherDao().getSortedCityName();
                break;
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void SetOnClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityNameText.setText(cities.get(position).cityName);
        holder.cityTemperature.setText(cities.get(position).getTemperature());
        holder.cityWeatherImg.setImageResource(Weather.getIcoFromString(cities.get(position).clouds));
        holder.cityDate.setText(Weather.convertDateToString(cities.get(position).date));
        highlightSelectedPosition(holder, position);
    }

    private void highlightSelectedPosition(ViewHolder holder, final int position) {
        if (currentSelection == position) {
            int color = holder.itemView.getResources().getColor(R.color.colorAccent);
            holder.itemView.setBackgroundColor(color);
        } else {
            int color = holder.itemView.getResources().getColor(android.R.color.transparent);
            holder.itemView.setBackgroundColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    class ViewHolder extends RecyclerDataAdapter.ViewHolder {
        private TextView cityNameText,cityTemperature,cityDate;
        private ImageView cityWeatherImg;
        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            setViews(itemView);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    currentSelection = getAdapterPosition();
                    SettingsSingleton.getInstance().setCityName(cities.get(currentSelection).cityName);
                    itemClickListener.OnItemClick(itemView, getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        private void setViews(View item) {
            cityDate=item.findViewById(R.id.itemCityInfoDate);
            cityNameText = item.findViewById(R.id.itemCityName);
            cityTemperature=item.findViewById(R.id.itemCityInfoTemperature);
            cityWeatherImg=item.findViewById(R.id.itemCityInfoIco);
        }
    }
}
