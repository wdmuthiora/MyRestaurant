package com.moringaschool.myrestaurants.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurants.R;
import com.moringaschool.myrestaurants.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurants.models.Business;
import com.moringaschool.myrestaurants.models.YelpBusinessesSearchResponse;
import com.moringaschool.myrestaurants.network.YelpApi;
import com.moringaschool.myrestaurants.network.YelpClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListActivity extends AppCompatActivity {

    public static final String TAG = RestaurantListActivity.class.getSimpleName();
    private RestaurantListAdapter mAdapter;
    public List<Business> restaurants;

    //use Butterknife to locate these Ids from the physical.
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView; //RecyclerView needs a RecyclerView.Adapter and LayoutManager.
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");


        //interacting with the YELP API
        YelpApi client = YelpClient.getClient();
        //Search restaurants based on the term provided, (location),
        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");

        //This call.enqueue has two built-in methods to be overwritten, onResponse, and onFailure.
        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {

//    If there is a network response from the API call:
            // The onResponse() takes two parameters, call and response
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                hideProgressBar();
                //if the network response is successful.
                if (response.isSuccessful()) {
                    restaurants = response.body().getBusinesses();

//    RecyclerView.Adapter section
                    //Define adapter,that takes in context, and an array to display. In this case, the restaurants, returned from body of the network query's results.
                    mAdapter = new RestaurantListAdapter(RestaurantListActivity.this, restaurants);
                    //set the RecyclerView's adapter to this custom adapter.
                    mRecyclerView.setAdapter(mAdapter);

//    LayoutManager section
                    //Define a LayoutManager to the RecyclerView, to position each item inside the RecyclerView.
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantListActivity.this);
                    //set LayoutManager to layoutManager
                    mRecyclerView.setLayoutManager(layoutManager);
                    //So that they don't attempt to resize for best-fit
                    mRecyclerView.setHasFixedSize(true);

                    showRestaurants();
                }
                //if the network response is unsuccessful.
                else {
                    showUnsuccessfulMessage();
                }

            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }

        });

    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}