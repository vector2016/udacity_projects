package demo.example.com.customarrayadapter.adapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import demo.example.com.customarrayadapter.contentviews.TabFragment1;
import demo.example.com.customarrayadapter.contentviews.TabFragment2;
import demo.example.com.customarrayadapter.contentviews.TabFragment3;
import demo.example.com.customarrayadapter.contentviews.TabFragment4;
import demo.example.com.customarrayadapter.contentviews.TabFragment5;
import demo.example.com.customarrayadapter.model.Movie;
import demo.example.com.customarrayadapter.contentviews.TabFragment1.Downloader;

public class PagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    private int mNumOfTabs;
    private Bundle bundle;
    private Context context;


    public PagerAdapter(Context context, FragmentManager fm, int NumOfTabs, Bundle bundle) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.bundle = bundle;
        this.context = context;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                tab1.setArguments(bundle);
                Log.d("LOG","** Tab1 ref:"+tab1);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                Log.d("LOG","** Tab2 ref:"+tab2);
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                Log.d("LOG","** Tab3 ref:"+tab3);
                return tab3;
            case 3:
                TabFragment4 tab4 = new TabFragment4();
                Log.d("LOG","** Tab4 ref:"+tab4);
                return tab4;
            case 4:
                TabFragment5 tab5 = new TabFragment5();
                Log.d("LOG","** Tab5 ref:"+tab5);
                return tab5;
            default:
                return null;
        }
    }
    @Override
    public int getItemPosition(Object object) {
        Log.d("LOG","Object() :" + object);
        return POSITION_NONE;
    }

    @Override
    public float getPageWidth (int position) {
        return 0.93f;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}