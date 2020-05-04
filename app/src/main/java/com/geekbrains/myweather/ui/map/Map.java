package com.geekbrains.myweather.ui.map;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.LocationDataAdapter;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.SettingsSingleton;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class Map extends Fragment implements OnMapReadyCallback {

    private MapView mapView;

    public Map() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView= view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng maltLng;
        if(SettingsSingleton.getInstance().getLocation()==null){
            if(LocationDataAdapter.getLocation()!=null){
                maltLng=new LatLng(
                        LocationDataAdapter.getLocation().getLatitude(),
                        LocationDataAdapter.getLocation().getLongitude());
            }else {
                maltLng=new LatLng(55.752830, 37.617257);
            }
        }else{
            maltLng=new LatLng(
                    SettingsSingleton.getInstance().getLocation().getLatitude(),
                    SettingsSingleton.getInstance().getLocation().getLongitude());
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(maltLng, 10);
        List<WeatherInfo> cityList=App.getInstance().getWeatherDao().getAllCities();
        for(WeatherInfo city:cityList){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(city.latitude,city.longitude))
                    .snippet(city.getTemperature())
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(Weather.getIcoFromString(city.clouds)))
                    .title(city.cityName))
                    .showInfoWindow();
        }
        googleMap.animateCamera(cameraUpdate);
        googleMap.setOnMapClickListener(latLng -> {
            Location location=new Location("convert");
            location.setLongitude(latLng.longitude);
            location.setLatitude(latLng.latitude);
            if(LocationDataAdapter.getCityByLoc(location)==null ||
                    LocationDataAdapter.getCityByLoc(location).equals("MSG_NO_DATA")){
                Toast.makeText(requireActivity().getBaseContext(),
                        "There are no city nearby",Toast.LENGTH_SHORT).show();
            }else{
                SettingsSingleton.getInstance().setCityName("");
                SettingsSingleton.getInstance().setLocation(location);
                MainActivity.navigate(R.id.nav_home);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
