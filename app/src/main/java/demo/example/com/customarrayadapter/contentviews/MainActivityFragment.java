package demo.example.com.customarrayadapter.contentviews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;

import demo.example.com.customarrayadapter.MovieActivity;
import demo.example.com.customarrayadapter.R;
import demo.example.com.customarrayadapter.adapter.AndroidFlavorCursorRecyclerViewAdapter;
import demo.example.com.customarrayadapter.customviews.data.FlavorsContract;
import demo.example.com.customarrayadapter.interfaces.ImageLoadedCallback.OnImageLoadedListener;
import demo.example.com.customarrayadapter.model.AndroidFlavor;
import demo.example.com.customarrayadapter.model.Movie;
import demo.example.com.customarrayadapter.model.PassReference;


/**
 * A fragment containing the list view of Android versions.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        TaskFragment. TaskCallbacks{


    private static String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.
    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private static final String TAG_TAB_FRAGMENT1 = "tab_fragment1";

    private static final String HIGHEST_RATED_MOVIES = "vote_average.desc";
    private static final String MOST_POPULAR_MOVIES = "popularity.desc";
    private static final int CURSOR_LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private AndroidFlavorCursorRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AndroidFlavor> flavorList;
    private TaskFragment mTaskFragment;
    private TabFragment1 mTabFragment1;
    private Cursor cur;
    ArrayList<Movie> moviesInfo;
    TabFragment1 context;
    private String mBitmap;
    private int mIndex;
    private OnImageLoadedListener mImageLoadedCallback;
    private onPassReferenceListener mOnPassReferenceCallback;



    private AndroidFlavor[] androidFlavors;
    SharedPreferences mPrefs;
    static onPassReferenceListener myListener;

    public interface OnImageLoadedCallbacks {
        void onImageLoaded(String bitmap);
    }

    public interface onPassReferenceListener {
        void onPassReference(MainActivityFragment fragment);
    }


    public MainActivityFragment() {
    }



    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */

    @Override
    public void onAttach(Activity activity) {
        if (DEBUG) Log.i(LOG_TAG, "onAttach(Activity)");
        super.onAttach(activity);

        /**
         * Hold a reference to the target fragment so we can report back the task's
         * current progress and results.
         */

        mOnPassReferenceCallback = (onPassReferenceListener) getTargetFragment();
        if (DEBUG) Log.d(LOG_TAG,"mOnPassReferenceCallback initialised!!" + mOnPassReferenceCallback);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelableArrayList("flavors", flavorList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);// Added
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AndroidFlavorCursorRecyclerViewAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.i(LOG_TAG, "onActivityCreated(Bundle)");

        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If we haven't retained the worker fragment, then create it
        // and set this MainActivityFragment as the TaskFragment's target fragment.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            mTaskFragment.setTargetFragment(this, 0);
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (mTaskFragment.isRunning()) {
            if (DEBUG) Log.i (LOG_TAG, "cancel");
        } else {
            if (DEBUG) Log.i (LOG_TAG, "start");
        }
    }

    // insert data into database
    public void insertData() {
        ContentValues[] flavorValuesArr = new ContentValues[androidFlavors.length];
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues
        for (int i = 0; i < androidFlavors.length; i++) {
            flavorValuesArr[i] = new ContentValues();
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_ICON, androidFlavors[i].getImage());
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME,
                    androidFlavors[i].getVersionName());
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION,
                    androidFlavors[i].getVersionNumber());
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_FILM_POSTER,
                    androidFlavors[i].getFilmposter());


        }
        getActivity().getContentResolver().bulkInsert(FlavorsContract.FlavorEntry.CONTENT_URI,
                flavorValuesArr);
    }

    // insert data into database
    public void insertData(ArrayList<Movie> movies) {
        ContentValues[] tasteValuesArr = new ContentValues[movies.size()];
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues
        for (int i = 0; i < movies.size(); i++) {
            tasteValuesArr[i] = new ContentValues();
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_MOVIE_ID,
                    movies.get(i).getId());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_POSTER_PATH,
                    "http://image.tmdb.org/t/p/w342/" + movies.get(i).getPosterPath());
            int isAdult = movies.get(i).isAdult() ? 1 : 0;
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_ADULT,
                    isAdult);
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_OVERVIEW,
                    movies.get(i).getOverview());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_RELEASE_DATE,
                    movies.get(i).getReleaseDate());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_TITLE,
                    movies.get(i).getOriginalTitle());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_LANGUAGE,
                    movies.get(i).getOriginalLanguage());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_TITLE,
                    movies.get(i).getTitle());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_BACKDROP_PATH,
                    "http://image.tmdb.org/t/p/w342/" + movies.get(i).getBackdropPath());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_POPULARITY,
                    movies.get(i).getPopularity());
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_VOTE_COUNT,
                    movies.get(i).getVoteCount());
            int isVideo = movies.get(i).isVideo() ? 1 : 0;
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_VIDEO,
                    isVideo);
            tasteValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_VOTE_AVERAGE,
                    movies.get(i).getVoteAverage());

        }
        // bulkInsert our ContentValues array
        getActivity().getContentResolver().bulkInsert(FlavorsContract.FlavorEntry.CONTENT_URI,
                tasteValuesArr);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Creating a shared preference
        //SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);
        if (DEBUG) Log.d(LOG_TAG, "Task status: " + mTaskFragment.isRunning());

        if ((mTaskFragment != null) && (!mTaskFragment.isRunning())) {
            mTaskFragment.start();
        }
        mAdapter.setOnItemClickListener(
                new AndroidFlavorCursorRecyclerViewAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Context context = v.getContext();
                        if (moviesInfo != null) {
                            mIndex = position;
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("movie_Array_List_Extra", moviesInfo);
                            bundle.putInt("position_extra", position);
                            Intent intent = new Intent(context, MovieActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                FlavorsContract.FlavorEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    // reset CursorAdapter on Loader Reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /**
     * The four proxy methods below are called by the TaskFragment when new
     * progress updates or results are available. The MainActivity
     * should respond by updating its UI to indicate the change.
     **/

    @Override
    public void onPreExecute() {
        if (DEBUG) Log.i(LOG_TAG, "onPreExecute()");
        Toast.makeText(getActivity(), R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int percent) {
        if (DEBUG) Log.i(LOG_TAG, "onProgressUpdate()");
    }

    @Override
    public void onCancelled() {
        if (DEBUG) Log.i(LOG_TAG, "onCancelled()");
        Toast.makeText(getActivity(), R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPostExecute(ArrayList<Movie> movies, Cursor c) {
        /**
         * NOTE: this piece of code will be updated to incorporate a syncadapter
         * or job service of some kind later on
         *
         */
        moviesInfo = movies;

        if (DEBUG) Log.i(LOG_TAG, "onPostExecute()");
        if (movies != null) {
            cur = getActivity().getContentResolver().query(FlavorsContract.FlavorEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null,
                    null);

            Log.d(LOG_TAG, "How many records in movies ContentProvider? " + cur.getCount());
            if (cur.getCount() == 0) {
                //insertData();
                insertData(movies);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
            } else {
                getActivity().getContentResolver().delete(FlavorsContract.FlavorEntry.CONTENT_URI,
                        null,
                        null);

                insertData(movies);
                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
            }

        }
        Toast.makeText(getActivity(), R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }

    /************************/
    /***** OPTIONS MENU *****/
    /************************/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();

            case R.id.most_popular:
                if (DEBUG) Log.i (LOG_TAG,"****most_popular - moviesInfo :"+moviesInfo);
                if (mTaskFragment != null)
                    if (!mTaskFragment.isRunning()) {
                        mTaskFragment.mSortType = MOST_POPULAR_MOVIES;
                        mTaskFragment.start();
                    } else {
                        mTaskFragment.cancel();
                        mTaskFragment.mSortType = MOST_POPULAR_MOVIES;
                        mTaskFragment.start();
                    }
                return true;

            case R.id.highest_rated:
                if (DEBUG) Log.i (LOG_TAG,"***highest_rated - moviesInfo :"+moviesInfo);
                if (mTaskFragment != null)
                    if (!mTaskFragment.isRunning()) {
                        mTaskFragment.mSortType = HIGHEST_RATED_MOVIES;
                        mTaskFragment.start();
                    } else {
                        mTaskFragment.cancel();
                        mTaskFragment.mSortType = HIGHEST_RATED_MOVIES;
                        mTaskFragment.start();
                    }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
