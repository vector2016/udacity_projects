package demo.example.com.customarrayadapter;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import demo.example.com.customarrayadapter.contentviews.MainActivityFragment;
import demo.example.com.customarrayadapter.contentviews.TabFragment1;

/**
 * MainActivity starts a UI fragment which will display the screen's UI.
 *
 * To run this MainActivity instead of the one in the
 * <code>com.adp.retaintask</code> package, you will need to manually change the
 * default launcher Activity in the <code>AndroidManifest.xml</code> file.
 */
public class MainActivity extends FragmentActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.
    private TabFragment1 mTabFragment1;
    private int dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (DEBUG) Log.i(TAG, "onCreate(Bundle)");



        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            MainActivityFragment mainFragment = new MainActivityFragment();


            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mainFragment).commit();

        }

        //Toolbar toolbar = (Toolbar)   findViewById(R.id.toolbar);
        //AppCompatActivity activity = (AppCompatActivity) MainActivity.this;
        //activity.setSupportActionBar(toolbar);


    }

    /************************/
    /***** OPTIONS MENU *****/
    /************************/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
