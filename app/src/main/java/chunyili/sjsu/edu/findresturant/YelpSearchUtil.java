package chunyili.sjsu.edu.findresturant;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chunyili.sjsu.edu.findresturant.interfaces.YelpBusinessCallback;
import chunyili.sjsu.edu.findresturant.interfaces.YelpSearchCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jilongs on 3/26/16.
 */
public class YelpSearchUtil {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG = "SearchFragment";
    private boolean isCurrentLocation = true;
    private String query = "";
    private LatLng latLng;
    private String sortBy = "";
    private YelpSearchCallback mSearchCallback;
    private ArrayList<MyBusiness> myBusinesses;
    private YelpBusinessCallback mBusinessCallback;
    private String typedLocation = "";
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }


    public YelpSearchCallback getmSearchCallback() {
        return mSearchCallback;
    }

    public void setmSearchCallback(YelpSearchCallback mSearchCallback) {
        this.mSearchCallback = mSearchCallback;
    }


    public String getTypedLocation() {
        return typedLocation;
    }

    public void setTypedLocation(String typedLocation) {
        this.typedLocation = typedLocation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    public void setIsCurrentLocation(boolean isCurrentLocation) {
        this.isCurrentLocation = isCurrentLocation;
    }

    public YelpBusinessCallback getmBusinessCallback() {
        return mBusinessCallback;
    }

    public void setmBusinessCallback(YelpBusinessCallback mBusinessCallback) {
        this.mBusinessCallback = mBusinessCallback;
    }

    public void findBusiness(String id){
        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
        YelpAPI yelpAPI = apiFactory.createAPI();


        Map<String, String> params = new HashMap<>();



        params.put("lang", "en");

        Callback<Business> callback = new Callback<Business>() {
            @Override
            public void onResponse(Response<Business> response, Retrofit retrofit) {
                Business searchResponse = response.body();
                //String name = searchResponse.name();
                // Update UI text with the Business object.
                mBusinessCallback.onBusinessReturn(new MyBusiness(searchResponse));

            }
            @Override
            public void onFailure(Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
            }
        };

        Call<Business> call = yelpAPI.getBusiness(id, params);

        call.enqueue(callback);
    }

    public void doMySearch() throws IOException {
        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();
        params.put("term", query);
        if (!sortBy.trim().isEmpty()) {
            params.put("sort", sortBy);
        }
        params.put("limit", "20");
        params.put("radius_filter", "22000");
        params.put("lang", "en");

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                SearchResponse searchResponse = response.body();
                ArrayList<Business> businesses = searchResponse.businesses();
                Log.e(TAG, String.valueOf(businesses.size()));

                myBusinesses = new ArrayList<>();
                for (Business b : businesses) {
                    myBusinesses.add(new MyBusiness(b));
                }
                Log.e(TAG, String.valueOf(myBusinesses.size()));
                mSearchCallback.onSearchReturned(myBusinesses);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
            }
        };
        Log.e(TAG, "" + isCurrentLocation);
        Log.e(TAG, "here   " + typedLocation);

        if (isCurrentLocation) {

            Call<SearchResponse> call = yelpAPI.search("San Jose", params);
            call.enqueue(callback);

        } else {

            if (latLng == null) {
                Call<SearchResponse> call = yelpAPI.search(typedLocation, params);
                call.enqueue(callback);
            } else {

                CoordinateOptions coordinateOptions = CoordinateOptions.builder()
                        .latitude(latLng.latitude)
                        .longitude(latLng.longitude)
                        .build();
                Call<SearchResponse> call = yelpAPI.search(coordinateOptions, params);

                call.enqueue(callback);
            }
        }

    }

}
