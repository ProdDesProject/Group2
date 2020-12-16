package com.Group2.Heartbeat.ui.SleepHist;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.rgb;

public class SleepHistFragment extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    int dateOffset = 0;
    int latestSleepSession = -1;
    NightResult nightResult;
    LocalDate time = LocalDate.now();
    String currDate;
    TextView textView;
    String welcomeMessage;
    String username;
    GraphView graph;
    Date[] dates;
    private SleepHistViewModel sleepHistViewModel;
    private Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.getLatestSleepSession();

        sleepHistViewModel =
                new ViewModelProvider(this).get(SleepHistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sleephist, container, false);
        TextView welcomeText = root.findViewById(R.id.welcomeText);

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
                currDate = currDate.substring(0, currDate.length() - 9);
                textView.setText(currDate);
                drawGraph();
                welcomeText.setText(getSleepExplanation());
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
                if ((latestSleepSession + dateOffset) < 2) {
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

        loadData();
        return root;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(NAME, "");
    }

    private String parseDate(LocalDate time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd'th' LLL yyyy");
        String date = time.format(formatter);
        return date;
    }

    private void drawGraph() {
        final int[] graphMaxY = {0};
        final int[] graphMinY = {100};
        Log[] logs = nightResult.getLogs();
        dates = new Date[logs.length];
        DataPoint[] dataPoints = new DataPoint[logs.length];
        IntStream.range(0, logs.length)
                .peek(i -> {if (logs[i].getHeartRate() > graphMaxY[0]){
                    graphMaxY[0] = logs[i].getHeartRate();
                }})
                .peek(i -> {
                    if (logs[i].getHeartRate() < graphMinY[0]) {
                        graphMinY[0] = logs[i].getHeartRate();
                    }
                })
                .forEach(i -> {
                    System.out.println("date before: " + logs[i].getDate());
                    LocalDateTime time = this.toDateTime(logs[i].getDate());
                    Date date = convertToDateViaInstant(time);
                    System.out.println("date after: " + date);
                    dates[i] = date;
                    dataPoints[i] = new DataPoint(date, logs[i].getHeartRate());
                });

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Format formatter = new SimpleDateFormat("HH:mm:ss");
                    return formatter.format(value);
                }
                return super.formatLabel(value, isValueX);
            }
        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(10); // only 4 because of the space
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(120);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.getViewport().setMaxY(graphMaxY[0] + 10);
        graph.getViewport().setMinY(graphMinY[0] - 10);
        graph.getViewport().setYAxisBoundsManual(true);


        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(dates[0].getTime());
        graph.getViewport().setMaxX(dates[dates.length-1].getTime());
        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setPadding(40);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("HeartRate");
        graph.setBackgroundColor(rgb(34, 34, 59));
        series.setColor(rgb(0, 255, 0));
        gridLabel.setHorizontalLabelsVisible(true);
        gridLabel.setVerticalLabelsVisible(true);
        gridLabel.setHumanRounding(true);
        graph.setVisibility(View.VISIBLE);
        graph.removeAllSeries();
        Paint paint = new Paint();
        paint.setColor(rgb(0, 255, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 0));

        LineGraphSeries<DataPoint> idealPattern = new LineGraphSeries<DataPoint>(paintIdealPattern(nightResult.getShape()));
        System.out.println(nightResult.getShape());

        idealPattern.setCustomPaint(paint);

        graph.addSeries(idealPattern);
        graph.addSeries(series);
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public String getSleepExplanation() {

        Log[] lastNightLogs = nightResult.getLogs();
        LocalDateTime startOfSleep = toDateTime(lastNightLogs[0].getDate());
        LocalDateTime endOfSleep = toDateTime(lastNightLogs[lastNightLogs.length - 1].getDate());

        System.out.println(endOfSleep);
        double hoursSlept = ChronoUnit.HOURS.between(startOfSleep, endOfSleep);

        if (username.length() > 1) {

            return "Good Morning, " + username + ".\n\nYou slept for " + hoursSlept +
                    " hours last night.\n\n You fell asleep at " +
                    lastNightLogs[0].getDate().split("T")[1] + " and you woke up at "
                    + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1];

        } else {

            return "Good Morning.\n\nYou slept for " + hoursSlept + " hours last night.\n\n You fell asleep at " +
                    lastNightLogs[0].getDate().split("T")[1] + " and you woke up at "
                    + lastNightLogs[lastNightLogs.length - 1].getDate().split("T")[1];
        }
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
     * Converts Strings to LocalDateTime.
     *
     * @param stringStamp A string for each log which is provided by the server.
     * @return Returns a  LocalDateTime which is formatted from the String provided.
     */
    public LocalDateTime toDateTime(String stringStamp) {

        String[] splitString = stringStamp.split("T");
        String noTinString = splitString[0] + splitString[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");
        LocalDateTime result = LocalDateTime.parse(noTinString, formatter);
        return result;
    }

    /**
     * Get the nightresult for a specific night. Currently hardcoded for user 1.
     *
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

    /**
     * A method for returning datapoints to construct a painted line for the graph.
     *
     * @param recognisedPattern Pattern recognised by code and returned from server
     * @return Returns the datapoints to be included in the painted line
     */
    public DataPoint[] paintIdealPattern(String recognisedPattern) {

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


        if (recognisedPattern.equals("HILL")) {

            return hillPattern;
        } else if (recognisedPattern.equals("HAMMOCK")) {

            return hammockPattern;
        }
//        else if (recognisedPattern.equals("CURVE")) {
        else if (recognisedPattern.equals("SLOPE")) {

            return curvePattern;
        } else if (recognisedPattern.equals("UNDEFINED")) {

            return hillPattern;
        }

        System.out.println("Did not receive recognised pattern from server");
        return null;
    }
}