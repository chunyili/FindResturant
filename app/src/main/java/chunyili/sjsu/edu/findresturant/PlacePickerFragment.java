package chunyili.sjsu.edu.findresturant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import chunyili.sjsu.edu.findresturant.interfaces.LocationPickListener;


public class PlacePickerFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,

        LocationListener {
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "PlacesAPIActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;

    public LocationPickListener getmListener() {
        return mListener;
    }

    public void setmListener(LocationPickListener mListener) {
        this.mListener = mListener;
    }

    private LocationPickListener mListener;


    private static final LatLngBounds BOUNDS_SAN_JOSE = new LatLngBounds(
            new LatLng(37.33, -121.890), new LatLng(37.34, -121.880));

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static String TAG = "LocationTest";
    Location mCurrentLocation;
    boolean mConnected = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_default, container, false);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .build();

        PlacePicker.IntentBuilder intentBuilder =
                new PlacePicker.IntentBuilder();


        intentBuilder.setLatLngBounds(BOUNDS_SAN_JOSE);
        Intent intent = null;
        try {
            intent = intentBuilder.build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
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
                    if (count == 0) {

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

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(getActivity(), data);
            Log.e(TAG, place.getAddress().toString());
            LatLng latitude = place.getLatLng();
            mListener.onLocationPicked(latitude, place.getAddress().toString());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
    }
}