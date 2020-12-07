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

public class SleepHistFragment extends Fragment {

    private SleepHistViewModel sleepHistViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sleepHistViewModel =
                new ViewModelProvider(this).get(SleepHistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sleephist, container, false);
        final TextView textView = root.findViewById(R.id.text_sleephist);
        sleepHistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}