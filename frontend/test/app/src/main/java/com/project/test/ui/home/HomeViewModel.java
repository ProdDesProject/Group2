package com.project.test.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.widget.TextView;
import java.util.List;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.test.MainActivity;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mainText;

    public HomeViewModel() {

        mText = new MutableLiveData<>();
        mainText = new MutableLiveData<>();

        mText.setValue(MainActivity.getRestMessage());
        mainText.setValue("Sean");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mainText;
    }
}