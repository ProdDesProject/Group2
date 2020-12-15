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

import com.Group2.Heartbeat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SleepHistFragment extends Fragment {

    private SleepHistViewModel sleepHistViewModel;

    int dateOffset = 0;

    LocalDateTime time = LocalDateTime.now();
    String currDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sleepHistViewModel =
                new ViewModelProvider(this).get(SleepHistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sleephist, container, false);
        TextView textView = root.findViewById(R.id.histDate);
        currDate = parseDate(time);
        textView.setText(currDate);

        FloatingActionButton date1 = root.findViewById(R.id.datePrevious);
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateOffset = dateOffset - 1;
                System.out.println(dateOffset);
            }
        });

        FloatingActionButton date2 = root.findViewById(R.id.dateNext);
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( dateOffset == 0) {
                    Snackbar.make(view, "Cannot go into the future", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    dateOffset = dateOffset + 1;
                    System.out.println(dateOffset);
                }
            }
        });

        return root;
    }

    public String parseDate(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd'th' LLL yyyy");
        String date = time.format(formatter);
        return date;
    }
}