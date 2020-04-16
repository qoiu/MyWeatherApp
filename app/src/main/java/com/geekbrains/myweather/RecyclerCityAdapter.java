package com.geekbrains.myweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.ViewHolder> {
    private String[] cityName;
    private OnItemClickListener itemClickListener;
    private int currentSelection;

    public RecyclerCityAdapter() {
        cityName = CityList.getSortedCityNames();
    }

    public void notifyCityChanges(){
        cityName = CityList.getSortedCityNames();
        notifyDataSetChanged();
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
        CityData city = CityList.getCity(cityName[position]);
        holder.cityNameText.setText(cityName[position]);
        holder.imgWeather.setImageResource(city.getTodayInfo().getIco());
        holder.cityTemperatureText.setText(city.getTodayInfo().getFormatedTemperature());
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
        return cityName == null ? 0 : cityName.length;
    }

    class ViewHolder extends RecyclerDataAdapter.ViewHolder {
        private TextView cityNameText, cityTemperatureText;
        private ImageView imgWeather;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            setViews(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        currentSelection = getAdapterPosition();
                        Singleton.getInstance().setCityName(cityName[currentSelection]);
                        itemClickListener.OnItemClick(itemView, getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
            });
        }

        private void setViews(View item) {
            cityNameText = item.findViewById(R.id.itemCityName);
            cityTemperatureText = item.findViewById(R.id.itemDayCityTemperature);
            imgWeather = item.findViewById(R.id.itemCityInfoIco);
        }
    }
}
