package chunyili.sjsu.edu.findresturant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jilongsun on 3/26/16.
 */
public class MyListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private BusinessAdapter mAdapter;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("list fragment", "here");
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        businesses = new ArrayList<>();
        mAdapter = new BusinessAdapter(getActivity(), businesses);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            Intent intent = new Intent(getContext(), DetailActivity.class);

            TextView tvName = (TextView) view.findViewById(R.id.business_id);

            intent.putExtra("id", tvName.getText().toString());
            startActivity(intent);

    }
}
