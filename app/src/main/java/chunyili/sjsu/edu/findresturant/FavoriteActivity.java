package chunyili.sjsu.edu.findresturant;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ljtao on 3/25/16.
 */
public class FavoriteActivity extends Activity {
    public static String MYFAVORITE = "MyPrefs";
    private ImageView restaurantIcon;
    private TextView restaurantName;
    private ImageView restaurantRating;
    private TextView restaurantReviews;
    public String iconURL;
    public String ratingURL;
    private TextView restaurantAdress;
    private TextView restaurantPhoneNO;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);
        restaurantName = (TextView) findViewById(R.id.name1);
        restaurantReviews = (TextView) findViewById(R.id.name2);
        restaurantPhoneNO = (TextView) findViewById(R.id.name3);
        restaurantAdress = (TextView) findViewById(R.id.name4);
        SharedPreferences sharedpreferences = getSharedPreferences(MYFAVORITE, Context.MODE_PRIVATE);
        restaurantName.setText(sharedpreferences.getString("NameKey", ""));
        restaurantAdress.setText(sharedpreferences.getString("AdressKey", ""));
        restaurantPhoneNO.setText(sharedpreferences.getString("PhoneKey", ""));
        restaurantReviews.setText(sharedpreferences.getString("ReviewKey", ""));
        iconURL = sharedpreferences.getString("IconURLKey", "");
        ratingURL = sharedpreferences.getString("RatingKey", "");
        restaurantIcon = (ImageView) findViewById(R.id.restaurant_icon);
        new DownloadImageTask(restaurantIcon)
                .execute(iconURL);
        restaurantRating = (ImageView) findViewById(R.id.restaurant_rating);
        new DownloadImageTask(restaurantRating)
                .execute(ratingURL);

    }
}
