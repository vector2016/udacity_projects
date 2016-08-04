package demo.example.com.customarrayadapter.customviews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FlavorsDBHelper extends SQLiteOpenHelper {
	public static final String LOG_TAG = FlavorsDBHelper.class.getSimpleName();

	//name & version
	private static final String DATABASE_NAME = "flavors.db";
	private static final int DATABASE_VERSION = 48;

	public FlavorsDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create the databas
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
				FlavorsContract.FlavorEntry.TABLE_FLAVORS + "(" + FlavorsContract.FlavorEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.FlavorEntry.COLUMN_MOVIE_ID +
				" INTEGER UNIQUE ON CONFLICT REPLACE, " +
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

		final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
				FlavorsContract.FavoritesEntry.TABLE_FAVORITES + "(" + FlavorsContract.FavoritesEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.FavoritesEntry.COLUMN_MOVIE_ID +
				" INTEGER UNIQUE ON CONFLICT REPLACE, " +
				FlavorsContract.FavoritesEntry.COLUMN_POSTER_PATH +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_ADULT +
				" INTEGER, " +
				FlavorsContract.FavoritesEntry.COLUMN_OVERVIEW +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_RELEASE_DATE +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_ORIGINAL_LANGUAGE +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_TITLE +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_BACKDROP_PATH +
				" TEXT, " +
				FlavorsContract.FavoritesEntry.COLUMN_POPULARITY +
				" FLOAT, " +
				FlavorsContract.FavoritesEntry.COLUMN_VOTE_COUNT +
				" INTEGER, " +
				FlavorsContract.FavoritesEntry.COLUMN_VIDEO +
				" INTEGER, " +
				FlavorsContract.FavoritesEntry.COLUMN_VOTE_AVERAGE +
				" FLOAT" + ");";


		sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);


		final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
				FlavorsContract.TasteEntry.TABLE_TASTE + "(" + FlavorsContract.TasteEntry._ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FlavorsContract.TasteEntry.COLUMN_GENRE_ID + " TEXT NOT NULL, " +
				FlavorsContract.TasteEntry.COLUMN_GENRE_NAME +
				" TEXT NOT NULL" +
				");";

		//sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);
	}
	// Upgrade database when version is changed.
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
				newVersion + ". OLD DATA WILL BE DESTROYED");
		// Drop the table
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FlavorsContract.FlavorEntry.TABLE_FLAVORS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");

		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FlavorsContract.FavoritesEntry.TABLE_FAVORITES);
		sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
				FlavorsContract.FavoritesEntry.TABLE_FAVORITES + "'");

		// re-create database
		onCreate(sqLiteDatabase);
	}
}
