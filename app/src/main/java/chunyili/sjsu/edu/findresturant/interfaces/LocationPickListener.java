package chunyili.sjsu.edu.findresturant.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jilongs on 3/26/16.
 */
public interface LocationPickListener {
    void onLocationPicked(LatLng latLng, String tag);
}
