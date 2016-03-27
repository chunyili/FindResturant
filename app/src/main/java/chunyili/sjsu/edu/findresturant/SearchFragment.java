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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
public class SearchFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG = "SearchFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "here");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }


}
