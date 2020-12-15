package com.Group2.Heartbeat.ui.SleepHist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.Group2.Heartbeat.Log;
import com.Group2.Heartbeat.NightResult;
import com.Group2.Heartbeat.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.IntStream;

import static android.graphics.Color.rgb;

public class SleepHistFragment extends Fragment {

    int dateOffset = 0;
    int latestSleepSession = -1;
    NightResult nightResult;
    LocalDate time = LocalDate.now();
    String currDate;
    private SleepHistViewModel sleepHistViewModel;
    private Gson gson = new Gson();
    TextView textView;

    GraphView graph;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.getLatestSleepSession();

        sleepHistViewModel =
                new ViewModelProvider(this).get(SleepHistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sleephist, container, false);

        sleepHistViewModel.latestSleepSession.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer result) {
                latestSleepSession = result;
                System.out.println(("new sleepsession: " + latestSleepSession));
                getNightResultForSleepsession(latestSleepSession);
            }
        });

        sleepHistViewModel.nightResult.observe(getViewLifecycleOwner(), new Observer<NightResult>() {
            @Override
            public void onChanged(@Nullable NightResult result) {
                nightResult = result;
                currDate = nightResult.getLogs()[0].getDate();
                currDate = currDate.substring(0, currDate.length()-9);
                textView.setText(currDate);
                drawGraph();
            }
        });


        textView = root.findViewById(R.id.histDate);
        currDate = parseDate(time);
        textView.setText(currDate);

        graph = root.findViewById(R.id.graph);

        FloatingActionButton previousDayFab = root.findViewById(R.id.datePrevious);
        previousDayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((latestSleepSession + dateOffset) < 0) {
                    Snackbar.make(view, "No records for that date!", Snackbar.LENGTH_LONG).show();
                } else {
                    dateOffset = dateOffset - 1;
                    getNightResultForSleepsession(latestSleepSession + dateOffset);
                    System.out.println("dateoffset: " + dateOffset);
                }
            }
        });

        FloatingActionButton nextDayFab = root.findViewById(R.id.dateNext);
        nextDayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateOffset == 0) {
                    Snackbar.make(view, "Cannot go into the future", Snackbar.LENGTH_LONG).show();
                } else {
                        dateOffset = dateOffset + 1;
                        getNightResultForSleepsession(latestSleepSession + dateOffset);
                        // VISUALIZE Data
                        System.out.println("dateoffset: " + dateOffset);
                }
            }
        });


        return root;
    }

    private String parseDate(LocalDate time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd'th' LLL yyyy");
        String date = time.format(formatter);
        return date;
    }

    private void drawGraph(){
        final int[] graphMaxY = {0};
        Log[] logs = nightResult.getLogs();
        DataPoint[] dataPoints = new DataPoint[logs.length];
        IntStream.range(0, logs.length)
                .peek(i -> {if (i > graphMaxY[0]) graphMaxY[0] = i;})
                .forEach(i -> dataPoints[i] = new DataPoint(i, logs[i].getHeartRate()));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        int graphMaxX = dataPoints.length;
        System.out.println("datapoints: " + dataPoints.length);
        graph.getViewport().setMaxX(graphMaxX);
        graph.getViewport().setMaxY(graphMaxY[0]);
        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setPadding(60);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("HeartRate");
        graph.setBackgroundColor(rgb(34, 34, 59));
        series.setColor(rgb(0, 255, 0));
        gridLabel.setHorizontalLabelsVisible(true);
        gridLabel.setVerticalLabelsVisible(true);
        gridLabel.setHumanRounding(true);
        graph.setVisibility(View.VISIBLE);
        graph.addSeries(series);
    }

    /**
     * Get the latest sleepsession for the current user from the server and sets the this.latestSleepSession
     */
    private void getLatestSleepSession() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://192.168.42.21:8080/logs/sleepsession/latest?userId=1";

            // Request a string response from the provided URL.
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    try {
                        String responseString = response.optString("sleepsession", "0");
                        Integer sleepSession = gson.fromJson(responseString, Integer.class);
                        sleepHistViewModel.latestSleepSession.setValue(sleepSession);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                setRestMessage("That didn't work!");
                    error.printStackTrace();
                }

            });

            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        } catch (Exception e) {
            System.out.println("error in getting latest sleep session: " + e.getLocalizedMessage());
        }
    }



    /**
     * Get the nightresult for a specific night. Currently hardcoded for user 1.
     * @param sleepSession identifier for the night its representing.
     */
    private void getNightResultForSleepsession(int sleepSession) {
        try {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://192.168.42.21:8080/results/get/specific?userId=1&sleepsession=" + sleepSession;

            // Request a string response from the provided URL.
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println(response);
                        NightResult nightResult = gson.fromJson(String.valueOf(response), NightResult.class);
                        sleepHistViewModel.nightResult.setValue(nightResult);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                setRestMessage("That didn't work!");
                    error.printStackTrace();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        } catch (Exception e) {
            System.out.println("error in getting nightresult: " + e.getLocalizedMessage());
        }
    }
}