package com.geekbrains.myweather;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private static AlertDialog alert;
    public static final int CITY_LENGTH_BASE= 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        createAlertDialog();
        importCityInfo();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cities, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.navigate(R.id.nav_cities);
    }

    public static void showAlert(){
        alert.show();
    }

    private void importCityInfo() {
        String[] cityList = getResources().getStringArray(R.array.cityList);
        for (String s : cityList) {
            WeatherDataLoader.addCityFromWeatherApi(s);
        }
    }

    public void goHome() {
        navController.navigate(R.id.nav_home);
    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // в билдере указываем заголовок окна (можно указывать как ресурс, так и строку)
        builder.setTitle(R.string.title_dialog)
                // указываем сообщение в окне (также есть вариант со строковым параметром)
                .setMessage(R.string.city_not_found)
                // можно указать и пиктограмму
                .setIcon(R.mipmap.ic_launcher_round)
                // из этого окна нельзя выйти кнопкой back
                .setCancelable(false)
                // устанавливаем кнопку (название кнопки также можно задавать строкой)
                .setPositiveButton(R.string.yes,
                        // Ставим слушатель, нажатие будем обрабатывать
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                navController.navigate(R.id.nav_cities);
                            }
                        });
        alert = builder.create();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        final SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    Singleton.getInstance().setCityName(query);
                    Singleton.getInstance().setCity(new CityData(Singleton.getInstance().getCityName()));
                    WeatherDataLoader.addCityFromWeatherApi(query);
                    navController.navigate(R.id.nav_home);
                    searchText.setQuery("", false);
                    searchText.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}