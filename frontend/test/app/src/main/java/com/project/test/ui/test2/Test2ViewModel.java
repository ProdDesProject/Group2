package com.project.test.ui.test2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Test2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Test2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the test 2 screen");
    }

    public LiveData<String> getText() { return mText; }
}