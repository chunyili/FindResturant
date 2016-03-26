package chunyili.sjsu.edu.findresturant;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelp.clientlib.entities.Business;

import java.io.IOException;

/**
 * Created by jilongsun on 3/25/16.
 */
public class MyBusiness implements Parcelable {
    public static final String BUSINESS_JSON = "business_json";
    private Business business;
    protected MyBusiness(Parcel in) {
        Bundle bundle = in.readBundle();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = bundle.getString(BUSINESS_JSON);

        try {
            business = mapper.readValue(jsonInString, Business.class);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public MyBusiness(){

    }
    public MyBusiness(Business business){
        this.business = business;
    }

    public static final Creator<MyBusiness> CREATOR = new Creator<MyBusiness>() {
        @Override
        public MyBusiness createFromParcel(Parcel in) {
            return new MyBusiness(in);
        }

        @Override
        public MyBusiness[] newArray(int size) {
            return new MyBusiness[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // create a bundle for the key value pairs
        Bundle bundle = new Bundle();
        ObjectMapper mapper = new ObjectMapper();

//Object to JSON in String
        try {
            String jsonInString = mapper.writeValueAsString(business);

            // insert the key value pairs to the bundle
            bundle.putString(BUSINESS_JSON, jsonInString);

            // write the key value pairs to the parcel
            dest.writeBundle(bundle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
