package demo.example.com.customarrayadapter.contentviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.example.com.customarrayadapter.MovieActivity;
import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.customviews.CustomImageView;
import demo.example.com.customarrayadapter.customviews.MyLeadingMarginSpan2;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback.OnImageLoadedListener;
import demo.example.com.customarrayadapter.interfaces.OrientationLoadedCallback.OnOrientationChangedListener;

public class TabFragment1 extends Fragment implements OnImageLoadedListener,
                                                    OnOrientationChangedListener {
    private static final int IMAGE_RATIO = 2;                    // This is the proportion of the custom view i.e IMAGE_SIZE = 2 is
                                                                // half the size, IMAGE_SIZE = 4 is a quarter of the size etc.
    private static final int IMAGE_PADDING = 10;
    private static final String TAG_MAIN_ACTIVITY_FRAGMENT = "tag_main_activity_fragment";


    MainActivityFragment mMainActivityFragment;

    ImageLoadedCallback listener;

    private ViewTarget<CustomImageView, GlideDrawable>
        viewTarget0,
        viewTarget1,
        viewTarget2;

    CustomImageView
        customView;

    Drawable mImage;

    Bitmap mBitmap;

    String mBitmapImage;

    SpannableString ss;
    int mWidth,
        mHeight,
        leftMargin,
        finalHeight,
        finalWidth;

    double aspectRatio;

    ImageView imageView;

    TextView messageView;

    View rootView;

    Context context;

    int numberOfGlobalLayoutCalled = 0;
    private int noParagraphs = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MovieActivity) activity;
        //activityCommunicator =  context;
        Log.d("LOG","***** What ? "+ context);
    }

    public void setImageLoadedListener(ImageLoadedCallback listener){
        this.listener = listener;
    }

    @Override
    public void onImageLoaded(String bitmap) {
        Log.d("LOG","** Image!? " + bitmap);
        Glide.
                with(getContext())
                //.load("http://image.tmdb.org/t/p/w342//fqe8JxDNO8B8QfOGTdjh6sPCdSC.jpg")
                .load(bitmap)
                .asBitmap()
                .placeholder(R.drawable.froyo)
                .animate(R.anim.slide_in_left)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap bitmap, GlideAnimation anim) {
                        Log.d("LOG","****onResourceReady() TabFragment "+ imageView );
                        customView.setOnOrientationChangedListener(
                                new   OnOrientationChangedListener() {
                                    /**
                                     * On screen (orientation) rotation the ratio is re-calculated.
                                     * @param width return the width of the custom view
                                     * @param height return the height of the custom view
                                     */
                                    @Override
                                    public void onOrientationChanged(int width, int height) {
                                        // Do something with bitmap here.
                                        mImage = new BitmapDrawable(getResources(), bitmap);
                                        imageView.setBackground(mImage);
                                        Log.d("LOG","Bitmap: " + mBitmap);
                                        aspectRatio = (double) mImage.getIntrinsicHeight() / mImage.getIntrinsicWidth();  // Use intrinsic dimensions to find the height of the image
                                        finalWidth = (width) / IMAGE_RATIO;                                          // The second image is half the width of the header image
                                        leftMargin = finalWidth + IMAGE_PADDING;                                    // padding (left margin) for image
                                        finalHeight = (int) (finalWidth * aspectRatio);
                                        imageView.getLayoutParams().width = finalWidth;                             // Once correct aspect ratio is calculated reset these dimensions to
                                        imageView.getLayoutParams().height = finalHeight;                           // the image.
                                        makeSpan();                                                                 // Wrap the text around image.
                                        Log.d("LOG", "** onDimensionChanged");
                                    }
                                });
                    }
                });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BitmapImage", mBitmap);
        Log.d("LOG","onSaveInstance: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null && rootView.getParent() != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
            Log.i("Test", "**************view.getParent(): " + rootView.getParent() + "    " + rootView);
        }

        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Restore saved state.
        //if (savedInstanceState != null) {
        //}

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mMainActivityFragment = (MainActivityFragment) fm.findFragmentByTag(TAG_MAIN_ACTIVITY_FRAGMENT);

        // If we haven't retained the worker fragment, then create it
        // and set this MainActivityFragment as the TaskFragment's target fragment.
        if (mMainActivityFragment == null) {
            mMainActivityFragment = new MainActivityFragment();
            mMainActivityFragment.setTargetFragment(this, 1);
            fm.beginTransaction().add(mMainActivityFragment, TAG_MAIN_ACTIVITY_FRAGMENT).commit();
        }



        //if (mTaskFragment.isRunning()) {
        //    mButton.setText(getString(R.string.cancel));
        //} else {
        //    mButton.setText(getString(R.string.start));
        //}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //retains fragment instance across Activity re-creation
        rootView = createView(savedInstanceState);

        messageView = (TextView) rootView.findViewById(R.id.message_view);
        customView = (CustomImageView) rootView.findViewById(R.id.custom_view);
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.info0);
        imageView =  (ImageView)  rootView.findViewById(R.id.text_image01);


        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }



    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public View createView(Bundle saveInstanceState){
        final View view = getActivity().getLayoutInflater().inflate(R.layout.tab_fragment_1, null, false);
        return view;
    }

    public void makeSpan(){
        /**
         * Get the text
         */
        //String plainText=getResources().getString(R.string.text_sample);
        String mText = "Older post, but since there is no accepted answer and I have just found solution for same problem in my app, I will post a solution.\n" +
                "\n" +
                "I have discovered that text without any line break works well. Text with a line break that splits the text into 2 parts in a way that the part before line break ends to the right of the image, and the part after line break starts already on next line bellow the image, this also works well.\n" +
                "\n" +
                "So what I do is I set left margin of the wrapping TextView's LayoutParams to the desired indent, and I set the text into TextView. Then I add OnGlobalLayoutListener, and inside onGlobalLayout callback, I count the position of the last character on the last line to the right of the image.";


        String plainTextToSearch=getResources().getString(R.string.text_sample);
        Spanned htmlText = Html.fromHtml(plainTextToSearch);
        SpannableString mSpannableString= new SpannableString(htmlText);

        // the pattern we want to search for
        Pattern p = Pattern.compile("<i>(\\S+)</i>");
        Matcher m = p.matcher(plainTextToSearch);

        // if we find a match, get the group
        if (m.find())
        {
            // get the matching group
            String codeGroup = m.group(1);

            // print the group
            //System.out.format("'%s'\n", codeGroup);
            Log.d("LOG","***** codeGroup: " + codeGroup);
        }

        int allTextStart = 0;
        int allTextEnd = htmlText.length() - 1;

        /**
         * Calculate the lines number = image height.
         * You can improve it... it is just an example
         */
        int lines;
        Rect bounds = new Rect();
        messageView.getPaint().getTextBounds(plainTextToSearch.substring(0,10), 0, 1, bounds);

        //float textLineHeight = mTextView.getPaint().getTextSize();
        float fontSpacing = messageView.getPaint().getFontSpacing();
        int height = bounds.bottom + bounds.height();
        //lines = (int) (finalHeight/(height)) + 1;
        lines = (int) (finalHeight/(fontSpacing)) + 1;
        /**
         * Build the layout with LeadingMarginSpan2
         */


        MyLeadingMarginSpan2 span = new MyLeadingMarginSpan2(lines, finalWidth + 10  );
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageView.setText(mSpannableString);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onOrientationChanged(int width, int height) {
        Log.d("LOG","*** onOrientationChanged()");
    }
}
