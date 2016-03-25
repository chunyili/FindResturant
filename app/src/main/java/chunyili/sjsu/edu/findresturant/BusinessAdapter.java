package chunyili.sjsu.edu.findresturant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.clientlib.entities.Business;

import java.io.InputStream;
import java.util.ArrayList;
import java.lang.*;

/**
 * Created by jilongsun on 3/19/16.
 */
public class BusinessAdapter extends ArrayAdapter<Business> {

    public BusinessAdapter(Context context, ArrayList<Business> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Business business = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.container_list_item_view, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.business_name);
        TextView tvId = (TextView) convertView.findViewById(R.id.business_id);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.location);
        //  TextView tvHome = (TextView) convertView.findViewById(R.id.business_rating);

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.image))
                .execute(business.imageUrl());
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.rating))
                .execute(business.ratingImgUrlSmall());

        // Populate the data into the template view using the data object
        StringBuilder sb = new StringBuilder();
        String address = business.location().address().toString();


        for(int i = 0; i < address.length() - 1; i ++){
            if(address.charAt(i) == '[' ){
                continue;
            }else{
                sb.append(address.charAt(i));
            }
        }


        tvName.setText(business.name());
        tvId.setText(business.id());

        tvLocation.setText(""+ sb.toString()+ ", " + business.location().city());

        return convertView;
    }

}



class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
