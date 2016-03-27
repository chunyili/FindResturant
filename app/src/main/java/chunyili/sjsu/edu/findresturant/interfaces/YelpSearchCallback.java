package chunyili.sjsu.edu.findresturant.interfaces;

import java.util.ArrayList;

import chunyili.sjsu.edu.findresturant.MyBusiness;

/**
 * Created by jilongs on 3/26/16.
 */
public interface YelpSearchCallback {
    void onSearchReturned(ArrayList<MyBusiness> myBusinesses);
}
