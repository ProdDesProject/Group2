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
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.IntStream;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.rgb;

public class HomeFragment extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    private String welcomeMessage;
    private String username;
    private String recognisedPattern;
    private NightResult nightResult;
    private GraphView graph;
    private Date[] dates;
    private HomeViewModel homeViewModel;
    private final Gson gson = new Gson();
    private final String URL = "http://192.168.42.21:8080";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView2 = root.findViewById(R.id.textHomeMain);
        TextView welcomeText = root.findViewById(R.id.welcomeText);

        homeViewModel.mainText.setValue("Last Night");

        homeViewModel.nightResult.observe(getViewLifecycleOwner(), new Observer<NightResult>() {
            @Override
            public void onChanged(@Nullable NightResult result) {
                nightResult = result;

                if (nightResult.getUserId() >= 0) {
                    final int[] graphMaxY = {0};
                    final int[] graphMinY = {100};
                    Log[] logs = nightResult.getLogs();
                    dates = new Date[logs.length];
                    DataPoint[] dataPoints = new DataPoint[logs.length];
                    for (int i = 0; i < logs.length; i++) {
                        if (logs[i].getHeartRate() > graphMaxY[0]) {
                            graphMaxY[0] = logs[i].getHeartRate();
                        }
                        if (logs[i].getHeartRate() < graphMinY[0]) {
                            graphMinY[0] = logs[i].getHeartRate();
                        }
                        LocalDateTime time = toDateTime(logs[i].getDate());
                        Date date = convertToDateViaInstant(time);
                        dates[i] = date;
                        dataPoints[i] = new DataPoint(date, logs[i].getHeartRate());
                    }

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                Format formatter = new SimpleDateFormat("HH:mm:ss");
                                return formatter.format(value);
                            }
                            return super.formatLabel(value, false);
                        }
                    });
                    graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(120);

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    graph.getViewport().setMaxY(graphMaxY[0] + 10);
                    graph.getViewport().setMinY(graphMinY[0] - 10);
                    graph.getViewport().setYAxisBoundsManual(true);


                    graph.getViewport().setMinX(dates[0].getTime());
                    graph.getViewport().setMaxX(dates[dates.length - 1].getTime());
                    graph.getViewport().setXAxisBoundsManual(true);
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
                    paint.setColor(rgb(255, 0, 0));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(10);
                    paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 0));

                    recognisedPattern = nightResult.getShape();
                    LineGraphSeries<DataPoint> idealPattern = new LineGraphSeries<DataPoint>(paintIdealPattern(recognisedPattern));
                    System.out.println(recognisedPattern);

                    idealPattern.setCustomPaint(paint);

                    graph.addSeries(series);
                    graph.addSeries(idealPattern);

                    Log[] lastNightLogs = nightResult.getLogs();
                    LocalDateTime startOfSleep = toDateTime(lastNightLogs[0].getDate());
                    LocalDateTime endOfSleep = toDateTime(lastNightLogs[lastNightLogs.length - 1].getDate());

                    System.out.println(endOfSleep);
                    double hoursSlept = ChronoUnit.HOURS.between(startOfSleep, endOfSleep);

                    welcomeMessage = getNightSummary(hoursSlept, lastNightLogs);

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

    public String getNightSummary(double hoursSlept, Log[] lastNightLogs) {

        if (username.length() > 1) {

            return "Good Morning, " + username + ".\n\nYou slept for " + hoursSlept +
                    " hours last night.\nYou fell asleep at " +
                    lastNightLogs[0].getDate().split("T")[1] + " and woke at "
                    + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1]
                    + ".\n\nYour heart-rate pattern looks very similar to the "
                    + recognisedPattern.toLowerCase() + " pattern (Represented by the dotted red line).\n" +
                    getSleepImprovementRecommendations(recognisedPattern);

        } else {

            return "Good Morning" + ".\n\nYou slept for " + hoursSlept +
                    " hours last night.\nYou fell asleep at " +
                    lastNightLogs[0].getDate().split("T")[1] + " and woke at "
                    + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1]
                    + ".\n\nYour heart-rate pattern looks very similar to the "
                    + recognisedPattern.toLowerCase() + " pattern (Represented by the dotted red line).\n" +
                    getSleepImprovementRecommendations(recognisedPattern);
        }
    }

    public String getSleepImprovementRecommendations(String recognisedPattern){

        switch (recognisedPattern) {
            case "HILL":

                return "The hill signals exhaustion\n" +
                        "It may occur if you go to bed outside of your ideal window." +
                        "It may also occur as a result of snoring, which raises your heartrate.\n";
            case "HAMMOCK":

                return "Congratulations! This is an ideal heart rate pattern.\n" +
                        "It represents a quality night's sleep.\nKeep up the great work!";
            case "CURVE":

                return "This signals an overworked metabolism\n" +
                        "Heart rate starts high and decreases until waking, meaning you can feel groggy in the morning." +
                        "You can prevent this by not exercising or eating late at night\n";
            default:

                return "Beep boop";
        }
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }


    /**
     * A method for returning datapoints to construct a painted line for the graph.
     *
     * @param recognisedPattern Pattern recognised by code and returned from server
     * @return Returns the datapoints to be included in the painted line
     */
    public DataPoint[] paintIdealPattern(String recognisedPattern) {
        try {
            if (dates.length > 0) {
                nightResult.getShape();
                Log[] logs = nightResult.getLogs();

                int quarterLog = logs.length / 4;
                int middleLog = logs.length / 2;
                int thirdQuarterLog = quarterLog * 3;


                DataPoint[] hillPattern = {
                        new DataPoint(dates[0], 55),
                        new DataPoint(dates[(quarterLog / 2)], (77 + 55) / 2),
                        new DataPoint(dates[quarterLog], 77),
                        new DataPoint(dates[((quarterLog + middleLog) / 2)], (100 + 77) / 2),
                        new DataPoint(dates[middleLog], 100),
                        new DataPoint(dates[((middleLog + thirdQuarterLog) / 2)], (100 + 46) / 2),
                        new DataPoint(dates[thirdQuarterLog], 46),
                        new DataPoint(dates[((thirdQuarterLog + logs.length) / 2)], (100 + 46) / 2),
                        new DataPoint(dates[(logs.length - 1)], 80)
                };

                DataPoint[] hammockPattern = {
                        new DataPoint(dates[0], 85),
                        new DataPoint(dates[(quarterLog / 2)], (60 + 85) / 2),
                        new DataPoint(dates[quarterLog], 60),
                        new DataPoint(dates[((quarterLog + middleLog) / 2)], (60 + 40) / 2),
                        new DataPoint(dates[middleLog], 40),
                        new DataPoint(dates[((middleLog + thirdQuarterLog) / 2)], (40 + 60) / 2),
                        new DataPoint(dates[thirdQuarterLog], 60),
                        new DataPoint(dates[((thirdQuarterLog + logs.length) / 2)], (60 + 85) / 2),
                        new DataPoint(dates[(logs.length - 1)], 85)
                };

                DataPoint[] curvePattern = {
                        new DataPoint(dates[0], 120),
                        new DataPoint(dates[(quarterLog / 2)], (120 + 100) / 2),
                        new DataPoint(dates[quarterLog], 100),
                        new DataPoint(dates[((quarterLog + middleLog) / 2)], (100 + 85) / 2),
                        new DataPoint(dates[middleLog], 85),
                        new DataPoint(dates[((middleLog + thirdQuarterLog) / 2)], (85 + 65) / 2),
                        new DataPoint(dates[thirdQuarterLog], 65),
                        new DataPoint(dates[((thirdQuarterLog + logs.length) / 2)], (50 + 65) / 2),
                        new DataPoint(dates[(logs.length - 1)], 50)
                };

                DataPoint[] undefinedPattern = {
                        new DataPoint(dates[0], 0),
                };


                switch (recognisedPattern) {
                    case "HILL":
                        return hillPattern;
                    case "HAMMOCK":
                        return hammockPattern;
                    case "SLOPE":
                        return curvePattern;
                }

                System.out.println("Did not receive recognised pattern from server");
                return null;
            }
        } catch (NullPointerException e) {
            System.out.println("ideal pattern error: DATES NOT SET");
        }

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
    public LocalDateTime toDateTime(String stringStamp) {

        String[] splitString = stringStamp.split("T");
        String noTinString = splitString[0] + splitString[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        return LocalDateTime.parse(noTinString, formatter);
    }


    private void getNightResultFromServer() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = URL + "/results/get/test";

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