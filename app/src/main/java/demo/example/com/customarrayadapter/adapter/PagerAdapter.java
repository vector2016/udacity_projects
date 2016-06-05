package demo.example.com.customarrayadapter.adapter;

//import android.support.v4.app.Fragment;
import android.app.Fragment;

//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import demo.example.com.customarrayadapter.contentviews.TabFragment1;
import demo.example.com.customarrayadapter.contentviews.TabFragment2;
import demo.example.com.customarrayadapter.contentviews.TabFragment3;
import demo.example.com.customarrayadapter.contentviews.TabFragment4;
import demo.example.com.customarrayadapter.contentviews.TabFragment5;

public class PagerAdapter extends SmartFragmentStatePagerAdapter  {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            case 3:
                TabFragment4 tab4 = new TabFragment4();
                return tab4;
            case 4:
                TabFragment5 tab5 = new TabFragment5();
                return tab5;
            default:
                return null;
        }
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