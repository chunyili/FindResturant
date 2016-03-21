package chunyili.sjsu.edu.findresturant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

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
            TextView tvHome = (TextView) convertView.findViewById(R.id.business_rating);
            // Populate the data into the template view using the data object
            tvName.setText(business.name());
            tvHome.setText("" + business.rating());
            // Return the completed view to render on screen
            return convertView;
        }

}
