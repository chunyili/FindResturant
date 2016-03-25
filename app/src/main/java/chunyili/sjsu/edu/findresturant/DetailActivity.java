package chunyili.sjsu.edu.findresturant;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailActivity extends AppCompatActivity {
    private ImageView restaurantIcon;
    private TextView restaurantName;
    private ImageView restaurantRating;
    private TextView restaurantReviews;
    private TextView restaurantAdress;
    private TextView restaurantPhoneNO;
    Intent intent;

    private String TAG___Test = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        intent = getIntent();
        connectToYelp();
        restaurantName.setText(intent.getExtras().get("id").toString());


    }


    private void connectToYelp(){
        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
        YelpAPI yelpAPI = apiFactory.createAPI();


        Map<String, String> params = new HashMap<>();



        params.put("lang", "en");

        Callback<Business> callback = new Callback<Business>() {
            @Override
            public void onResponse(Response<Business> response, Retrofit retrofit) {
                Business searchResponse = response.body();
                //String name = searchResponse.name();

//                View convertView = LayoutInflater//.from(getContext()).inflate(R.layout.container_list_item_view, parent, false);
                // Lookup view for data population
                restaurantName = (TextView) findViewById(R.id.restaurant_name);
                restaurantAdress = (TextView) findViewById(R.id.restaurant_address);
                restaurantIcon = (ImageView) findViewById(R.id.restaurant_icon);
                restaurantPhoneNO = (TextView) findViewById(R.id.restaurant_phone_No);
                restaurantReviews = (TextView) findViewById(R.id.restaurant_reviews);
                new DownloadImageTask(restaurantIcon)
                        .execute(searchResponse.imageUrl());
                restaurantRating = (ImageView) findViewById(R.id.restaurant_rating);
                new DownloadImageTask(restaurantRating)
                        .execute(searchResponse.ratingImgUrlLarge());

                // Populate the data into the template view using the data object
                StringBuilder sb = new StringBuilder();
                String address = searchResponse.location().address().toString();


                for(int i = 0; i < address.length() - 1; i ++){
                    if(address.charAt(i) == '[' ){
                        continue;
                    }else{
                        sb.append(address.charAt(i));
                    }
                }


                restaurantName.setText(searchResponse.name());
                if (searchResponse.phone() != null) {
                    restaurantPhoneNO.setText("Call " + searchResponse.phone().toString());
                } else {
                    restaurantPhoneNO.setText("No data!");
                }
                if (searchResponse.reviewCount() != null) {
                    restaurantReviews.setText(searchResponse.reviewCount() + " Reviews");
                } else {
                    restaurantReviews.setText("No data!");
                }

                restaurantAdress.setText(""+ sb.toString()+ ", " + searchResponse.location().city());

                // Update UI text with the Business object.

            }
            @Override
            public void onFailure(Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.e(TAG___Test, t.getMessage());
                t.printStackTrace();
            }
        };

        Call<Business> call = yelpAPI.getBusiness(intent.getExtras().get("id").toString(), params);

        call.enqueue(callback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.test:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
