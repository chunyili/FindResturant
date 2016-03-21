package chunyili.sjsu.edu.findresturant;

import android.Manifest;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener,
        NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG___Test = "SearchActivity";
    public ListView mainListView;
    // This is the Adapter being used to display the list's data.
    BusinessAdapter mAdapter;
    private static final int LOADER_ID = 42;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAdapter = new BusinessAdapter(this,
                new ArrayList<Business>());
        mainListView = (ListView) findViewById(android.R.id.list);
        Log.e(TAG___Test, mainListView.toString());
        mainListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
//        Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
//        locatioinToolbar.animate().translationY(-locatioinToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.e(TAG___Test, "clicked");
        switch (item.getItemId()) {

            case R.id.searchItem:
                //set visibilty
                //do what ever you wantLinearLayout linearLayout = (LinearLayout) menuItem.getActionView();
                item.setActionView(R.layout.search_combo);
                LinearLayout linearLayout = (LinearLayout) item.getActionView();
                Toolbar locatioinToolbar = (Toolbar) findViewById(R.id.location_toolbar);
                locatioinToolbar.setVisibility(View.VISIBLE);
                locatioinToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                SearchView searchView =
                        (SearchView) linearLayout.findViewById(R.id.content_search_view);
                searchView.setVisibility(View.VISIBLE);
                searchView.setQueryHint("Search Resturant");
                searchView =
                        (SearchView) locatioinToolbar.findViewById(R.id.location_search_view);
                searchView.setVisibility(View.VISIBLE);
                searchView.setQueryHint("Current Location");
                Log.e(TAG___Test, "seachItem clicked");

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    100);
            return null;
        }
        Uri baseUri;

            baseUri = ContactsContract.Contacts.CONTENT_URI;


        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
        return new CursorLoader(MainActivity.this, baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        //mAdapter.add((Business) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.clear();
    }
}
