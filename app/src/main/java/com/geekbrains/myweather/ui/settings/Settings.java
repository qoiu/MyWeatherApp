package com.geekbrains.myweather.ui.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.Singleton;
import com.geekbrains.myweather.ui.main.MainFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends Fragment {

    private SwitchMaterial swNight, swPressure, swWind, swHumidity;
    private MaterialRadioButton rbCelsius, rbFahrenheit;
    private MaterialButton btnSaveSettings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupElements(view);
        setSettingsSaveBtn();
        setElements();
    }

    private void setSettingsSaveBtn() {
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveElementsState();
                ((MainActivity)getActivity()).goHome();
            }
        });
    }

    private void setupElements(View view) {
        swNight = view.findViewById(R.id.switchNightMode);
        swPressure = view.findViewById(R.id.switchPressure);
        swWind = view.findViewById(R.id.switchWind);
        swHumidity = view.findViewById(R.id.switchHumidity);
        rbCelsius = view.findViewById(R.id.radioBtnCels);
        rbFahrenheit = view.findViewById(R.id.radioBtnFahr);
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setElements();
    }

    private void setElements() {
        swNight.setChecked(Singleton.getInstance().isSettingNightMode());
        swWind.setChecked(Singleton.getInstance().isSettingWnd());
        swPressure.setChecked(Singleton.getInstance().isSettingPressure());
        swHumidity.setChecked(Singleton.getInstance().isSettingHumidity());
        rbCelsius.setChecked(Singleton.getInstance().isSettingInCelsius());
        rbFahrenheit.setChecked(!Singleton.getInstance().isSettingInCelsius());
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveElementsState();
    }

    private void saveElementsState() {
        Singleton.getInstance().setSettingNightMode(swNight.isChecked());
        Singleton.getInstance().setSettingPressure(swPressure.isChecked());
        Singleton.getInstance().setSettingWnd(swWind.isChecked());
        Singleton.getInstance().setSettingHumidity(swHumidity.isChecked());
        Singleton.getInstance().setSettingInCelsius(rbCelsius.isChecked());
    }
}
