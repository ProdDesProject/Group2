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

import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;

import java.time.LocalDateTime;


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

    private Gson gson;

    private static String restMessage = "";

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
                    // Log.d("MyApp", "api when pressing button: " + api.toString());
                    // check connection state
                    if (!isConnected && !isConnecting) {
                        Snackbar.make(view, "Connecting...", Snackbar.LENGTH_LONG).show();
                        connect();
                        fab.setImageResource(android.R.drawable.ic_input_delete);
                    } else if (isConnected){
                        Snackbar.make(view, "Disconnecting...", Snackbar.LENGTH_LONG).show();
                        disconnect();
                        fab.setImageResource(android.R.drawable.ic_input_add);
                    } else {
                        Log.d("MyApp", "Still connecting, refusing connect or disconnect action... ");
                    }
                } catch (Exception e) {
                    Log.d("MyApp", "Exception caught: " + e );
                }
            }
        });

        // Create the necessary variables for posting
        String PostUrl = "https://reqbin.com/echo/post/json";
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

                // Initialise the log to be sent
                //com.Group2.Heartbeat.Log log = new com.Group2.Heartbeat.Log(LocalDateTime.now().toString(), heartRate, 0, 0);
                //String json = gson.toJson(log);
                JSONObject json = new JSONObject();
                try {
                    json.put("date", LocalDateTime.now().toString());
                    json.put("hearRate", heartRate);
                    json.put("userID", 0);
                    json.put("sleepSession", 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PostUrl, json, new Response.Listener<JSONObject>() {
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

        //creation of gson objects for parsing rest response
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://192.168.56.1:8080/results/get/test";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Display the first 500 characters of the response string.
                    setRestMessage("Response is: " +response);
                    System.out.println(response);

                    NightResult nightResults = gson.fromJson(response.toString(), NightResult.class);

                    for (com.Group2.Heartbeat.Log log: nightResults.getLogs()) {

                        System.out.println(log.getHeartRate());
                    };

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setRestMessage("That didn't work!");
                System.out.println(error.getMessage());
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

    public void disconnect() {
        try{
            api.disconnectFromDevice(this.DEVICE_ID);
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

    //gets static restMessage value
    public static String getRestMessage() {
        return restMessage;
    }

    //sets the static restMessage value
    public static void setRestMessage(String restMessage) {
        MainActivity.restMessage = restMessage;
    }

}

