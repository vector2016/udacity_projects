package demo.example.com.customarrayadapter.contentviews;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import demo.example.com.customarrayadapter.model.Movie;

/**
 * This Fragment manages a single background task and retains 
 * itself across configuration changes.
 */
@SuppressWarnings("ALL")
public class TaskFragment extends Fragment {
  private static final String LOG_TAG = TaskFragment.class.getSimpleName();
  private static final boolean DEBUG = true; // Set this to false to disable logs.
  private static final String HIGHEST_RATED_MOVIES = "vote_average.desc";
  private static final String MOST_POPULAR_MOVIES = "popularity.desc";


  /**
   * Callback interface through which the fragment will report the
   * task's progress and results back to the Activity.
   */
  interface TaskCallbacks {
    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute(ArrayList<Movie> movies, Cursor c);
  }

  private TaskCallbacks mCallbacks;
  private FetchMoviesTask mTask;
  private boolean mRunning;
  public String mSortType = MOST_POPULAR_MOVIES;



  private Uri.Builder builder;

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
    if (!(getTargetFragment() instanceof TaskCallbacks)) {
      throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
    }

   /**
    * Hold a reference to the target fragment so we can report back the task's
    * current progress and results.
    */
    mCallbacks = (TaskCallbacks) getTargetFragment();
    Log.d(LOG_TAG,"CALLBACK**"+mCallbacks);
    if (DEBUG) Log.d(LOG_TAG,"mCallbacks initialised!!: "+mCallbacks);
  }

  /**
   * This method will only be called once when the retained
   * Fragment is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    if (DEBUG) Log.i(LOG_TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);

    // Retain this fragment across configuration changes.
    setRetainInstance(true);
  }

  /**
   * Note that this method is <em>not</em> called when the Fragment is being
   * retained across Activity instances. It will, however, be called when its
   * parent Activity is being destroyed for good (such as when the user clicks
   * the back button, etc.).
   */
  @Override
  public void onDestroy() {
    if (DEBUG) Log.i(LOG_TAG, "onDestroy()");
    super.onDestroy();
    cancel();
  }


  /*****************************/
  /***** TASK FRAGMENT API *****/
  /*****************************/

  /**
   * Start the background task from MainActivityFragment.
   */
  public void start() {
    if (!mRunning) {
      mTask = new FetchMoviesTask();
      mTask.mSortType = mSortType;
    }
    mTask.execute();
    mRunning = true;
  }

  /**
   * Cancel the background task.
   */
  public void cancel() {
    if (mRunning) {
      mTask.cancel(false);
      mTask = null;
      mRunning = false;
    }
  }

  /**
   * Returns the current state of the background task.
   */
  public boolean isRunning() {
    return mRunning;
  }


  /************************/
  /***** LOGS & STUFF *****/
  /************************/

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    if (DEBUG) Log.i(LOG_TAG, "onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    if (DEBUG) Log.i(LOG_TAG, "onStart()");
    super.onStart();
  }

  @Override
  public void onResume() {
    if (DEBUG) Log.i(LOG_TAG, "onResume()");
    super.onResume();
  }

  @Override
  public void onPause() {
    if (DEBUG) Log.i(LOG_TAG, "onPause()");
    super.onPause();
  }

  @Override
  public void onStop() {
    if (DEBUG) Log.i(LOG_TAG, "onStop()");
    super.onStop();
  }

  @Override
  public void onDetach() {
    if (DEBUG) Log.i(LOG_TAG, "onDetach()");
    super.onDetach();
  }


  /***************************/
  /***** BACKGROUND TASK *****/
  /***************************/

  /**
   * A movies task that performs some background work and proxies progress
   * updates and results back to the Activity.
   */

  private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String API_KEY = "60dc21f18cb4ecb2cf95340022bc1bc1";
    private static final String LOCATION = "US";
    private ArrayList<Movie> mMovie;
    //private String mSortType = MOST_POPULAR_MOVIES;
    private String mSortType = HIGHEST_RATED_MOVIES;

    //private ArrayList<Integer> mGenre;
    //private MovieListener movieListener;

    //public FetchMoviesTask (MovieListener movieListener) {
    //  this.movieListener = movieListener;
    //}
    @Override
    protected void onPreExecute() {
      // Proxy the call to the Activity.

      //mCallbacks.onPreExecute();
      mRunning = true;
    }

    protected ArrayList<Movie> doInBackground(Void ...Void) {
      Log.v(LOG_TAG, "doInBackground()");

      // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
      HttpURLConnection urlConnection = null;
      BufferedReader reader = null;

// Will contain the raw JSON response as a string.
      String forecastJsonStr = null;


      try {
        switch (mSortType) {
          case MOST_POPULAR_MOVIES:
            builder = null;
            builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", MOST_POPULAR_MOVIES)
                    .appendQueryParameter("api_key", API_KEY);
            Log.d(LOG_TAG, "mSortType:" + MOST_POPULAR_MOVIES);
            break;

          case HIGHEST_RATED_MOVIES:
            builder = null;
            builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("top_rated")
                    .appendQueryParameter("api_key", API_KEY);
            Log.d(LOG_TAG, "mSortType:" + HIGHEST_RATED_MOVIES);
            break;

        }

        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast

        //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

        //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=60dc21f18cb4ecb2cf95340022bc1bc1
        URL url = new URL(builder.toString());
        Log.d(LOG_TAG,"TRL:"+url);

        // Create the request to OpenWeatherMap, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
          // Nothing to do.
          forecastJsonStr = null;

        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
          // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
          // But it does make debugging a *lot* easier if you print out the completed
          // buffer for debugging.
          buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
          // Stream was empty.  No point in parsing.
          return null;
        }
        forecastJsonStr = buffer.toString();
        mMovie = getMovieDataFromJson(forecastJsonStr);

        Log.v (LOG_TAG, "Forecast JSON String: " + forecastJsonStr );
      } catch (IOException e) {
        Log.e(LOG_TAG, "Error ", e);
        // If the code didn't successfully get the weather data, there's no point in attempting
        // to parse it.
        return null;
      } catch (JSONException e) {
        e.printStackTrace();
      } finally {
        if (urlConnection != null) {
          urlConnection.disconnect();
        }
        if (reader != null) {
          try {
            reader.close();
          } catch (final IOException e) {
            Log.e(LOG_TAG, "Error closing stream", e);
          }
        }
      }
      return mMovie;
    }//doInBackground

    @Override
    protected void onCancelled() {
      // Proxy the call to the Activity.
      mCallbacks.onCancelled();
      mRunning = false;
    }

    @Override
    protected void onProgressUpdate(Void... Void) {
    }//onProgressUpdate

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
      //Log.v(LOG_TAG,"moveListener: "+movieListener);
      //if ( ((MainActivityFragment) getTargetFragment()) != null)
      //  ((MainActivityFragment) getTargetFragment()).getMovies(movies);
      Cursor c = null;
      mCallbacks.onPostExecute(movies, c);
      mRunning = false;
    }//onPostExecute



    //public void setOnMovieListener(MovieListener movieListener) {
    //  this.movieListener = movieListener;
    //}

    private ArrayList<Movie> getMovieDataFromJson(String forecastJsonStr)
            throws JSONException {

      // Now we have a String representing the complete forecast in JSON Format.
      // Fortunately parsing is easy:  constructor takes the JSON string and converts it
      // into an Object hierarchy for us.

      // These are the names of the JSON objects that need to be extracted.


      final String RESULTS = "results"; // array of objects
      final String POSTER_PATH = "poster_path";       // string
      final String ADULT = "adult";           //boolean
      final String OVERVIEW = "overview";  // string
      final String RELEASE_DATE = "release_date"; // string; date
      final String GENRE_IDS = "genre_ids"; // list of ints
      final String ID = "id";     //long
      final String ORIGINAL_TITLE = "original_title"; //String
      final String ORIGINAL_LANGUAGE = "original_language";   //string
      final String TITLE = "title"; //string
      final String BACKDROP_PATH = "backdrop_path";// string
      final String POPULARITY = "popularity";
      final String VOTE_COUNT = "vote_count"; //int
      final String VIDEO = "video"; // boolean
      final String VOTE_AVERAGE = "vote_average"; // float

      ArrayList<Movie> mMovie = new ArrayList<Movie>();
      ArrayList<Integer> mGenre = new ArrayList<Integer>();

      JSONObject movieDataJson = new JSONObject(forecastJsonStr);
      JSONArray movieArray = movieDataJson.getJSONArray(RESULTS);

      for (int i = 0;i < movieArray.length();i++){
        Movie movie = new Movie();
        JSONObject movieJson = movieArray.getJSONObject(i);
        String movieTitle = movieJson.getString(ORIGINAL_TITLE);
        movie.setOriginalTitle(movieTitle);

        String pPath = movieJson.getString(POSTER_PATH);
        movie.setPosterPath(pPath);

        Boolean adult = movieJson.getBoolean(ADULT);
        movie.setAdult(adult);

        String overview = movieJson.getString(OVERVIEW);
        movie.setOverview(overview);

        String releaseDate = movieJson.getString(RELEASE_DATE);
        movie.setReleaseDate(releaseDate);

        JSONArray genreIds = movieJson.getJSONArray(GENRE_IDS);

        for (int j=0;j<genreIds.length();j++){
          int genre = genreIds.getInt(j);
          mGenre.add(genre);
        }
        movie.setGenreIds(mGenre);
        mGenre.clear();

        Long id = movieJson.getLong(ID);
        movie.setId(id);

        String originaltitle = movieJson.getString(ORIGINAL_TITLE);
        movie.setTitle(originaltitle);

        String language = movieJson.getString(ORIGINAL_LANGUAGE);
        movie.setTitle(language);

        String title = movieJson.getString(TITLE);
        movie.setTitle(title);

        String backdropPath = movieJson.getString(BACKDROP_PATH);
        movie.setBackdropPath(backdropPath);

        Double popularity = movieJson.getDouble(POPULARITY);
        movie.setPopularity(popularity);

        Integer voteCount = movieJson.getInt(VOTE_COUNT);
        movie.setVoteCount(voteCount);

        Boolean video = movieJson.getBoolean(VIDEO);
        movie.setVideo(video);

        Double voteAverage = movieJson.getDouble(VOTE_AVERAGE);
        movie.setVoteAverage(voteAverage);

        mMovie.add(movie);

        //Log.v(LOG_TAG,"Movie title: "+movieTitle);
      }

      //long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

      // Insert the new weather information into the database
      //Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());

      // OWM returns daily forecasts based upon the local time of the city that is being
      // asked for, which means that we need to know the GMT offset to translate this data
      // properly.

      // Since this data is also sent in-order and the first day is always the
      // current day, we're going to take advantage of that to get a nice
      // normalized UTC date for all of our weather.

      Time dayTime = new Time();
      dayTime.setToNow();

      // we start at the day returned by local time. Otherwise this is a mess.
      int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

      // now we work exclusively in UTC
      dayTime = new Time();


            /*
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(WeatherContract.WeatherEntry.CONTENT_URI,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " <= ?",
                        new String[] {Long.toString(dayTime.setJulianDay(julianStartDay-1))});

                // Call notifyWeather(); within the onPerformSync function.
                // You should call it when weather data is inserted into the database
                // (right after bulkInsert is called).
                notifyWeather();

            }
            */
      //Log.d(LOG_TAG, "Sunshine Service Complete. " + cVVector.size() + " Inserted");

      return mMovie;
    }
  }//FetchMovies
}
