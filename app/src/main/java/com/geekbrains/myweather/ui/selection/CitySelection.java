package com.geekbrains.myweather.ui.selection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.geekbrains.myweather.CityData;
import com.geekbrains.myweather.CityList;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerCityAdapter;
import com.geekbrains.myweather.Singleton;
import com.geekbrains.myweather.WeatherDataLoader;
import com.google.android.material.button.MaterialButton;
import java.util.Objects;

public class CitySelection extends Fragment {
    private MaterialButton btnSaveCity;
    private RecyclerView recyclerView;
    private EditText etInputCity;
    private TextView textInformation;
    private ProgressBar progressBar;
    private RecyclerCityAdapter recycleAdapter;
    private static Handler handler = new Handler();

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
        if (CityList.getLength() > 0) {
            setRecyclerView();
        } else {
            resetRecView();
        }
    }

    private void resetRecView() {
        Thread t=new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.w("CitySelection ", e);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("CitySelection",recycleAdapter.getItemCount() + "<" + CityList.getLength());
                        if(recycleAdapter.getItemCount() < 12){
                            if (recycleAdapter.getItemCount() < CityList.getLength()) {
                                if(recycleAdapter.getItemCount()==0)setRecyclerView();
                                recycleAdapter.notifyCityChanges();
                            }
                            resetRecView();
                        }else {
                            recycleAdapter.notifyCityChanges();
                        }
                    }
                });
            }
        };
        t.start();
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
                Singleton.getInstance().setCity(new CityData(Singleton.getInstance().getCityName()));
                WeatherDataLoader.addCityFromWeatherApi(etInputCity.getText().toString());
                ((MainActivity) Objects.requireNonNull(getActivity())).goHome();
            }
        });
    }
}
