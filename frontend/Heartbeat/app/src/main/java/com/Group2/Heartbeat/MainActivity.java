package com.Group2.Heartbeat;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private final static String TAG = MainActivity.class.getSimpleName();
    PolarBleApi api;
    String DEVICE_ID = "50924E2C";  //Note: SET OWN DEVICE ID OR USE SCAN FUNCTIONALITY
    int heartRate = 0;

    boolean isConnected;
    boolean isConnecting;

    String username;
    String useremail;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.FEATURE_HR);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Connecting...", Snackbar.LENGTH_LONG).show();
                connect();
            }
        });

        api.setApiCallback(new PolarBleApiCallback() {
            @Override
            public void blePowerStateChanged(boolean powered) {
                Log.d("MyApp", "BLE power: " + powered);
            }

            @Override
            public void deviceConnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d("MyApp", "CONNECTED: " + polarDeviceInfo.deviceId);
                isConnected = true;
                isConnecting = false;
            }

            @Override
            public void deviceConnecting(PolarDeviceInfo polarDeviceInfo) {
                Log.d("MyApp", "CONNECTING: " + polarDeviceInfo.deviceId);
                isConnected = false;
                isConnecting = true;
            }

            @Override
            public void deviceDisconnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d("MyApp", "DISCONNECTED: " + polarDeviceInfo.deviceId);
                isConnected = false;
                isConnecting = false;
            }

            @Override
            public void ecgFeatureReady(String identifier) {
            }

            @Override
            public void accelerometerFeatureReady(String identifier) {
            }

            @Override
            public void ppgFeatureReady(String identifier) {
            }

            @Override
            public void ppiFeatureReady(String identifier) {
            }

            @Override
            public void biozFeatureReady(String identifier) {
            }

            @Override
            public void hrFeatureReady(String identifier) {
                Log.d("MyApp", "HR READY: " + identifier);
            }

            @Override
            public void batteryLevelReceived(String identifier, int level) {
            }

            @Override
            public void hrNotificationReceived(String identifier, PolarHrData data) {
                Log.d("MyApp", "HR: " + data.hr);
                heartRate = data.hr;

            }

            @Override
            public void polarFtpFeatureReady(String s) {
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        loadData();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_test)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        TextView nav_header_title = headerView.findViewById(R.id.nav_header_title);
        nav_header_title.setText(username);
        TextView nav_header_subtitle = headerView.findViewById(R.id.textView);
        nav_header_subtitle.setText(useremail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(NAME, "");
        useremail = sharedPreferences.getString(EMAIL, "");
    }

    public void connect() {
        try{
            api.connectToDevice(this.DEVICE_ID);
        } catch(Exception e) {
            Log.d("MyApp", "Exception caught: " + e );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == 1) {
            Log.d(TAG,"bt ready");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            api.backgroundEntered();
        } catch (Exception e) {
            Log.d("MyApp", "Error: " + e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MyApp", "Back to foreground!");
        try {
            api.foregroundEntered();
        } catch (Exception e) {
            Log.d("MyApp", "Error: " + e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        api.shutDown();
    }

}

