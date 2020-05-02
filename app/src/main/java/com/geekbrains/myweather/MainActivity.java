package com.geekbrains.myweather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import java.util.Arrays;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    public static final String BROADCAST_ACTION_CITY_LOADED = "com.geekbrains.myweather.cityDataChanged";
    private BroadcastReceiver messageReceiver=new EventReceiver();
    private AppBarConfiguration mAppBarConfiguration;
    private static NavController navController;
    private static HashSet<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNotificationChannel();
        initGetToken();
        registerReceiver(messageReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(messageReceiver, new IntentFilter("android.intent.action.BATTERY_LOW"));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        cityList = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.cityList)));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final SharedPreferences defaultPrefs =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadPrefs(defaultPrefs);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cities, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                    }
                });
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void loadPrefs(SharedPreferences sharedPreferences) {
        String[] keys = {"night", "pressure", "wind", "humidity", "celsius"};
        SettingsSingleton.getInstance().setSettingNightMode(sharedPreferences.getBoolean(keys[0], false));
        SettingsSingleton.getInstance().setSettingPressure(sharedPreferences.getBoolean(keys[1], false));
        SettingsSingleton.getInstance().setSettingWnd(sharedPreferences.getBoolean(keys[2], false));
        SettingsSingleton.getInstance().setSettingHumidity(sharedPreferences.getBoolean(keys[3], false));
        SettingsSingleton.getInstance().setSettingInFahrenheit(sharedPreferences.getBoolean(keys[4], false));
    }

    public static void navigate(int id) {
        navController.navigate(id);
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
                    SettingsSingleton.getInstance().setCityName(query);
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
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(item -> {
            navigate(R.id.nav_settings);
            return false;
        });
        return true;
    }

    public static void addCityName(String cityName) {
        cityList.add(cityName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }
}