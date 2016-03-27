package chunyili.sjsu.edu.findresturant;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import chunyili.sjsu.edu.findresturant.interfaces.YelpSortChangeListner;

/**
 * Created by jilongsun on 3/26/16.
 */
public class ButtonFragment extends Fragment {
    public YelpSortChangeListner getmListner() {
        return mListner;
    }

    public void setmListner(YelpSortChangeListner mListner) {
        this.mListner = mListner;
    }

    private YelpSortChangeListner mListner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.button_fragment, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        view.findViewById(R.id.radioButton);
        view.findViewById(R.id.radioButton2);
        Log.e("fragment", " button fragment");
        radioGroup.setOnCheckedChangeListener(next_Listener);
        return view;
    }
    private RadioGroup.OnCheckedChangeListener next_Listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Log.e("test", "button clicked");
            String sortBy = "";
            if(checkedId == R.id.radioButton) {
                sortBy = "1";
            }else{
                sortBy = "0";
            }
            Log.e("test", sortBy);
            mListner.onSortChanged(sortBy);
        }
    };

}

