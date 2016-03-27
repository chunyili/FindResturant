package chunyili.sjsu.edu.findresturant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chunyili.sjsu.edu.findresturant.interfaces.YelpSearchCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jilongs on 3/26/16.
 */
public class SearchFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG = "SearchFragment";
    private boolean isCurrentLocation = true;
    private String query = "";

    public YelpSearchCallback getmSearchCallback() {
        return mSearchCallback;
    }

    public void setmSearchCallback(YelpSearchCallback mSearchCallback) {
        this.mSearchCallback = mSearchCallback;
    }

    private YelpSearchCallback mSearchCallback;
    private ArrayList<MyBusiness> myBusinesses;
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

    private String typedLocation = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("list fragment", "here");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
    public void doMySearch() throws IOException {
        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();
        params.put("term", query);
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
                for(Business b: businesses){
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
        Log.e(TAG, "here   " +typedLocation);

        if(isCurrentLocation){

            Call<SearchResponse> call = yelpAPI.search("San Jose", params);

            call.enqueue(callback);

        }else{

            Log.e(TAG, typedLocation);
            Call<SearchResponse> call = yelpAPI.search(typedLocation, params);

            call.enqueue(callback);
        }

    }
}
