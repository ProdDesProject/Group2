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

import com.Group2.Heartbeat.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    int recognisedPattern;
    String welcomemessage;
    String username;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView2 = root.findViewById(R.id.textHomeMain);

        //all code for initialising the graphview and plotting some points
        final GraphView graph = root.findViewById(R.id.graph);
        int graphMaxY = 150;
        int graphMaxX = 10;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 60),
                new DataPoint(1, 69),
                new DataPoint(2, 81),
                new DataPoint(3, 82),
                new DataPoint(4, 58)
        });

        LineGraphSeries<DataPoint> hammock = new LineGraphSeries<DataPoint> (paintIdealPattern(1));

        graph.getViewport().setMaxX(graphMaxX);
        graph.getViewport().setMaxY(graphMaxY);
        graph.getViewport().setMinY(0);
        graph.getGridLabelRenderer().setPadding(60);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("HeartRate");
        graph.setBackgroundColor(rgb(34,34,59));
        series.setColor(rgb(0,255,0));
        gridLabel.setHorizontalLabelsVisible(true);
        gridLabel.setVerticalLabelsVisible(true);
        gridLabel.setHumanRounding(true);
        graph.setVisibility(View.VISIBLE);

        Paint paint = new Paint();
        paint.setColor(rgb(0,255,0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{25, 35}, 35));

        hammock.setCustomPaint(paint);

        graph.addSeries(series);
        graph.addSeries(hammock);

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

        loadData();

        TextView welcomeText = root.findViewById(R.id.welcomeText);
        welcomemessage = "Hi, " + username.toString();
        welcomeText.setText(welcomemessage);

        return root;
    }

    public DataPoint[] paintIdealPattern(int recognisedPattern){

        /*
        LineGraphSeries<DataPoint> hllPattern = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 50),
                new DataPoint(1, 60),
                new DataPoint(2, 65),
                new DataPoint(3, 60),
                new DataPoint(4, 50)
        });
        */

        DataPoint[] hillPattern = {
                new DataPoint(0, 50),
                new DataPoint(1, 60),
                new DataPoint(2, 65),
                new DataPoint(3, 60),
                new DataPoint(4, 50)
        };

        /*
        LineGraphSeries<DataPoint> hammockPattern = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 65),
                new DataPoint(1, 60),
                new DataPoint(2, 50),
                new DataPoint(3, 60),
                new DataPoint(4, 65)
        });
        */

        DataPoint[] hammockPattern = {
                new DataPoint(0, 65),
                new DataPoint(1, 60),
                new DataPoint(2, 50),
                new DataPoint(3, 60),
                new DataPoint(4, 65)
        };

        /*
        LineGraphSeries<DataPoint> othrPattern = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 40),
                new DataPoint(1, 35),
                new DataPoint(2, 60),
                new DataPoint(3, 35),
                new DataPoint(4, 40)
        });

         */

        DataPoint[] otherPattern = {
                new DataPoint(0, 40),
                new DataPoint(1, 35),
                new DataPoint(2, 60),
                new DataPoint(3, 35),
                new DataPoint(4, 40)
        };

        if (recognisedPattern == 0){

            return hillPattern;
        }
        else if (recognisedPattern == 1){

            return hammockPattern;
        }
        else if (recognisedPattern == 2){

            return otherPattern;
        }

        return null;
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(NAME, "");
    }
}