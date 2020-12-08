package com.Group2.Heartbeat.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.Group2.Heartbeat.R;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    String username, useremail;
    int userage;

    EditText user_name;
    EditText user_email;
    EditText user_age;

    Button SubmitProfile;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String AGE = "age";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        user_name = root.findViewById(R.id.user_Name);
        user_email = root.findViewById(R.id.user_Email);
        user_age = root.findViewById(R.id.user_age);
        SubmitProfile = root.findViewById(R.id.SubmitProfile);

        SubmitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user_name.getText().toString();
                useremail = user_email.getText().toString();
                if(!user_age.getText().toString().equals("")) {
                    userage = Integer.parseInt(user_age.getText().toString());
                }
                saveData();
            }
        });
        loadData();
        updateView();
        return root;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME, username);
        editor.putString(EMAIL, useremail);
        editor.putString(AGE, Integer.toString(userage));

        editor.apply();
        Toast.makeText(getActivity(), "Data has been saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(NAME, "");
        useremail = sharedPreferences.getString(EMAIL, "");
        userage = Integer.parseInt(sharedPreferences.getString(AGE,"0"));
    }

    public void updateView() {
        user_name.setText(username);
        user_email.setText(useremail);
        user_age.setText(Integer.toString(userage));
    }
}