package demo.example.com.customarrayadapter.interfaces;
public class OrientationLoadedCallback{

/**
 * Callback interface through which the custom view will report the
 * task's progress and results back to the Fragment.
 */
    public interface OnOrientationChangedListener {
        void onOrientationChanged(int width, int height, String dim );
    }
}
