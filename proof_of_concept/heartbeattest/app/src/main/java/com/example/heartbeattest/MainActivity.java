package com.example.heartbeattest;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;

public class MainActivity extends AppCompatActivity {


    private final static String TAG = MainActivity.class.getSimpleName();
    PolarBleApi api;
    String DEVICE_ID = "50924E2C";  //Note: SET OWN DEVICE ID OR USE SCAN FUNCTIONALITY
    int heartRate = 0;
    Button connectButton;
    boolean isConnected;
    boolean isConnecting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setText("Connect");
        this.isConnected = false;
        this.isConnecting = false;

        // NOTICE only FEATURE_HR is enabled, to enable more features like battery info
        // e.g. PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.FEATURE_HR |
        // PolarBleApi.FEATURE_BATTERY_INFO);
        // batteryLevelReceived callback is invoked after connection
        Log.d("MyApp", "Setting api... ");
        api = PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.FEATURE_HR);
        Log.d("MyApp", "Api set! ");

        Log.d("MyApp", "Api after creation: " + api.toString());

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
                connectButton.setClickable(true);
                connectButton.setText("Disconnect");
            }

            @Override
            public void deviceConnecting(PolarDeviceInfo polarDeviceInfo) {
                Log.d("MyApp", "CONNECTING: " + polarDeviceInfo.deviceId);
                isConnecting = true;
                connectButton.setClickable(false);
                connectButton.setText("Connecting...");
            }

            @Override
            public void deviceDisconnected(PolarDeviceInfo polarDeviceInfo) {
                Log.d("MyApp", "DISCONNECTED: " + polarDeviceInfo.deviceId);
                isConnected = false;
                isConnecting = false;
                connectButton.setClickable(false);
                connectButton.setText("Connect");
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
                heartRate = 0;
            }

            @Override
            public void polarFtpFeatureReady(String s) {
            }


        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

    public void connectClicked(View view) {
        try {
//            Log.d("MyApp", "api when pressing button: " + api.toString());
            // check connection state
            if ( this.isConnected == false && this.isConnecting == false) {
                // based on that, connect/disconnect
                api.connectToDevice(this.DEVICE_ID);
            } else if (this.isConnected == true){
                api.disconnectFromDevice(this.DEVICE_ID);
            } else {
                Log.d("MyApp", "Still connecting, refusing connect or disconnect action... ");
            }
        } catch (Exception e) {
            Log.d("MyApp", "Exception caught: " + e );
        }

    }

    //TODO: make interface current heartbeat field
}