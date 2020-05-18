package com.geekbrains.myweather.ui.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekbrains.myweather.R;
import com.geekbrains.myweather.SettingsSingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends Fragment {

    private SwitchMaterial swNight, swPressure, swWind, swHumidity;
    private MaterialRadioButton rbCelsius, rbFahrenheit;
    private MaterialButton btnSaveSettings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupElements(view);
        setSettingsSaveBtn();
        setElements();
    }

    private void setSettingsSaveBtn() {
        btnSaveSettings.setOnClickListener(v -> {
            saveElementsState();
            getActivity().onBackPressed();
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
        swNight.setChecked(SettingsSingleton.getInstance().isSettingNightMode());
        swWind.setChecked(SettingsSingleton.getInstance().isSettingWnd());
        swPressure.setChecked(SettingsSingleton.getInstance().isSettingPressure());
        swHumidity.setChecked(SettingsSingleton.getInstance().isSettingHumidity());
        rbCelsius.setChecked(!SettingsSingleton.getInstance().isSettingInFahrenheit());
        rbFahrenheit.setChecked(SettingsSingleton.getInstance().isSettingInFahrenheit());
        rbCelsius.setChecked(!SettingsSingleton.getInstance().isSettingInFahrenheit());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveElementsState();
    }

    private void saveElementsState() {
        SettingsSingleton.getInstance().setSettingNightMode(swNight.isChecked());
        SettingsSingleton.getInstance().setSettingPressure(swPressure.isChecked());
        SettingsSingleton.getInstance().setSettingWnd(swWind.isChecked());
        SettingsSingleton.getInstance().setSettingHumidity(swHumidity.isChecked());
        SettingsSingleton.getInstance().setSettingInFahrenheit(!rbCelsius.isChecked());
        SharedPreferences defaultPrefs =
                PreferenceManager.getDefaultSharedPreferences(requireActivity().getApplicationContext());
        savePreference(defaultPrefs);
    }

    private void savePreference(SharedPreferences sharedPreferences) {
        String[] keys = {"night", "pressure", "wind", "humidity", "celsius"};
        boolean[] values = {
                SettingsSingleton.getInstance().isSettingNightMode(),
                SettingsSingleton.getInstance().isSettingPressure(),
                SettingsSingleton.getInstance().isSettingWnd(),
                SettingsSingleton.getInstance().isSettingHumidity(),
                SettingsSingleton.getInstance().isSettingInFahrenheit()
        };

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putBoolean(keys[i], values[i]);
        }
        editor.apply();

    }
}
