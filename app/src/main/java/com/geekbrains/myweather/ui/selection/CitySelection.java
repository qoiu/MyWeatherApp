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
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerCityAdapter;
import com.geekbrains.myweather.SettingsSingleton;
import com.google.android.material.button.MaterialButton;

public class CitySelection extends Fragment {
    private MaterialButton btnSaveCity;
    private RecyclerView recyclerView;
    private EditText etInputCity;
    private RecyclerCityAdapter recycleAdapter;

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
        view.findViewById(R.id.btnSortTemperature).setOnClickListener(v -> {
            setRecyclerView(1);
        });
        view.findViewById(R.id.btnSortDate).setOnClickListener(v -> {
            setRecyclerView(2);
        });
        view.findViewById(R.id.btnSortName).setOnClickListener(v -> {
            setRecyclerView(3);
        });
        setRecyclerView(0);
    }

    private void setRecyclerView(int sort) {
        btnSaveCity.setEnabled(true);
        recycleAdapter = new RecyclerCityAdapter(sort);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.SetOnClickListener((view, position) -> etInputCity.setText(SettingsSingleton.getInstance().getCityName()));
    }

    private void setSaveCityName() {
        btnSaveCity.setOnClickListener(v -> {
            SettingsSingleton.getInstance().setCityName(etInputCity.getText().toString());
            MainActivity.navigate(R.id.nav_home);
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        recycleAdapter.notifyDataSetChanged();
    }
}
