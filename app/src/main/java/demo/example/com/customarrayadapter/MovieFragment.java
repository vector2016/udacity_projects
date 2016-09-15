package demo.example.com.customarrayadapter;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import demo.example.com.customarrayadapter.adapter.PagerAdapter;
import demo.example.com.customarrayadapter.contentviews.repository.TaskFragment;

public class MovieFragment extends Fragment {
        final static String EXTRA_NAME = "extra_name";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private TaskFragment mTaskFragment;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //setContentView(R.layout.activity_detail);
        //Bundle bundle = getIntent().getExtras();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        View result = inflater.inflate(R.layout.activity_detail, container, false);


//------------------------
        TabLayout tabLayout = (TabLayout)result. findViewById(R.id.tab_layout);
        Log.d("LOG","iudgbuieygvbuisygvbsieo::::::  " + tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 5"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentManager fm = getFragmentManager();

        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        final ViewPager viewPager = (ViewPager)result. findViewById(R.id.pager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(12);
        viewPager.setOffscreenPageLimit(5);

//----------------------------------
        final PagerAdapter adapter = new PagerAdapter
                (getContext(), getFragmentManager(), tabLayout.getTabCount(), null);
        viewPager.setAdapter(adapter);

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("LOG","*** onPageScrolled: position: "+position
                        +" position offset: "+
                        positionOffset+" position offset pixels: "+
                        positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                /**
                 *

                    NOTE:
                    Fix notifyDataSetChanged() to a more performant alternative later.
                 *
                 */


                //mActionBar.setSelectedNavigationItem(position);
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                String mState;
                switch (state){
                    case 0: mState = "SCROLL_STATE_IDLE";
                            break;
                    case 1: mState = "SCROLL_STATE_DRAGGING";
                            break;
                    case 2: mState = "SCROLL_STATE_SETTLING";
                            break;
                    default: mState = "INVALID STATE";
                            break;
                }
                Log.d("LOG","*** onPageScrollStateChanged: " + mState);
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = 0;
                viewPager.setCurrentItem(tab.getPosition());
                if ((pos = tab.getPosition()) == 0){}
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(getActivity()).addApi(AppIndex.API).build();

        return result;
    } //----onCreateView ( .. )


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Movie Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://demo.example.com.customarrayadapter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Movie Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://demo.example.com.customarrayadapter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}