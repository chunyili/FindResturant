package chunyili.sjsu.edu.findresturant;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
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

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SearchableActivity extends ListActivity {





    private static final String TAG___Test = "SearchActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        try {
//            handleIntent(getIntent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//        try {
//            handleIntent(intent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleIntent(Intent intent) throws IOException {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }
//    }
//
//    private void doMySearch(String query) throws IOException {
//        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
//        YelpAPI yelpAPI = apiFactory.createAPI();
//
//
//        Map<String, String> params = new HashMap<>();
//
//
//        params.put("term", query);
//        params.put("limit", "20");
//        params.put("radius_filter", "22000");
//
//
//
//        params.put("lang", "fr");
//
//        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
//            @Override
//            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
//                SearchResponse searchResponse = response.body();
//                // Update UI text with the Business object.
//                int totalNumberOfResult = searchResponse.total();
//                ArrayList<Business> businesses = searchResponse.businesses();
//                String businessName = businesses.get(0).name();
//                Double rating = businesses.get(0).rating();
//                Log.e(TAG___Test, businessName);
//                Log.e(TAG___Test, String.valueOf(rating));
//
//
//                ListView mainListView = (ListView) findViewById(android.R.id.list);
//                mainListView.setAdapter(new BusinessAdapter(SearchableActivity.this, businesses));
//            }
//            @Override
//        public void onFailure(Throwable t) {
//                // HTTP error happened, do something to handle it.
//                Log.e(TAG___Test, t.getMessage());
//                t.printStackTrace();
//            }
//        };
//        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);
////        Response<SearchResponse> searchResponse = call.execute();
//
////        Log.e(TAG___Test, searchResponse.body().toString());
//        call.enqueue(callback);
//        Log.e(TAG___Test, query);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.searchItem).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        return true;
//    }



}
