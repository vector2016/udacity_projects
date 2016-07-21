package demo.example.com.customarrayadapter.contentviews;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.customviews.CustomImageView;
import demo.example.com.customarrayadapter.customviews.FutureStudioView;
import demo.example.com.customarrayadapter.customviews.MyLeadingMarginSpan2;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback;
import demo.example.com.customarrayadapter.interfaces.OrientationLoadedCallback;
import demo.example.com.customarrayadapter.model.Movie;

public class TabFragment1 extends Fragment {
    private static final int IMAGE_RATIO = 2;                    // This is the proportion of the custom view i.e IMAGE_SIZE = 2 is
                                                                // half the size, IMAGE_SIZE = 4 is a quarter of the size etc.
    private static final int IMAGE_PADDING = 10;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    ImageLoadedCallback listener;
    //FutureStudioView customView;
    Drawable mImage;
    Bitmap mBitmap;
    Button favoritesButton;
    String mBitmapImage;
    String overView;
    SpannableString ss;
    int mWidth,
        mHeight,
        leftMargin,
        finalHeight,
        finalWidth;
    double aspectRatio;
    ImageView imageView, imageView0 ;
    ImageView image_top;
    TextView messageView, dateView, ratingsView, durationView;
    View rootView;
    Context context;
    String image,imageHeader;
   static Movie movie;
    ProgressBar progressBar;
    List<String> cachedImages = new ArrayList<>();
    List<SimpleTarget> targets = new ArrayList<>();
    List <String> mKey = new ArrayList<>();
    private  FutureTarget<File> futureTarget;
    private ViewTarget<CustomImageView, GlideDrawable> viewTarget;
    private Thread mThread;
    private static String mImageHeader;
    private static Handler handler;
    private static File cacheFile;
    private int noCache = 0;
    private Downloader mFileDownloaderTask;
    private TabFragment1 tab;
    private FutureStudioView customView;

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

    //@Override

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

