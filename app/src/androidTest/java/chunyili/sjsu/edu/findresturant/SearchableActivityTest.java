package chunyili.sjsu.edu.findresturant;


import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;

import static org.junit.Assert.*;

/**
 * Created by jilongsun on 3/20/16.
 */
public class SearchableActivityTest {

    @Test
    public void test() throws IOException {
        YelpAPIFactory apiFactory = new YelpAPIFactory("QHmJW4LG9PtjZEqUiW1pow", "yjf3i7UyzturnAkQ2LPtVtnT6k0", "PFRsmfRmnsX1oQXP4tK8Gm-UQ4CSrv1w", "KYhZNmMrY3JKdGyf7JWkxtgA2Gc");
        YelpAPI yelpAPI = apiFactory.createAPI();


        Map<String, String> params = new HashMap<>();


        params.put("term", "food");
        params.put("limit", "20");
        params.put("radius_filter", "22000");



        params.put("lang", "fr");

        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);
        Response<SearchResponse> searchResponse = call.execute();

        System.out.println(searchResponse.body().toString());
//        call.enqueue(callback);
    }


}