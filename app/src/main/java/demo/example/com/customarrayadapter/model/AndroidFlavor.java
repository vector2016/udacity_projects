package demo.example.com.customarrayadapter.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by poornima-udacity on 6/26/15.
 */
//public class AndroidFlavor {
//    String versionName;
//    String versionNumber;
//    int image; // drawable reference id
//
//    public AndroidFlavor(String vName, String vNumber, int image)
//    {
//        this.versionName = vName;
//        this.versionNumber = vNumber;
//        this.image = image;
//    }
//
//    public String getVersionName() {
//        return versionName;
//    }
//    public void setVersionName(String versionName) {
//        this.versionName = versionName;
//    }
//    public String getVersionNumber() {
//        return versionNumber;
//   }
//    public void setVersionNumber(String versionNumber) {
//        this.versionNumber = versionNumber;
//    }
//    public int getImage() {return image;}
//    public void setImage(int image) {this.image = image;}
//}
public class AndroidFlavor implements Parcelable {
    String versionName;
    String versionNumber;
    int image; // drawable reference id
    String filmposter;


    public AndroidFlavor(String vName, String vNumber, int image, String filmposter)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
        this.filmposter = filmposter;
    }
    public AndroidFlavor()
    {

    }

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public String getVersionNumber() {
        return versionNumber;
    }
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
    public int getImage() {return image;}
    public void setImage(int image) {this.image = image;}
    public String getFilmposter() {return filmposter;}
    public void setFilmposter(String filmposter) {this.filmposter = filmposter;}

    protected AndroidFlavor(Parcel in) {
        versionName = in.readString();
        versionNumber = in.readString();
        image = in.readInt();
        filmposter = in.readString();
    }

    public static AndroidFlavor fromCursor(Cursor cursor) {
        //TODO return your MyListItem from cursor.
        return new AndroidFlavor();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(versionName);
        dest.writeString(versionNumber);
        dest.writeInt(image);
        dest.writeString(filmposter);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AndroidFlavor> CREATOR = new Parcelable.Creator<AndroidFlavor>() {
        @Override
        public AndroidFlavor createFromParcel(Parcel in) {
            return new AndroidFlavor(in);
        }

        @Override
        public AndroidFlavor[] newArray(int size) {
            return new AndroidFlavor[size];
        }
    };
}