package demo.example.com.customarrayadapter.customviews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FlavorsDBHelper extends SQLiteOpenHelper {
	public static final String LOG_TAG = FlavorsDBHelper.class.getSimpleName();

	//name & version
	private static final String DATABASE_NAME = "flavors.db";
	private static final int DATABASE_VERSION = 44;

	public FlavorsDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
		//public static final String RESULTS = "results"; // array of objects
		public static final String COLUMN_POSTER_PATH = "poster_path";       // string
		public static final String COLUMN_ADULT = "adult";           //boolean
		public static final String COLUMN_OVERVIEW = "overview";  // string
		public static final String COLUMN_RELEASE_DATE = "release_date"; // string; date

		//public static final String ID = "id";     //long
		public static final String COLUMN_ORIGINAL_TITLE = "original_title"; //String
		public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";   //string
		public static final String COLUMN_TITLE = "title"; //string
		public static final String COLUMN_BACKDROP_PATH = "backdrop_path";// string
		public static final String COLUMN_POPULARITY = "popularity";
		public static final String COLUMN_VOTE_COUNT = "vote_count"; //int
		public static final String COLUMN_VIDEO = "video"; // boolean
		public static final String COLUMN_VOTE_AVERAGE = "vote_average"; // float

	 */

	// Create the databas

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				FlavorsContract.FlavorEntry.TABLE_FLAVORS + "(" + FlavorsContract.FlavorEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_ICON +
				" INTEGER, " +
				FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_FILM_POSTER +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_POSTER_PATH +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_ADULT +
				" INTEGER, " +
				FlavorsContract.FlavorEntry.COLUMN_OVERVIEW +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_RELEASE_DATE +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_TITLE +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_LANGUAGE +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_TITLE +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_BACKDROP_PATH +
				" TEXT, " +
				FlavorsContract.FlavorEntry.COLUMN_POPULARITY +
				" FLOAT, " +
				FlavorsContract.FlavorEntry.COLUMN_VOTE_COUNT +
				" INTEGER, " +
				FlavorsContract.FlavorEntry.COLUMN_VIDEO +
				" INTEGER, " +
				FlavorsContract.FlavorEntry.COLUMN_VOTE_AVERAGE +
				" FLOAT" + ");";


		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

		final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
				FlavorsContract.TasteEntry.TABLE_TASTE + "(" + FlavorsContract.TasteEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.TasteEntry.COLUMN_GENRE_ID + " TEXT NOT NULL, " +
				FlavorsContract.TasteEntry.COLUMN_GENRE_NAME +
				" TEXT NOT NULL" +
				");";

		//sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);
	}


	/*
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				FlavorsContract.FlavorEntry.TABLE_FLAVORS + "(" + FlavorsContract.FlavorEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME +
				" TEXT NOT NULL, " +
				FlavorsContract.FlavorEntry.COLUMN_ICON +
				" INTEGER NOT NULL, " +
				FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION +
				" TEXT NOT NULL, " +
				FlavorsContract.FlavorEntry.COLUMN_FILM_POSTER +
				" TEXT NOT NULL " +


				");";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
	}
	*/

	// Upgrade database when version is changed.
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
				newVersion + ". OLD DATA WILL BE DESTROYED");
		// Drop the table
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FlavorsContract.FlavorEntry.TABLE_FLAVORS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");

		// re-create database
		onCreate(sqLiteDatabase);
	}
}
