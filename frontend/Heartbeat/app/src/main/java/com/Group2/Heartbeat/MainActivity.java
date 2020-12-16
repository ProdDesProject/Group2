package com.Group2.Heartbeat;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;

import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;


public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    private final static String TAG = MainActivity.class.getSimpleName();
    private static String restMessage = "";
    PolarBleApi api;
    String DEVICE_ID = "50924E2C";  //Note: SET OWN DEVICE ID OR USE SCAN FUNCTIONALITY
    private AppBarConfiguration mAppBarConfiguration;
    private int heartRate = 0;
    private int timer;
    private boolean isConnected;
    private boolean isConnecting;
    private String userName;
    private String userEmail;
    private final Gson gson = new Gson();

    //gets static restMessage value
    public static String getRestMessage() {
        return restMessage;
    }

    //sets the static restMessage value
    public static void setRestMessage(String restMessage) {
        MainActivity.restMessage = restMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //int remove = getResources().getIdentifier("com.Group2.Heartbeat:drawable/" + StringGenerated, null, null);

        this.isConnected = false;
        this.isConnecting = false;

        api = PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.FEATURE_HR);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isConnected && !isConnecting) {
                        Snackbar.make(view, "Connecting...", Snackbar.LENGTH_LONG).show();
                        connect();
                        fab.setImageResource(android.R.drawable.ic_delete);
                    } else if (isConnected) {
                        Snackbar.make(view, "Disconnecting...", Snackbar.LENGTH_LONG).show();
                        disconnect();
                        fab.setImageResource(android.R.drawable.ic_input_add);
                    } else {
                        Log.d("MyApp", "Still connecting, refusing connect or disconnect action... ");
                    }
                } catch (Exception e) {
                    Log.d("MyApp", "Exception caught: " + e);
                }
            }
        });

        // Create the necessary variables for posting
        String URL = "http://192.168.42.21:8080";
        String PostUrl = URL + "/logs/new/log";
        RequestQueue queue2 = Volley.newRequestQueue(MainActivity.this);

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
            public void hrFeatureReady(String identifier) {
                Log.d("MyApp", "HR READY: " + identifier);
            }

            @Override
            public void hrNotificationReceived(String identifier, PolarHrData data) {
                Log.d("MyApp", "HR: " + data.hr);
                heartRate = data.hr;

                timer = timer + 1;

                if (timer >= 5) {
                    // Initialise the log to be sent
                    JSONObject json = new JSONObject();
                    LocalDateTime time = LocalDateTime.now();
                    ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
                    long epoch = time.atZone(zoneId).toEpochSecond() * 1000;
                    try {
                        json.put("epochDate", epoch);
                        json.put("heartRate", heartRate);
                        json.put("userId", 0);
                        json.put("sleepSession", 0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(json);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrl, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    queue2.add(jsonObjectRequest);
                    timer = 0;
                }
            }
        });

        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

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
        nav_header_title.setText(userName);
        TextView nav_header_subtitle = headerView.findViewById(R.id.textView);
        nav_header_subtitle.setText(userEmail);

//        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = URL + "/results/get/test";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Display the first 500 characters of the response string.
                    setRestMessage("Response is: " + response);
                    System.out.println(response);

                    NightResult nightResults = gson.fromJson(response.toString(), NightResult.class);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRestMessage("That didn't work!");
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);


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
        userName = sharedPreferences.getString(NAME, "");
        userEmail = sharedPreferences.getString(EMAIL, "");
    }

    public void connect() {
        try {
            api.connectToDevice(this.DEVICE_ID);
        } catch (Exception e) {
            Log.d("MyApp", "Exception caught: " + e);
        }
    }

    public void disconnect() {
        try {
            api.disconnectFromDevice(this.DEVICE_ID);
        } catch (Exception e) {
            Log.d("MyApp", "Exception caught: " + e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            Log.d(TAG, "bt ready");
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

