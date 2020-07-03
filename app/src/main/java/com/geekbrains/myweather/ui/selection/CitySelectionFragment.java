package com.geekbrains.myweather.ui.selection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.ui.recyclers.RecyclerCityAdapter;
import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CitySelectionFragment extends Fragment {
    private MaterialButton btnSaveCity;
    private RecyclerView recyclerView;
    private EditText etInputCity;
    private RecyclerCityAdapter recycleAdapter;
    private boolean[] sortOrder = new boolean[4];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        setSaveCityName();
    }

    private void setView(View view) {
        recyclerView = view.findViewById(R.id.recyclerCityInfo);
        btnSaveCity = view.findViewById(R.id.btnSaveCity);
        etInputCity = view.findViewById(R.id.inputText);
        btnSaveCity.setEnabled(false);
        view.findViewById(R.id.btnSortTemperature).setOnClickListener(v -> sort(1)
        );
        view.findViewById(R.id.btnSortDate).setOnClickListener(v -> sort(2)
        );
        view.findViewById(R.id.btnSortName).setOnClickListener(v -> sort(3));
        sortOrder[3]=true;
        setRecyclerView(3);
    }

    private void sort(int id) {
        sortOrder[id] = !sortOrder[id];
        setRecyclerView(id);
        updateRecyclerView(getCities(id));
    }

    private void setRecyclerView(int id) {
        btnSaveCity.setEnabled(true);
        recycleAdapter = new RecyclerCityAdapter();
        updateRecyclerView(getCities(id));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.SetOnClickListener((view, position) -> etInputCity.setText(AppSettings.get().getCityName()));
    }

    private void setSaveCityName() {
        btnSaveCity.setOnClickListener(v -> MainActivity.showMainFragment(etInputCity.getText().toString()));
    }

    private List<WeatherInfo> getCities(int id) {
        List<WeatherInfo> cities;
        long date=AppSettings.get().getToday();
        switch (id) {
            case 1:
                cities = App.getInstance().getWeatherDao().getSortedTemperature(sortOrder[id],date);
                break;
            case 2:
                cities = App.getInstance().getWeatherDao().getSortedDate(sortOrder[id],date);
                break;
            case 3:
                cities = App.getInstance().getWeatherDao().getSortedCityName(sortOrder[id],date);
                break;
            default:
                cities = App.getInstance().getWeatherDao().getAllCities();
                break;
        }
        return cities;
    }

    private void updateRecyclerView(List<WeatherInfo> cities) {
        recycleAdapter.changeCities(cities);
        recycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        recycleAdapter.notifyDataSetChanged();
    }
}
