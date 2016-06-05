package demo.example.com.customarrayadapter.contentviews;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ViewTarget;

import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.customviews.CustomImageView;

public class TabFragment2 extends Fragment {
    private ViewTarget<CustomImageView, GlideDrawable> viewTarget0,
            viewTarget1, viewTarget2;
    CustomImageView customView0, customView1;
    Drawable drawable;
    ImageView imageView;
    ColorDrawable drawableColor;
    int mWidth;
    int mHeight;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null && rootView.getParent() != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);

            Log.d("Test", "**************view.getParent(): " + rootView.getParent() + "    " + rootView);
        }
        return  rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retains fragment instance across Activity re-creation

        rootView = createView(savedInstanceState);
        setRetainInstance(true);

    }

    public View createView(Bundle saveInstanceState){
        View view = getActivity().getLayoutInflater().inflate(R.layout.tab_fragment_2, null, false);
        //customView1 = (AspectRatioImageView)rootView. findViewById(R.id.imageView1);
        //customView0.set(0, (AspectRatioImageView) rootView.findViewById(R.id.imageView));
        //customView0.set(1, (AspectRatioImageView) rootView.findViewById(R.id.imageView));

        //loadImageViewTargetTop();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadImageViewTarget();

    }
}