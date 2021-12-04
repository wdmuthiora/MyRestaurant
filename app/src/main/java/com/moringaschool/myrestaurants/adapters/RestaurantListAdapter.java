package com.moringaschool.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurants.R;
import com.moringaschool.myrestaurants.models.Business;
import com.moringaschool.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private List<Business> mRestaurants; //mRestaurants in this case, represents a list of restaurant businesses received from the Yelp API, converted into POJO through the use of the Business model
    private Context mContext;

    //  Constructor
    public RestaurantListAdapter(Context context, List<Business> restaurants) {
        mContext = context;
        mRestaurants = restaurants;
    }

    //  Inflates XML layout and returns a ViewHolder
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    //  Binds data from our list, to a specific View, via the ViewHolder
    @Override
    public void onBindViewHolder(RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));

    }

    //  Returns size of array of items to be displayed
    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    // ViewHolder for individual items to be displayed. Notice, it is a nested/inner class
    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

        //declared again bc it's a nested class.
        private Context mContext;

        //Constructor.
        public RestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this); //Notice the onClickListener has been set to an instance of an individual item, inside the constructor.
        }

        public void bindRestaurant(Business restaurant) {
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
           //use Picasso to get the url and display image from the Business class object restaurant.
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);

        }

        //onClick method is where you dictate what the click does.
        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
            mContext.startActivity(intent);
        }
    }

}
