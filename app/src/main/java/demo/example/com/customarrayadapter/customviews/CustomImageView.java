package demo.example.com.customarrayadapter.customviews;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.Point;
    import android.graphics.PorterDuff;
    import android.graphics.drawable.BitmapDrawable;
    import android.graphics.drawable.Drawable;
    import android.text.Layout;
    import android.text.StaticLayout;
    import android.text.TextPaint;
    import android.util.AttributeSet;
    import android.util.DisplayMetrics;
    import android.util.Log;
    import android.view.Display;
    import android.view.View;
    import android.view.WindowManager;
    import android.widget.FrameLayout;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
    import com.bumptech.glide.load.resource.drawable.GlideDrawable;
    import com.bumptech.glide.request.animation.GlideAnimation;
    import com.bumptech.glide.request.target.SimpleTarget;
    import com.bumptech.glide.request.target.ViewTarget;

    import java.io.File;

    import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback;
    import demo.example.com.customarrayadapter.interfaces.OrientationLoadedCallback.OnOrientationChangedListener;

public class CustomImageView extends View implements
                                ImageLoadedCallback.OnImageLoadedListener {

        static private Drawable mDrawableLeft, mDrawableRight;
        private static final int mColumnCount = 2;
        private static int mCount = 0;
        private int mLheight, mRheight, wleft, wtop, leftMargin;
        private TextPaint mTextPaint;
        private StaticLayout mTextLayout;
        private CharSequence mText;
        private Point mTextOrigin;
        private Bitmap mBitmap;

        private OnOrientationChangedListener listener;

        public void setOnOrientationChangedListener(OnOrientationChangedListener listener){
            this.listener = listener;
        }

        public void initialize( final Context context) {
        //Glide.
        //        with(context)
        //    .load("http://image.tmdb.org/t/p/w342//sSvgNBeBNzAuKl8U8sP50ETJPgx.jpg")
        //        .asBitmap()
        //        .into(new SimpleTarget<Bitmap>() {
        //  public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
        //                // Do something with bitmap here.
        //                setDrawable(bitmap);
        //            }
        //        });
    }

    private static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }


    public void setDrawable(Bitmap bitmap){
        mDrawableLeft = new GlideBitmapDrawable(getResources(), bitmap);
        mBitmap = bitmap;
        Log.d("LOG","setdrawable:"+mBitmap);

        //mDrawableLeft = drawable;
        updateContentBounds();
        invalidate();
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }

    public CustomImageView(Context context) {
        this(context, null,0);
        //if (!isInEditMode())
            initialize(context);
    }
    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //if (!isInEditMode())
            initialize(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //if (!isInEditMode())
            initialize(context);
    }

    public int getCustomWidth(){
        return getWidth();
    }

    public int getCustomHeight(){
        return (mLheight + mRheight);
    }

    public void updateContentBounds(){
        if (mDrawableLeft != null) {

            double ratio = (double) mDrawableLeft.getIntrinsicHeight() / mDrawableLeft.getIntrinsicWidth();
            int left = 0;
            int top = 0;
            int w = left + getWidth();
            int h = (int) (top + (getWidth() * ratio));
            mLheight = h;
            //mDrawableLeft.setColorFilter(0xffff0000, PorterDuff.Mode.LIGHTEN);
            mDrawableLeft.setBounds(left,
                    top,
                    w,
                    h);
            Log.d("LOG","width: "+w+ " height: "+h);
            // Make sure the width and height are actual relevant dimensions before proceeding.
            if ((listener != null) &&
                            (w > 0) &&
                            (h > 0)) {
                listener.onOrientationChanged(w, h);
            }
        }
    }
        public void setTextLayout(String text){
            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(16);
            mText = text;
            float textWidth = mTextPaint.measureText(mText, 0, mText.length()/3);
            mTextLayout = new StaticLayout(mText, mTextPaint, (int)textWidth,
                    Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
        }


        @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

        if (mDrawableLeft != null) {
            mDrawableLeft.draw(canvas);
        }
        Log.d("LOG","bitrmapper:"+mBitmap);
        //if (mBitmap != null) {
        //    canvas.drawBitmap(mBitmap,0,0,null);
        //}

        //if (mTextLayout != null){
        //    canvas.save();
        //    canvas.translate(mTextOrigin.x, mTextOrigin.y);
        //    mTextLayout.draw(canvas);
        //    canvas.restore();
        //}

        //if (mDrawableRight != null) {
        //    mDrawableRight.draw(canvas);
        //}
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updateContentBounds();
        //Get the width measurement
        int widthSize = View.resolveSize(getCustomWidth(), widthMeasureSpec);

        //Get the height measurement
        int heightSize = View.resolveSize(getCustomHeight() , heightMeasureSpec);

        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public void onImageLoaded(String bitmap) {
        Log.d("LOG","** onImageLoaded()");

    }
}