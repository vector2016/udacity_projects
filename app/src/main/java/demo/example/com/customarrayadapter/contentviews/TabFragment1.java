package demo.example.com.customarrayadapter.contentviews;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.example.com.customarrayadapter.MainActivity;
import demo.example.com.customarrayadapter.MovieActivity;
import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.customviews.CustomImageView;
import demo.example.com.customarrayadapter.customviews.MyLeadingMarginSpan2;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback.OnImageLoadedListener;
import demo.example.com.customarrayadapter.interfaces.OrientationLoadedCallback.OnOrientationChangedListener;
import demo.example.com.customarrayadapter.model.Movie;

public class TabFragment1 extends Fragment implements
                            //OnImageLoadedListener,
                            OnOrientationChangedListener,
                            TaskFragment. TaskCallbacks {
    private static final int IMAGE_RATIO = 2;                    // This is the proportion of the custom view i.e IMAGE_SIZE = 2 is
                                                                // half the size, IMAGE_SIZE = 4 is a quarter of the size etc.
    private static final int IMAGE_PADDING = 10;
    ImageLoadedCallback listener;
    CustomImageView
        customView;
    Drawable mImage;
    Bitmap mBitmap;
    String mBitmapImage;
    String overView;
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
    static Movie movie;
    ProgressBar progressBar;
    List<String> cachedImages = new ArrayList<>();
    List<SimpleTarget> targets = new ArrayList<>();
    private  FutureTarget<File> futureTarget;
    private ViewTarget<CustomImageView, GlideDrawable> viewTarget;
    private Thread mThread;
    private static String mImageHeader;
    private static Handler handler;
    private static File cacheFile;
    private int noCache = 0;

    public void setArguments(Bundle args) {
        super.setArguments(args);
        ArrayList<Movie> movieList = args.getParcelableArrayList("movie_Array_List_Extra");
        int position = args.getInt("position_extra",0);
        assert movieList != null;
        movie = movieList.get(position);


        // now adjust what ever you want to display
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    public void setImageLoadedListener(ImageLoadedCallback listener){
        this.listener = listener;
    }
    //@Override
    public void onImageLoaded(String mImageId, Context context) {
        //Log.d("LOG","** Image!? " + bitmap);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BitmapImage", mBitmap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null && rootView.getParent() != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
        }
        //displayBitmap("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath());
        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading);

        Log.d("LOG"," movie object sent from MainActivityFragment() :" + movie);

        String image = "http://image.tmdb.org/t/p/w342/" + movie.getPosterPath();
        String imageHeader = "http://image.tmdb.org/t/p/w342/" + movie.getBackdropPath();


        FutureTarget<File> futureCache = Glide.with(getContext()).load(imageHeader).downloadOnly(500,500);
        try {
            File fileCache = futureCache.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Glide.with(getContext())
                    .load(image)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .preload();


        Glide.with(getContext())
                .load(futureCache)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ViewTarget<CustomImageView, GlideDrawable>( customView ) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Log.d("LOG","** view **" + this.getView() + "** the image **" + resource.getCurrent());

                        this.view.setDrawable( resource.getCurrent() );
                    }
                });



        progressBar.setVisibility(View.VISIBLE);

        Glide.with(getContext())
                .load(image)
                .asBitmap()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        customView.setOnOrientationChangedListener(new OnOrientationChangedListener() {
                            /**
                             * On screen (orientation) rotation the ratio is re-calculated.
                             * @param width return the width of the custom view
                             * @param height return the height of the custom view
                             */

                            @Override
                            public void onOrientationChanged(int width, int height) {
                                // Do something with bitmap here.
                                mImage = new BitmapDrawable(getContext().getResources(), resource);
                                imageView.setBackground(mImage);
                                aspectRatio = (double) mImage.getIntrinsicHeight() / mImage.getIntrinsicWidth();  // Use intrinsic dimensions to find the height of the image
                                finalWidth = (width) / IMAGE_RATIO;                                          // The second image is half the width of the header image
                                leftMargin = finalWidth + IMAGE_PADDING;                                    // padding (left margin) for image
                                finalHeight = (int) (finalWidth * aspectRatio);
                                imageView.getLayoutParams().width = finalWidth;                             // Once correct aspect ratio is calculated reset these dimensions to
                                imageView.getLayoutParams().height = finalHeight;                           // the image.
                                makeSpan();                                                                 // Wrap the text around image.
                            }
                        });
                    }
                });
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


        //imageView.setTag(target);

        return view;
    }

    public void makeSpan(){
        /**
         * Get the text
         */
        String plainText=getResources().getString(R.string.text_sample);
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

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute(ArrayList<Movie> movies, Cursor c) {
        Log.d("LOG","onPostExecute( .......)" + movies);
    }

    public class MovieImage {
        private String image = "http://image.tmdb.org/t/p/w342//s7OVVDszWUw79clca0durAIa6mw.jpg";

        public String getImage() {
            return image;
        }
    }
}
