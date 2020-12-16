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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    int recognisedPattern;
    private Gson gson = new Gson();
    String welcomeMessage;
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
        TextView welcomeText = root.findViewById(R.id.welcomeText);

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
                    System.out.println("datapoints: " + dataPoints.length);
                    graph.getViewport().setMaxY(graphMaxY[0]);
                    graph.getViewport().setMinY(0);
                    graph.getGridLabelRenderer().setPadding(40);
                    GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
                    gridLabel.setHorizontalAxisTitle("Time");
                    gridLabel.setVerticalAxisTitle("HeartRate");
                    graph.setBackgroundColor(rgb(34, 34, 59));
                    series.setColor(rgb(0, 255, 0));
                    series.setTitle("Last Night");
                    gridLabel.setHorizontalLabelsVisible(true);
                    gridLabel.setVerticalLabelsVisible(true);
                    gridLabel.setHumanRounding(true);
                    graph.setVisibility(View.VISIBLE);
                    Paint paint = new Paint();
                    paint.setColor(rgb(0,255,0));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(10);
                    paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 0));

                    LineGraphSeries<DataPoint> idealPattern = new LineGraphSeries<DataPoint> (paintIdealPattern(nightResult.getShape()));
                    System.out.println(nightResult.getShape());

                    idealPattern.setCustomPaint(paint);

                    graph.addSeries(series);
                    graph.addSeries(idealPattern);

                    Log[] lastNightLogs = nightResult.getLogs();
                    LocalDateTime startOfSleep = toDateTime(lastNightLogs[0].getDate());
                    LocalDateTime endOfSleep = toDateTime(lastNightLogs[lastNightLogs.length - 1].getDate());

                    System.out.println(endOfSleep);
                    double hoursSlept = ChronoUnit.HOURS.between(startOfSleep, endOfSleep);

                    if (username.length() > 1) {

                        welcomeMessage =    "Good Morning, " + username + ".\n\nYou slept for " + hoursSlept +
                                            " hours last night.\n\n You fell asleep at " +
                                            lastNightLogs[0].getDate().split("T")[1] + " and you woke up at "
                                            + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1];

                    }
                    else {

                        welcomeMessage =    "Good Morning.\n\nYou slept for " + hoursSlept + " hours last night.\n\n You fell asleep at " +
                                            lastNightLogs[0].getDate().split("T")[1] + " and you woke up at "
                                            + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1];
                    }

                    welcomeText.setText(welcomeMessage);
                }
            }
        });

        graph = root.findViewById(R.id.graph);

        homeViewModel.mText.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        homeViewModel.mainText.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView2.setText(s);
            }
        });

        getNightResultFromServer();
        loadData();

        return root;
    }


    /**
     *
     * A method for returning datapoints to construct a painted line for the graph.
     *
     * @param recognisedPattern Pattern recognised by code and returned from server
     * @return Returns the datapoints to be included in the painted line
     */
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


    /**
     * Converts Strings to LocalDateTime.
     *
     * @param stringStamp A string for each log which is provided by the server.
     * @return Returns a  LocalDateTime which is formatted from the String provided.
     */
    public LocalDateTime toDateTime(String stringStamp){

        String[] splitString = stringStamp.split("T");
        String noTinString = splitString[0] + splitString[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        LocalDateTime result = LocalDateTime.parse(noTinString, formatter);
        return result;
    }


    private void getNightResultFromServer() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.56.1:8080/results/get/test";

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