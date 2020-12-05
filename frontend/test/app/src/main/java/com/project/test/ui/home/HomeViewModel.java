package com.project.test.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.widget.TextView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mainText;

    public HomeViewModel() {

        mText = new MutableLiveData<>();
        mainText = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                String stringValue = "";

                if (!response.isSuccessful()) {
                    mText.setValue("Beep boop");
                    return;
                }
                List<Post> posts = response.body();
                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";
                    stringValue = stringValue + content;
                }

                //mText.setValue("Success");
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //textViewResult.setText(t.getMessage());
                mText.setValue("Failure");
            }

        });

        //mText.setValue("this is a Home page");
        mainText.setValue("Sean");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mainText;
    }
}