    public View createView(Bundle saveInstanceState){
        final View view = getActivity().getLayoutInflater().inflate(R.layout.tab_fragment_1, null, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retains fragment instance across Activity re-creation

        rootView = createView(savedInstanceState);
        messageView = (TextView) rootView.findViewById(R.id.message_view);

        customView = (FutureStudioView) rootView.findViewById(R.id.custom_view);
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.info0);
        imageView =  (ImageView)  rootView.findViewById(R.id.text_image01);
        dateView = (TextView) rootView.findViewById(R.id.date_view);
        durationView = (TextView) rootView.findViewById(R.id.duration_view);
        ratingsView = (TextView) rootView.findViewById(R.id.ratings_view);
        favoritesButton = (Button) rootView.findViewById(R.id.favorites_button);
        //imageView0 =  (ImageView)  rootView.findViewById(R.id.text_image02);

        Log.d("LOG"," movie object sent from MainActivityFragment() :" + movie);

        image =  IMAGE_URL + movie.getPosterPath();
        imageHeader = IMAGE_URL + movie.getBackdropPath();

        mFileDownloaderTask = new Downloader(Glide.with(getContext()));
        mFileDownloaderTask.execute(imageHeader,image);

        Result result = new Result();

        result.key.put("0",image);
        result.key.put("1", imageHeader);

        displayImages(result);
        result.key.clear();
        Log.d("LOG_TAG","onCreate()");
    }

    ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
        @Override
        public void animate(View view) {
            // if it's a custom view class, cast it here
            // then find subviews and do the animations
            // here, we just use the entire view for the fade animation
            view.setAlpha( 0f );

            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
            fadeAnim.setDuration( 2500 );
            fadeAnim.start();
        }
    };

    private RequestListener<String, Bitmap> requestListener =  new RequestListener<String, Bitmap>() {
        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Glide.with(getContext())
                    .load(imageHeader)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .animate(animationObject)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            customView.setImage(new BitmapDrawable(getContext().getResources(), resource));
                        }
                    });
            return false;
        }
    };

    private void displayImages(final Result result) {

        Glide.with(getContext())
                .load(image)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(requestListener)
                .animate(animationObject)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        customView.setOnOrientationChangedListener(new OrientationLoadedCallback.OnOrientationChangedListener() {
                            @Override
                            public void onOrientationChanged(int width, int height, String screenDim) {
                                mImage = new BitmapDrawable(getContext().getResources(), resource);
                                imageView.setImageDrawable(mImage);
                                dateView.setText(R.string.movie_date);
                                durationView.setText(R.string.movie_duration);
                                ratingsView.setText(R.string.movie_rating);
                                favoritesButton.setText(R.string.favorites);
                                aspectRatio = (double) mImage.getIntrinsicHeight() / mImage.getIntrinsicWidth();  // Use intrinsic dimensions to find the height of the image
                                finalWidth = (width)/ IMAGE_RATIO;                                                // The second image is half the width of the header image
                                leftMargin = finalWidth + IMAGE_PADDING;                                    // padding (left margin) for image
                                finalHeight = (int) (finalWidth * aspectRatio);
                                imageView.getLayoutParams().width = finalWidth;                             // Once correct aspect ratio is calculated reset these dimensions to
                                imageView.getLayoutParams().height = finalHeight;
                                makeSpan();
                                Log.d("LOG"," In onOrientationChanged() I have " + " Image: " + mImage + " finalWidth: " + finalWidth + " finalHeight: " + finalHeight + " and my image view: " + imageView );
                            }
                        });
                    }
                });
    }

    public class Downloader extends AsyncTask<String, File, Downloader.Result> {
        private static final String TAG = "Downloader";
        private final RequestManager glide;
        public Downloader(RequestManager glide) {
            this.glide = glide;
        }

        @Override protected Result doInBackground(String... params) {

            FutureTarget<File>[] requests;
            requests = new FutureTarget[params.length];
            // fire everything into Glide queue
            for (int i = 0; i < params.length; i++) {
                if (isCancelled()) {
                    break;
                }
                requests[i] = glide
                        .load(params[i])
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                ;
            }
            // wait for each item
            Result result = new Result();
            for (int i = 0; i < params.length; i++) {
                if (isCancelled()) {
                    for (int j = i; j < params.length; j++) {
                        if (requests[i] != null) Glide.clear(requests[i]);
                        result.failures.put(params[j], new CancellationException());
                    }
                    break;
                }
                try {
                    File file = requests[i].get(10, TimeUnit.SECONDS);

                    result.key.add(i, params[i]);
                    result.success.put(params[i], file);
                    Log.d(TAG,"params[i] ?" + params[i]);
                    publishProgress(file);
                } catch (Exception e) {
                    result.failures.put(params[i], e);
                } finally {
                    Glide.clear(requests[i]);
                }
            }
            return result;
        }

        @Override protected void onProgressUpdate(File... values) {
            for (File file : values) {
                Log.v(TAG, "Finished " + file);
            }
        }

        @Override protected void onPostExecute(Downloader.Result result) {
            Log.i(TAG, String.format(Locale.ROOT, "Downloaded %d files, %d failed.",
                    result.success.size(), result.failures.size()));
            result.key.clear();
        }

         class Result {
            Map<String, File> success = new HashMap<>();
            Map<String, Exception> failures = new HashMap<>();
            List<String> key = new ArrayList<>();
        }
    }

    class Result {
        Map<String,String> key = new HashMap<>();
    }

    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public void makeSpan(){

        Log.d("LOG","makeSpan()");
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
        //String plainTextToSearch=mText;

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
        bounds(bounds);

        //float textLineHeight = mTextView.getPaint().getTextSize();
        float fontSpacing = messageView.getPaint().getFontSpacing();
        int height = bounds.bottom + bounds.height();
        //lines = (int) (finalHeight/(height)) + 1;
        lines = (int) ( (finalHeight/(fontSpacing)) / 2.0) + 1;
        /**
         * Build the layout with LeadingMarginSpan2
         */


        MyLeadingMarginSpan2 span = new MyLeadingMarginSpan2(lines, finalWidth + 15  );
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageView.setText(mSpannableString);
    }
    public void bounds(Rect bound){
        Log.d("LOG", "*Width: " + bound.width());
        Log.d("LOG", "*Height: " + bound.height());
        Log.d("LOG", "*Left: " + bound.left);
        Log.d("LOG", "*Right: " + bound.right);
        Log.d("LOG", "*Top: " + bound.top);
        Log.d("LOG", "*Bottom: " + bound.bottom);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFileDownloaderTask.cancel(true);
        Log.d("LOG_TAG","onDestroy()");
    }
}
