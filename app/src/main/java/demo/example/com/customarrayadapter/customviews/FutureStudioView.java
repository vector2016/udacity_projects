package demo.example.com.customarrayadapter.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.interfaces.OrientationLoadedCallback;

public class FutureStudioView extends FrameLayout {
    LinearLayout lv;
    ImageView iv;
    TextView tv;
    Drawable drawable;
    Context context;
    String screenRes;
    int width, height;
    private OrientationLoadedCallback.OnOrientationChangedListener listener;

    public void initialize(Context context) {
        this.context = context;
        inflate( context, R.layout.custom_view_futurestudio, this );
        lv = (LinearLayout) findViewById(R.id.custom_layout);
        iv = (ImageView) findViewById( R.id.custom_view_image );
        tv = (TextView) findViewById( R.id.custom_view_text );
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

    public FutureStudioView(Context context, AttributeSet attrs) {
        super( context, attrs );
        initialize( context );
    }

    public FutureStudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initialize( context );
    }

    public void setOnOrientationChangedListener(OrientationLoadedCallback.OnOrientationChangedListener listener){
        this.listener = listener;
    }

    public void getLocalVisibleRect(){
        Rect rectf = new Rect();
        iv.getLocalVisibleRect(rectf);

        Log.d("WIDTH        :", String.valueOf(rectf.width()));
        Log.d("HEIGHT       :", String.valueOf(rectf.height()));
        Log.d("left         :", String.valueOf(rectf.left));
        Log.d("right        :", String.valueOf(rectf.right));
        Log.d("top          :", String.valueOf(rectf.top));
        Log.d("bottom       :", String.valueOf(rectf.bottom));
        rotationChanged(rectf.top, rectf.left, rectf.right, rectf.bottom);
    }

    ViewTreeObserver.OnGlobalLayoutListener myOnGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener(){
                @Override
                public void onGlobalLayout() {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        lv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    getLocalVisibleRect();
                }
            };

    public void setImage(final Drawable drawable) {
        iv = (ImageView) findViewById( R.id.custom_view_image );
        iv.setImageDrawable(drawable);
        lv.getViewTreeObserver().addOnGlobalLayoutListener(myOnGlobalLayoutListener);
    }

    public void rotationChanged(int top, int left, int right, int bottom){
        /*
            Notes:
            Use dimensions returned from onLayoutChanged( ... ) instead of drawable.
         */
        Drawable drawable = iv.getDrawable();
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            return;
        }
        int drawWidth = right - left;
        int drawHeight = bottom - top;

        if (drawable != null) {
            double ratio = (double) drawHeight / drawWidth;
            int w = left + drawWidth;
            int h = (int) (top + (drawWidth * ratio));

            // Make sure the width and height are actual relevant dimensions before proceeding.
            if ( (listener != null) && (w > 0) && (h > 0) ) {
                listener.onOrientationChanged(w, h, "");
            }
        }
    }
}