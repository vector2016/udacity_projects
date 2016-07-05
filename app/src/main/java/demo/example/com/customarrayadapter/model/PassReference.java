package demo.example.com.customarrayadapter.model;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import demo.example.com.customarrayadapter.adapter.AndroidFlavorCursorRecyclerViewAdapter;
import demo.example.com.customarrayadapter.contentviews.MainActivityFragment;

/**
 * Created by Craig_B on 16/06/2016.
 */
public class PassReference implements Parcelable {

    private ArrayList<Movie> object;


    public ArrayList<Movie> getReference() {
        return object;
    }
    public void setReference(ArrayList<Movie> object) {
        this.object = object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.object);
    }

    public PassReference() {
    }

    protected PassReference(Parcel in) {
        this.object = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Parcelable.Creator<PassReference> CREATOR = new Parcelable.Creator<PassReference>() {
        @Override
        public PassReference createFromParcel(Parcel source) {
            return new PassReference(source);
        }

        @Override
        public PassReference[] newArray(int size) {
            return new PassReference[size];
        }
    };
}