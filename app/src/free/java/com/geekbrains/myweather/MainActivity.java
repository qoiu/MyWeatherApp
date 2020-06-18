package com.geekbrains.myweather;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    @SuppressLint("StaticFieldLeak")
    private static NavController navController;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;

    public static void showMainFragment(String query) {
        WeatherInfo weather = App.getInstance().getWeatherDao().getWeather(query, AppSettings.get().getToday());
        if (weather != null) {
            AppSettings.get().setLocationInLatLng(new LatLng(
                    weather.latitude,
                    weather.longitude
            ));
        } else {
            AppSettings.get().setLocation(null);
        }
        AppSettings.get().setCityName(query);
        navigate(R.id.nav_home);
    }

    public static void navigate(int id) {
        navController.navigate(id);
    }

    private BroadcastReceiver messageReceiver = new EventReceiver();
    private AppBarConfiguration mAppBarConfiguration;
    private final String CONNECTIVITY_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
    private final String BATTERY_ACTION = "android.intent.action.BATTERY_LOW";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationModule.getInstance().setLocManager((LocationManager) getSystemService(LOCATION_SERVICE));
        LocationModule.getInstance().setFromActivity(this);
        initNotificationChannel();
        initGetToken();
        googleAuth();
        registerReceiver(messageReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        registerReceiver(messageReceiver, new IntentFilter(BATTERY_ACTION));
        initMenu();
        final SharedPreferences defaultPrefs =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loadPrefs(defaultPrefs);
    }

    private void googleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean permissionsGranted = true;
            for (int grantResult : grantResults) {
                Log.w("Permission", "succes");
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }
            if (permissionsGranted) recreate();
        }
    }

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Firebase", "getInstanceId failed", task.getException());
                    }
                });
    }

    private void initMenu() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppSettings.get().setLocalization(getResources().getString(R.string.lang));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cities, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void loadPrefs(SharedPreferences sharedPreferences) {
        String[] keys = {"night", "pressure", "wind", "humidity", "celsius"};
        AppSettings.get().setSettingNightMode(sharedPreferences.getBoolean(keys[0], false));
        AppSettings.get().setSettingPressure(sharedPreferences.getBoolean(keys[1], false));
        AppSettings.get().setSettingWnd(sharedPreferences.getBoolean(keys[2], false));
        AppSettings.get().setSettingHumidity(sharedPreferences.getBoolean(keys[3], false));
        AppSettings.get().setSettingInFahrenheit(sharedPreferences.getBoolean(keys[4], false));
    }


    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context,
                             @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        if (BuildConfig.DEBUG) {
            MenuItem cheat = menu.findItem(R.id.cheat_btn);
            cheat.setVisible(true);
            cheat.setOnMenuItemClickListener((v) -> {
                Toast.makeText(this, "Some usefull stuff", Toast.LENGTH_SHORT).show();
                return true;
            });
        }
        final SearchView searchText = (SearchView) search.getActionView();
        initAuthGoogleBtn();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    showMainFragment(query);
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


    private void initAuthGoogleBtn() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        TextView logOff = findViewById(R.id.headerLogOffField);
        logOff.setOnClickListener(v -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, task -> {
                        updateUI(null);
                    });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("MainActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        TextView email = findViewById(R.id.headerEmailField);
        TextView name = findViewById(R.id.headerNameField);
        TextView logOff = findViewById(R.id.headerLogOffField);
        ImageView img = findViewById(R.id.headerImgField);
        if (account != null && !account.isExpired()) {
            Toast.makeText(getApplicationContext(), "Thank you for authorization", Toast.LENGTH_SHORT).show();
            signInButton.setVisibility(View.GONE);
            name.setText(account.getDisplayName());
            img.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(account.getPhotoUrl().toString())
                    .transform(new CircleTransformation())
                    .placeholder(R.mipmap.anonymous)
                    .into(img);
            email.setVisibility(View.VISIBLE);
            email.setText(account.getEmail());
            logOff.setVisibility(View.VISIBLE);
        } else {
            logOff.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            name.setText(getResources().getString(R.string.nav_header_title));
            email.setVisibility(View.GONE);
        }
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
