package chunyili.sjsu.edu.findresturant;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by ljtao on 3/25/16.
 */
public class FavoriteActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);

    }
}
