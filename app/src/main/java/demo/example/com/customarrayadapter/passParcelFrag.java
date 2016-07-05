package demo.example.com.customarrayadapter;

/**
 * Created by Craig_B on 27/04/2016.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.io.Serializable;

import demo.example.com.customarrayadapter.adapter.AndroidFlavorCursorRecyclerViewAdapter;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class passParcelFrag{
    public Object id;

    public passParcelFrag(Object ids) {
        id = ids;
    }

    public passParcelFrag(){}

    public Object getFrag(){
        return id;
    }

    protected passParcelFrag(Parcel in) {
        id = (Object) in.readValue(Object.class.getClassLoader());
    }



}
