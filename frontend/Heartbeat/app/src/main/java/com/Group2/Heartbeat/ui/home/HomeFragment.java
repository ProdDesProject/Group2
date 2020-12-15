package com.Group2.Heartbeat.ui.home;

import android.content.SharedPreferences;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
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

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.rgb;

import com.Group2.Heartbeat.Log;
import com.Group2.Heartbeat.NightResult;
import com.Group2.Heartbeat.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    int recognisedPattern;
    private Gson gson = new Gson();
    String welcomemessage;
    String username;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    NightResult nightResult;
    GraphView graph;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView2 = root.findViewById(R.id.textHomeMain);

        homeViewModel.nightResult.observe(getViewLifecycleOwner(), new Observer<NightResult>() {
            @Override
            public void onChanged(@Nullable NightResult result) {
                System.out.println("onchange!!!!!!!!!!!!!!!!");
                nightResult = result;
                if (nightResult.getUserId() >= 0) {
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
                    Paint paint = new Paint();
                    paint.setColor(rgb(0,255,0));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(10);
                    paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 0));

                    LineGraphSeries<DataPoint> hammock = new LineGraphSeries<DataPoint> (paintIdealPattern(nightResult.getShape()));
                    System.out.println(nightResult.getShape());

                    hammock.setCustomPaint(paint);

                    graph.addSeries(series);
                    graph.addSeries(hammock);
                    graph.addSeries(series);
                }
            }
        });



        //TODO: Move paint code into graph creation scope
        /*
        Paint paint = new Paint();
        paint.setColor(rgb(0,255,0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 35));

        hammock.setCustomPaint(paint);

        graph.addSeries(series);
        graph.addSeries(hammock);
        this.getNightResultFromServer();

         */

        graph = root.findViewById(R.id.graph);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        homeViewModel.getText2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView2.setText(s);
            }
        });

        getNightResultFromServer();
        loadData();

        TextView welcomeText = root.findViewById(R.id.welcomeText);
        welcomemessage = "Hi, " + username.toString();
        welcomeText.setText(welcomemessage);


        return root;
    }

    public DataPoint[] paintIdealPattern(String recognisedPattern){

        nightResult.getShape();
        Log[] logs = nightResult.getLogs();

        int quarterLog = logs.length / 4;
        int middleLog = logs.length / 2;
        int thirdQuarterLog = quarterLog * 3;

        int i = 0;

        DataPoint[] hillPattern = {
                new DataPoint(0, 55),
                new DataPoint((quarterLog / 2), (77 + 55) / 2),
                new DataPoint(quarterLog, 77),
                new DataPoint(((quarterLog + middleLog) / 2), (100 + 77) / 2),
                new DataPoint(middleLog, 100),
                new DataPoint(((middleLog + thirdQuarterLog) / 2), (100 + 46) / 2),
                new DataPoint(thirdQuarterLog, 46),
                new DataPoint(((thirdQuarterLog + logs.length) / 2), (100 + 46) / 2),
                new DataPoint((logs.length - 1), 80)
        };

        DataPoint[] hammockPattern = {
                new DataPoint(0, 85),
                new DataPoint((quarterLog / 2), (60 + 85) / 2),
                new DataPoint(quarterLog, 60),
                new DataPoint(((quarterLog + middleLog) / 2), (60 + 40) / 2),
                new DataPoint(middleLog, 40),
                new DataPoint(((middleLog + thirdQuarterLog) / 2), (40 + 60) / 2),
                new DataPoint(thirdQuarterLog, 60),
                new DataPoint(((thirdQuarterLog + logs.length) / 2), (60 + 85) / 2),
                new DataPoint((logs.length - 1), 85)
        };

        DataPoint[] curvePattern = {
                new DataPoint(0, 120),
                new DataPoint((quarterLog / 2), (120 + 100) / 2),
                new DataPoint(quarterLog, 100),
                new DataPoint(((quarterLog + middleLog) / 2), (100 + 85) / 2),
                new DataPoint(middleLog, 85),
                new DataPoint(((middleLog + thirdQuarterLog) / 2), (85 + 65) / 2),
                new DataPoint(thirdQuarterLog, 65),
                new DataPoint(((thirdQuarterLog + logs.length) / 2), (50 + 65) / 2),
                new DataPoint((logs.length - 1), 50)
        };

        DataPoint[] undefinedPattern = {
                new DataPoint(0, 0),
        };


        if (recognisedPattern.equals("HILL")){

            return hillPattern;
        }
        else if (recognisedPattern.equals("HAMMOCK")){

            return hammockPattern;
        }
        else if (recognisedPattern.equals("CURVE")) {

            return curvePattern;
        }
        else if (recognisedPattern.equals("UNDEFINED")){

            return hillPattern;
        }

        System.out.println("Did not receive recognised pattern from server");
        return null;
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(NAME, "");
    }

    private void getNightResultFromServer() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.42.21:8080/results/get/test";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                try {
                    NightResult nightResult = gson.fromJson(String.valueOf(response), NightResult.class);
                    homeViewModel.nightResult.setValue(nightResult);
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

    }


}