package demo.example.com.customarrayadapter;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.
    private TabFragment1 mTabFragment1;
    private int dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, new MainActivityFragment()).commit();

        }
        //setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar)   findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) MainActivity.this;
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
