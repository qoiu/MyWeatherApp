package com.geekbrains.myweather.ui.selection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerCityAdapter;
import com.geekbrains.myweather.Singleton;
import com.geekbrains.myweather.WeatherDataService;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class CitySelection extends Fragment {
    private MaterialButton btnSaveCity;
    private RecyclerView recyclerView;
    private EditText etInputCity;
    private TextView textInformation;
    private ProgressBar progressBar;
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
        progressBar = view.findViewById(R.id.progressBar);
        textInformation = view.findViewById(R.id.textInformation);
        recycleAdapter = new RecyclerCityAdapter();
        btnSaveCity.setEnabled(false);
        setRecyclerView();
    }

    private void setRecyclerView() {
        btnSaveCity.setEnabled(true);
        textInformation.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.SetOnClickListener(new RecyclerCityAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                etInputCity.setText(Singleton.getInstance().getCityName());
            }
        });
    }

    private void setSaveCityName() {
        btnSaveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setCityName(etInputCity.getText().toString());
                WeatherDataService.startWeatherDataService(getContext(), etInputCity.getText().toString());
                ((MainActivity) Objects.requireNonNull(getActivity())).goHome();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(dataLoadedReceiver, new IntentFilter(MainActivity.BROADCAST_ACTION_CITY_LOADED));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(dataLoadedReceiver);
    }

    private BroadcastReceiver dataLoadedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (recycleAdapter.getItemCount() == 0) setRecyclerView();
            recycleAdapter.notifyCityChanges();
        }
    };
}
