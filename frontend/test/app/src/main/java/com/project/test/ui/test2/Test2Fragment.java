package com.project.test.ui.test2;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.test.R;

public class Test2Fragment extends Fragment {

    private Test2ViewModel Test2ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Test2ViewModel =
                new ViewModelProvider(this).get(Test2ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_test2,container, false);
        final TextView textView = root.findViewById(R.id.text_test2);
        Test2ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s)  { textView.setText(s); }
        });
        return root;
    }
}
