package com.project.test.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import com.google.android.material.snackbar.Snackbar;
import com.project.test.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    String username, useremail;
    int userage;

    EditText user_name;
    EditText user_email;
    EditText user_age;

    Button EditProfile;

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

        user_name = (EditText) root.findViewById(R.id.user_Name);
        user_email = (EditText) root.findViewById(R.id.user_Email);
        user_age = (EditText) root.findViewById(R.id.user_age);
        EditProfile = (Button) root.findViewById(R.id.EditProfile);

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user_name.getText().toString();
                useremail = user_email.getText().toString();
                if(!user_age.getText().toString().equals("")) {
                    userage = Integer.parseInt(user_age.getText().toString());
                }

                //ToastforTest(username);
                ToastforTest(useremail);
                //ToastforTest(String.valueOf(userage));
            }
        });
        return root;
    }

    private void ToastforTest(String test) {
        Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();
        /*
        Toast.makeText(getActivity(), username, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), useremail, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), String.valueOf(userage), Toast.LENGTH_SHORT).show();
        */
    }
}