package chunyili.sjsu.edu.findresturant;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;

import chunyili.sjsu.edu.findresturant.interfaces.YelpSearchCallback;
import chunyili.sjsu.edu.findresturant.interfaces.YelpSortChangeListner;

public class MainActivity extends AppCompatActivity
        implements
        YelpSortChangeListner,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        YelpSearchCallback,
        NavigationView.OnNavigationItemSelectedListener {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final String TAG___Test = "SearchActivity";
    static volatile boolean isCurrentLocation = true;
    static volatile String query = "";
    private static volatile String typedLocation;
    private String sortBy = "";

    private Fragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setTitle(R.string.app_names);




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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.e(TAG___Test, "clicked nav item");
        Context context = getApplicationContext();
        // Handle navigation view item clicks here.
        Class fragClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Log.e(TAG___Test, "clicked search");
            Toast toast = Toast.makeText(context, "Search", Toast.LENGTH_SHORT);
            toast.show();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            mFragment = new SearchFragment();
            fragmentTransaction.replace(R.id.fragment_container, mFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_favorite) {
            Log.e(TAG___Test, "clicked favorite");
            fragClass = FavoriteActivity.class;
            Toast toast = Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT);
            toast.show();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            mFragment = new LikeFragment();
            fragmentTransaction.replace(R.id.fragment_container, mFragment);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Log.e(TAG___Test, "  submit  " + typedLocation);

        if (!isCurrentLocation && typedLocation.length() > 0) {
            doMySearch();
            Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
            SearchView searchView =
                    (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);

            searchView.setVisibility(View.INVISIBLE);
            setSupportActionBar(locatioinToolbar);
            getSupportActionBar().hide();
            return true;
        }
        return false;


    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.e(TAG___Test, "textchange   " + newText);
        typedLocation = newText;

        if (typedLocation.trim().isEmpty()) {

            isCurrentLocation = true;

        } else {
            isCurrentLocation = false;
        }

        Log.e(TAG___Test, "iscurrentlocation " + isCurrentLocation);
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

    public void doMySearch() {
        if (mFragment instanceof SearchFragment) {
            SearchFragment fragment = (SearchFragment)mFragment;

            fragment.setSortBy(sortBy);
            fragment.setIsCurrentLocation(isCurrentLocation);
            fragment.setQuery(query);
            fragment.setTypedLocation(typedLocation);
            fragment.setmSearchCallback(this);
            try {
                fragment.doMySearch();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ButtonFragment buttonFragment = (ButtonFragment) mFragment
                    .getChildFragmentManager().findFragmentById(R.id.fragment0);
            buttonFragment.setmListner(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            Log.e(TAG___Test + " test ", name.toString());
            Log.e(TAG___Test + "test ", address.toString());

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSearchReturned(ArrayList<MyBusiness> myBusinesses) {
        MyListFragment fragment = (MyListFragment) mFragment.getChildFragmentManager().findFragmentById(R.id.fragment1);
        fragment.setBusinesses(myBusinesses);

        SearchView searchView = (SearchView) findViewById(R.id.searchItem);

        searchView.onActionViewCollapsed();
        Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
        setSupportActionBar(locatioinToolbar);
        getSupportActionBar().hide();
    }


    @Override
    public void onSortChanged(String sortBy) {
        this.sortBy = sortBy;
        doMySearch();
    }
}
