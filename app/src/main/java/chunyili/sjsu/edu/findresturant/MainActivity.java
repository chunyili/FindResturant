package chunyili.sjsu.edu.findresturant;

import android.Manifest;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Coordinate;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity
        implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private static final String FRAGMENT = "frament";
    private static final String TAG___Test = "SearchActivity";
    public ListView mainListView;
    // This is the Adapter being used to display the list's data.
    BusinessAdapter mAdapter;
    private static final int LOADER_ID = 42;
    static volatile boolean isCurrentLocation = true;

    private GoogleApiClient mGoogleApiClient;

    static volatile String query = "";
    private ArrayList<MyBusiness> myBusinesses;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setTitle(R.string.app_names);




        mAdapter = new BusinessAdapter(this, new ArrayList<MyBusiness>());
        mainListView = (ListView) findViewById(android.R.id.list);
        mainListView.smoothScrollToPosition(15);

        Log.e(TAG___Test, mainListView.toString());
        mainListView.setAdapter(mAdapter);

        Log.e(TAG___Test, "Init content");
        final Context context = this;

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailActivity.class);

                TextView tvName = (TextView) view.findViewById(R.id.business_id);

                intent.putExtra("id", tvName.getText().toString());
                startActivity(intent);

            }
        });
//        getLoaderManager().initLoader(LOADER_ID, null, this);
        Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
        setSupportActionBar(locatioinToolbar);
        getSupportActionBar().hide();
        setSupportActionBar(toolbar);
        try {
            handleIntent(getIntent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if(savedInstanceState != null) {
            Log.e(TAG___Test, "Loading content");
            myBusinesses = savedInstanceState.getParcelableArrayList(FRAGMENT);
            mainListView.setAdapter(new BusinessAdapter(MainActivity.this, myBusinesses));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.e(TAG___Test, "Saving content");
        outState.putParcelableArrayList(FRAGMENT, myBusinesses);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            Log.e(TAG___Test, "Loading content");
            myBusinesses = savedInstanceState.getParcelableArrayList(FRAGMENT);
            mainListView.setAdapter(new BusinessAdapter(MainActivity.this, myBusinesses));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private static volatile String typedLocation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem menuItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) menuItem.getActionView();
        Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
        SearchView locationsearchView =
                (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);
        locationsearchView.setOnQueryTextListener(this);


        Log.e(TAG___Test, getComponentName().toString());
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
                SearchView searchView =
                        (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);


                int searchImgId = MainActivity.this.getResources().getIdentifier("android:id/search_mag_icon", null, null);
                ImageView searchImage = (ImageView) searchView.findViewById(searchImgId);

                searchImage.setImageResource(R.drawable.location);
                searchView.setVisibility(View.VISIBLE);
                searchView.setQueryHint("Current Location");
                setSupportActionBar(locatioinToolbar);
                getSupportActionBar().show();

                Log.e(TAG___Test, "seachItem opened");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
                SearchView searchView =
                        (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);

                searchView.setVisibility(View.INVISIBLE);
                setSupportActionBar(locatioinToolbar);
                getSupportActionBar().hide();
                Log.e(TAG___Test, "seachItem closed");
                return false;
            }
        });
        return true;
    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        MenuItem menuItem = menu.findItem(R.id.searchItem);
//        LinearLayout linearLayout = (LinearLayout) menuItem.getActionView();
//        SearchView searchView =
//                (SearchView) linearLayout.findViewById(R.id.content_search_view);
//        Log.e(TAG___Test, getComponentName().toString());
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        // Handle navigation view item clicks here.
        Class fragClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragClass = SearchableActivity.class;
            Toast toast = Toast.makeText(context,"Search", Toast.LENGTH_SHORT);
            toast.show();

        } else if (id == R.id.nav_favorite) {
            fragClass = FavoriteActivity.class;
            Toast toast = Toast.makeText(context,"Favorite", Toast.LENGTH_SHORT);
            toast.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.CONTACT_PRESENCE,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts.LOOKUP_KEY,
    };


    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Log.e(TAG___Test, "  submit  " +  typedLocation);

        if(!isCurrentLocation && typedLocation.length() > 0){
            try {
                doMySearch();
                Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
                SearchView searchView =
                        (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);

                searchView.setVisibility(View.INVISIBLE);
                setSupportActionBar(locatioinToolbar);
                getSupportActionBar().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;


    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.e(TAG___Test, "textchange   " + newText);
        typedLocation = newText;


//                typedLocation = newText;



        if(typedLocation.trim().isEmpty()) {

            isCurrentLocation = true;

        }else{
            isCurrentLocation = false;
        }

        Log.e(TAG___Test, "iscurrentlocation " +  isCurrentLocation);
        Log.e(TAG___Test, "typed location  " + typedLocation);


        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        try {
            handleIntent(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleIntent(Intent intent) throws IOException {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);



            doMySearch();
        }
    }


    private void doMySearch() throws IOException {
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
                // Update UI text with the Business object.
                int totalNumberOfResult = searchResponse.total();
                ArrayList<Business> businesses = searchResponse.businesses();
                Log.e(TAG___Test, String.valueOf(businesses.size()));

                myBusinesses = new ArrayList<>();
                for(Business b: businesses){
                    myBusinesses.add(new MyBusiness(b));
                }
                Log.e(TAG___Test, String.valueOf(myBusinesses.size()));
                mainListView.setAdapter(new BusinessAdapter(MainActivity.this, myBusinesses));
//                MenuItem item = (MenuItem) findViewById(R.id.searchItem);
                SearchView searchView = (SearchView) findViewById(R.id.searchItem);

                // This method does not exist
                searchView.onActionViewCollapsed();
                Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
                setSupportActionBar(locatioinToolbar);
                getSupportActionBar().hide();
//                mFragment.setmBusiness(myBusinesses);
            }
            @Override
            public void onFailure(Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.e(TAG___Test, t.getMessage());
                t.printStackTrace();
            }
        };
        Log.e(TAG___Test, "" + isCurrentLocation);
        Log.e(TAG___Test, "here   " +typedLocation);

        if(isCurrentLocation){


//            CoordinateOptions coordinate = CoordinateOptions.builder()
//                    .latitude(37.11)
//                    .longitude(-122.88)
//                    .build();

//            Call<SearchResponse> call = yelpAPI.search(coordinate, params);
            Call<SearchResponse> call = yelpAPI.search("San Jose", params);

            call.enqueue(callback);

        }else{

            Log.e(TAG___Test, typedLocation);
            Call<SearchResponse> call = yelpAPI.search(typedLocation, params);

            call.enqueue(callback);
        }

//        Log.e(TAG___Test, query);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    LocationRequest mLocationRequest;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }




    @Override
    public void onConnectionSuspended(int i) {
        Log.i("suspend", "GoogleApiClient connection has been suspend");



    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("main_location", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    Double Latitude;
    Double Longitude;

    @Override
    public void onLocationChanged(android.location.Location location) {
        Latitude = Double.valueOf(location.getLatitude());
        Longitude = Double.valueOf(location.getLongitude());

    }


}
