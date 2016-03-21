package chunyili.sjsu.edu.findresturant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


public class PlacePickerActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView mName;
    private TextView mAddress;
    private TextView mAttributions;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "PlacesAPIActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "LocationTest";
    // Location client
    // Current location
    Location mCurrentLocation;
    boolean mConnected = false;
    private TextView mLocationView;


    // Object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    private ConnectionResult connectionResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        // Create Location Client
//        mLocationView = (TextView)findViewById(R.id.textView1);
//
//        setContentView(mLocationView);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
        mName = (TextView) findViewById(R.id.textView);
        mAddress = (TextView) findViewById(R.id.textView2);
        mAttributions = (TextView) findViewById(R.id.textView3);
        Button pickerButton = (Button) findViewById(R.id.pickerButton);
//
//        pickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { displayLocation(); }
//        });
//        final int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, PlacePickerActivity.this)
                .build();
//
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    if (ContextCompat.checkSelfPermission(PlacePickerActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PlacePickerActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_CODE);
                    } else {
                        callPlaceDetectionApi();
                    }

                }
            }
        });
//        pickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//
////                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                        //Execute location service call if user has explicitly granted ACCESS_FINE_LOCATION..
//
//                        PlacePicker.IntentBuilder intentBuilder =
//                                new PlacePicker.IntentBuilder();
//                        intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
//                        Intent intent = intentBuilder.build(PlacePickerActivity.this);
//                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
////                    }
//                } catch (GooglePlayServicesRepairableException
//                        | GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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
    private void displayLocation() {
//        if (mConnected) {
//            if (mCurrentLocation != null) {
                mLocationView.setText(mCurrentLocation.toString());
//            } else {
//                Log.d(TAG, "mCurrentLocation is NULL!");
//            }
//        } else {
//            Log.d(TAG, "mLocationClient is DISCONNECTED!");
//        }
    }

    private void callPlaceDetectionApi() throws SecurityException {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        Log.e(LOG_TAG, "in call back");

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {


            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                Log.e(LOG_TAG, likelyPlaces.toString());
                int count = 0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if(count == 0){
                        PlacePicker.IntentBuilder intentBuilder =
                                new PlacePicker.IntentBuilder();
                        LatLng currentLatLng = placeLikelihood.getPlace().getLatLng();
                        LatLng lowerLatLng = new LatLng(currentLatLng.latitude - 0.01, currentLatLng.longitude - 0.05);
                        LatLng upperLatLng = new LatLng(currentLatLng.latitude + 0.01, currentLatLng.longitude + 0.05);

                        LatLngBounds currentLocation = new LatLngBounds(
                                lowerLatLng, upperLatLng);
                        intentBuilder.setLatLngBounds(currentLocation);
                        Intent intent = null;
                        try {
                            intent = intentBuilder.build(PlacePickerActivity.this);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    }
                    count++;
                    Log.i(LOG_TAG, String.format("Place '%s' with " +
                                    "likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
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

            mName.setText(name);
            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "GoogleApiClient connection has been connected.");
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000); // Update location every second
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
//            ActivityCompat.requestPermissions(PlacePickerActivity.this,
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                PERMISSION_REQUEST_CODE);
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }


    @Override
    public void onLocationChanged(Location location) {
        mLocationView.setText("Location received: " + location.toString());
    }
}