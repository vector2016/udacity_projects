package demo.example.com.customarrayadapter.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.customviews.data.FlavorsContract;
import demo.example.com.customarrayadapter.model.AndroidFlavor;
import demo.example.com.customarrayadapter.model.Movie;


/**
 * Created by richard on 03/01/16.
 */
public class AndroidFlavorCursorRecyclerViewAdapter extends
        CursorRecyclerAdapter<AndroidFlavorCursorRecyclerViewAdapter
                .DataObjectHolder> {
    private static String LOG_TAG = AndroidFlavorCursorRecyclerViewAdapter.class.getSimpleName();
    private List<AndroidFlavor> mDataset;
    private static MyClickListener myClickListener;
    private Cursor mCursor;
    private int posterHeight = 100;
    private int posterWidth = 100;



    public static class DataObjectHolder extends RecyclerView.ViewHolder
            //implements View
            //.OnClickListener
    {
        String Url;
        TextView label01,
                label02,
                label03;
        ImageView image;
        Info displayInfo;
        Movie info;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label01 = (TextView) itemView.findViewById(R.id.text_image01);
            label02 = (TextView) itemView.findViewById(R.id.text_image02);
            image = (ImageView) itemView.findViewById(R.id.flavor_image);
            info = new Movie();
        }
    }

    public static class Info {
        public String poster;
        public String posterBackdrop;
        public String description;

        public String posterPath;       // string
        public Boolean adult;           //boolean
        public String overview;  // string
        public String releaseDate; // string; date
        public ArrayList<Integer> genreIds; // list of ints
        public Long id;     //long
        public String originalTitle; //String
        public String originalLanguage;   //string
        public String title; //string
        public String backdropPath;// string
        public Double popularity; //float
        public Integer voteCount; //int
        public Boolean video; // boolean
        public Double voteAverage; // float
    }


    public int getPosterWidth(){
        return posterWidth;
    }
    public  int getPosterHeight(){
        return posterHeight;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public AndroidFlavorCursorRecyclerViewAdapter(Cursor cursor) {
        super(cursor);
        //mCursor = cursor;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flavor_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    @Override
    public void onBindViewHolderCursor(final DataObjectHolder holder, Cursor cursor) {
        Context context = holder.image.getContext();

        /*
         * Note: There is a massive drop in performance possibly due to attempts
         * to retrieve resources offline. Access to contentprovider should be done
         * earlier
         */
        holder.label01.setText("Movie Title");
        holder.label02.setText("more info....");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use holder.getPosition() to get item from array
                // Note: pass cursor to my callback or use cursor content and fill
                // info then pass info to callback
                myClickListener.onItemClick(holder.getPosition(), v);
            }
        });

            String poster = cursor.getString(
                    cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_POSTER_PATH));
        String movieName = cursor.getString(
                cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_TITLE));
            holder.info.setPosterPath(poster);
            holder.info.setOriginalLanguage(movieName);
            String description = cursor.getString(
                    cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_OVERVIEW));


            Glide.with(context.getApplicationContext()).load(poster)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(185, 328)
                    .into(holder.image);

        Log.v(LOG_TAG, "onBindViewHolderCursor() " + cursor.getCount());
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

}
