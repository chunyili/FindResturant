package chunyili.sjsu.edu.findresturant;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chunyili.sjsu.edu.findresturant.interfaces.YelpBusinessCallback;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailActivity extends AppCompatActivity 
implements YelpBusinessCallback{
    private ImageView restaurantIcon;
    private TextView restaurantName;
    private ImageView restaurantRating;
    private TextView restaurantReviews;
    public String iconURL;
    public String ratingURL;
    private double latitude;
    private double longtitue;

    private String mapUrl;

    private TextView restaurantAdress;
    private TextView restaurantPhoneNO;
    public static String MYFAVORITE = "MyPrefs";
    Intent intent;
    Menu menu;
    Button button;
    SharedPreferences sharedpreferences;
    private YelpSearchUtil yelpSearchUtil;
    private DBUitl dbUitl;

    private String TAG___Test = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yelpSearchUtil = new YelpSearchUtil();
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbUitl = new DBUitl(this);

        getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().setTitle(R.string.app_names);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        intent = getIntent();
        final String id = intent.getExtras().getString("id");
        yelpSearchUtil.setmBusinessCallback(this);
        yelpSearchUtil.findBusiness(id);
        restaurantName.setText(intent.getExtras().get("id").toString());

        button = (Button) findViewById(R.id.restaurant_button);
        sharedpreferences = getSharedPreferences(MYFAVORITE, Context.MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(DetailActivity.this, "Thanks", Toast.LENGTH_SHORT);
                toast.show();
                Log.e(TAG___Test, "like " + id);
                dbUitl.addLike(id);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.test:
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBusinessReturn(MyBusiness myBusiness) {
        restaurantName = (TextView) findViewById(R.id.restaurant_name);
        restaurantAdress = (TextView) findViewById(R.id.restaurant_address);
        restaurantIcon = (ImageView) findViewById(R.id.restaurant_icon);
        restaurantPhoneNO = (TextView) findViewById(R.id.restaurant_phone_No);
        restaurantReviews = (TextView) findViewById(R.id.restaurant_reviews);
        ImageView map = (ImageView) findViewById((R.id.map));

        iconURL = myBusiness.getBusiness().imageUrl();
        new DownloadImageTask(restaurantIcon)
                .execute(iconURL);
        restaurantRating = (ImageView) findViewById(R.id.restaurant_rating);
        ratingURL = myBusiness.getBusiness().ratingImgUrlLarge();
        latitude = myBusiness.getBusiness().location().coordinate().latitude();
        longtitue = myBusiness.getBusiness().location().coordinate().longitude();

        mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + ","
                + longtitue+ "&zoom=20&size=1500x800&maptype=roadmap&markers=color:red%7Clabel:name%7C" + latitude + "," + longtitue;


        new DownloadImageTask(restaurantRating)
                .execute(myBusiness.getBusiness().ratingImgUrlLarge());

        new DownloadImageTask(map).execute(mapUrl);

        // Populate the data into the template view using the data object
        StringBuilder sb = new StringBuilder();
        String address = myBusiness.getBusiness().location().address().toString();


        for(int i = 0; i < address.length() - 1; i ++){
            if(address.charAt(i) == '[' ){
                continue;
            }else{
                sb.append(address.charAt(i));
            }
        }


        restaurantName.setText(myBusiness.getBusiness().name());
        if (myBusiness.getBusiness().phone() != null) {
            restaurantPhoneNO.setText("Call " + myBusiness.getBusiness().phone().toString());
        } else {
            restaurantPhoneNO.setText("No data!");
        }
        if (myBusiness.getBusiness().reviewCount() != null) {
            restaurantReviews.setText(myBusiness.getBusiness().reviewCount() + " Reviews");
        } else {
            restaurantReviews.setText("No data!");
        }

        restaurantAdress.setText(""+ sb.toString()+ ", " + myBusiness.getBusiness().location().city());
    }
}
