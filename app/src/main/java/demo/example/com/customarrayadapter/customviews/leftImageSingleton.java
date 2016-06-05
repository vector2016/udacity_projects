package demo.example.com.customarrayadapter.customviews;

/**
 * Created by Craig_B on 25/04/2016.
 */
public final class leftImageSingleton {

    private static final leftImageSingleton INSTANCE = new leftImageSingleton();

    private leftImageSingleton() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static leftImageSingleton getInstance() {
        return INSTANCE;
    }
}
