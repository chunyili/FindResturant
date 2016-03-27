package chunyili.sjsu.edu.findresturant;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import chunyili.sjsu.edu.findresturant.interfaces.YelpBusinessCallback;

/**
 * Created by jilongs on 3/26/16.
 */
public class LikeFragment extends ListFragment implements AdapterView.OnItemClickListener  {
    private final static String TAG = "likeFragment";
    private BusinessAdapter mAdapter;
    private YelpSearchUtil yelpSearchUtil;

    public ArrayList<MyBusiness> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<MyBusiness> businesses) {
        Log.e("list fragment", "setting list items");
        this.businesses = businesses;
        mAdapter = new BusinessAdapter(getActivity(), businesses);
        setListAdapter(mAdapter);
    }

    private ArrayList<MyBusiness> businesses;

    private DBUitl dbUitl;
    private ArrayList<String> jsonObjectList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        yelpSearchUtil = new YelpSearchUtil();
        dbUitl = new DBUitl(getContext());
        jsonObjectList = dbUitl.getLikes();
        final ArrayList<MyBusiness> myBusinesses = new ArrayList<>();
        for(String id: jsonObjectList){
            yelpSearchUtil.setmBusinessCallback(new YelpBusinessCallback() {
                @Override
                public void onBusinessReturn(MyBusiness myBusiness) {
                    myBusinesses.add(myBusiness);
                    mAdapter = new BusinessAdapter(getActivity(), myBusinesses);
                    setListAdapter(mAdapter);
                }
            });
            yelpSearchUtil.findBusiness(id);
        }
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Log.e(TAG, "items clicked");
        Intent intent = new Intent(getContext(), DetailActivity.class);

        TextView tvName = (TextView) v.findViewById(R.id.business_id);

        intent.putExtra("id", tvName.getText().toString());
        startActivity(intent);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Log.e(TAG, "items clicked");
        Intent intent = new Intent(getContext(), DetailActivity.class);

        TextView tvName = (TextView) view.findViewById(R.id.business_id);

        intent.putExtra("id", tvName.getText().toString());
        startActivity(intent);

    }
}
