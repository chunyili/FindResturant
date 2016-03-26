package chunyili.sjsu.edu.findresturant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jilongsun on 3/26/16.
 */
public class ButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.button_fragment, container, false);
        view.findViewById(R.id.radioButton);
        view.findViewById(R.id.radioButton2);
        Log.e("fragment", " button fragment");
        return view;
    }
}

