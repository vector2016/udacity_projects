package demo.example.com.customarrayadapter.customviews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

// STARTS IN ANDROIDMANIFEST.XML
public class FlavorsProvider extends ContentProvider {
	private static final String LOG_TAG = FlavorsProvider.class.getSimpleName();
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private FlavorsDBHelper mOpenHelper;

	// Codes for the UriMatcher //////
	private static final int FLAVOR = 100;
	private static final int FLAVOR_WITH_ID = 200;
	private static final int FAVORITES = 300;
	private static final int FAVORITES_WITH_ID = 400;

	private static final String QUERY_STATEMENT = "INSERT OR IGNORE INTO favorites(movie_id," +
			"poster_path," +
			"adult," +
			"overview," +
			"release_date," +
			"original_title," +
			"original_language," +
			"title," +
			"backdrop_path," +
			"popularity," +
			"vote_count," +
			"video," +
			"vote_average) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

	private static UriMatcher buildUriMatcher(){
		// Build a UriMatcher by adding a specific code to return based on a match
		// It's common to use NO_MATCH as the code for this case.
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = FlavorsContract.CONTENT_AUTHORITY;

		// add a code for each type of URI you want

		// CONTENT_AUTHORITY = "demo.example.com.customarrayadapter.app";
		// TABLE_FLAVORS = "flavor";
		// FLAVOR = 100;
		// EG, demo.example.com.customarrayadapter.app/flavor
		matcher.addURI(authority, FlavorsContract.FlavorEntry.TABLE_FLAVORS, FLAVOR);

		// CONTENT_AUTHORITY = "demo.example.com.customarrayadapter.app";
		// TABLE_FLAVORS = "flavor/#";
		// FLAVOR_WITH_ID = 200;
		// EG, demo.example.com.customarrayadapter.app/flavor/#
		matcher.addURI(authority, FlavorsContract.FlavorEntry.TABLE_FLAVORS + "/#", FLAVOR_WITH_ID);

		matcher.addURI(authority, FlavorsContract.FavoritesEntry.TABLE_FAVORITES, FAVORITES);
		matcher.addURI(authority, FlavorsContract.FavoritesEntry.TABLE_FAVORITES + "/#", FAVORITES_WITH_ID);
		return matcher;	
	}

	@Override
	public boolean onCreate(){
		mOpenHelper = new FlavorsDBHelper(getContext());
		return true;
	}

	@Override
	public String getType(@NonNull Uri uri){
		final int match = sUriMatcher.match(uri);
		Log.d(LOG_TAG,"***uri: "+uri);
		switch (match){
			case FLAVOR:{
				Log.d(LOG_TAG,"getType - FLAVOR" + uri);
				return FlavorsContract.FlavorEntry.CONTENT_DIR_TYPE;
			}
			case FLAVOR_WITH_ID:{
				Log.d(LOG_TAG,"getType - FLAVOR_WITH_ID" + uri);
				return FlavorsContract.FlavorEntry.CONTENT_ITEM_TYPE;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
	}
	/*
	Note:
			These function are called from MainActivityFragment.

	 */

	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){ // uri: ref db,
																													// projection: columns - id,description,
																													// selection: specific column,
																													// selectionArgs: column id = 2
		Cursor retCursor;
		switch(sUriMatcher.match(uri)){
			// All Flavors selected
			case FLAVOR:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				retCursor.setNotificationUri(getContext().getContentResolver(),uri);
				Log.d(LOG_TAG,"query - FLAVOR" + uri);
				return retCursor;
				// TEST FAVORITES TABLE!!!!
			/*} case FAVORITES:{
				retCursor = mOpenHelper.getReadableDatabase().query(
						FlavorsContract.FavoritesEntry.TABLE_FAVORITES,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder);
				retCursor.setNotificationUri(getContext().getContentResolver(),uri);
				Log.d(LOG_TAG,"query - FLAVOR");
				return retCursor;
			*/
			}
			// Individual flavor based on Id selected
			case FLAVOR_WITH_ID:{ //
				retCursor = mOpenHelper.getReadableDatabase().query(
						FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						projection,
						FlavorsContract.FlavorEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))},
						null,
						null,
						sortOrder);
				retCursor.setNotificationUri(getContext().getContentResolver(),uri);
				Log.d(LOG_TAG,"query - FLAVOR_WITH_ID");
				return retCursor;
			}
			default:{
				// By default, we assume a bad URI
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
	}

	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri returnUri;
		switch (sUriMatcher.match(uri)) {
			case FLAVOR: {
				long _id = db.insert(FlavorsContract.FlavorEntry.TABLE_FLAVORS, null, values);
				// insert unless it is already contained in the database
				if (_id > 0) {
					returnUri = FlavorsContract.FlavorEntry.buildFlavorsUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				Log.d(LOG_TAG,"insert - FLAVOR" + uri);

				break;
			} case FAVORITES: {
				db.beginTransaction();
				SQLiteStatement stmt = null;
				try {
					stmt = db.compileStatement(QUERY_STATEMENT);
					stmt.bindLong(1,
							values.getAsInteger( FlavorsContract.FlavorEntry.COLUMN_MOVIE_ID));
					//String version = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME);
					//stmt.bindString(2,
					//		version != null ? values.getAsString( FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME) : "null");
					//String icon = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_ICON);
					//stmt.bindString(3,icon != null ? icon : "null");
					//String description = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION);
					//stmt.bindString(4,description != null ? description : "null");
					//String filmPoster = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_FILM_POSTER);
					//stmt.bindString(5,filmPoster != null ? filmPoster : "null");
					String posterPath = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_POSTER_PATH);
					stmt.bindString(2,posterPath != null ? posterPath : "null");
					stmt.bindLong(3,
							values.getAsInteger( FlavorsContract.FlavorEntry.COLUMN_ADULT));
					String overview = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_OVERVIEW);
					stmt.bindString(4,overview != null ? overview : "null");
					String releasedDate = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_RELEASE_DATE);
					stmt.bindString(5,releasedDate != null ? releasedDate : "null");
					String originalTitle = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_TITLE);
					stmt.bindString(6,originalTitle != null ? originalTitle : "null");
					String originalLanguage = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_ORIGINAL_LANGUAGE);
					stmt.bindString(7,originalLanguage != null ? originalLanguage : "null");
					String title = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_TITLE);
					stmt.bindString(8,title != null ? title : "null");
					String backdropPath = values.getAsString( FlavorsContract.FlavorEntry.COLUMN_BACKDROP_PATH);
					stmt.bindString(9,backdropPath != null ? backdropPath : "null");
					stmt.bindDouble(10,
							values.getAsFloat( FlavorsContract.FlavorEntry.COLUMN_POPULARITY));
					stmt.bindLong(11,
							values.getAsInteger( FlavorsContract.FlavorEntry.COLUMN_VOTE_COUNT));
					stmt.bindLong(12,
							values.getAsInteger( FlavorsContract.FlavorEntry.COLUMN_VIDEO));
					stmt.bindDouble(13,
							values.getAsFloat( FlavorsContract.FlavorEntry.COLUMN_VOTE_AVERAGE));
					stmt.execute();
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
					if (stmt != null) stmt.close();
				}
				// insert unless it is already contained in the database
				if (stmt != null) {
					// TEST FAVORITES TABLE (DISPLAY TO LOG). IF WORKS USE CURSORLOADER LATER!!!!!!!!!
					Cursor retCursor = mOpenHelper.getReadableDatabase().query(
							FlavorsContract.FavoritesEntry.TABLE_FAVORITES,
							null,
							null,
							null,
							null,
							null,
							null);
					if (retCursor != null ) {
						if  (retCursor.moveToFirst()) {
							do {
								String poster = retCursor.getString( retCursor.getColumnIndex(FlavorsContract.FavoritesEntry.COLUMN_TITLE));
								Log.d(LOG_TAG,"retCursor: " + poster);
							}while (retCursor.moveToNext());
						}
					}
					retCursor.close();

					/////////////////////////////////////////////
					returnUri = FlavorsContract.FavoritesEntry.buildFavoritesUri(0);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				Log.d(LOG_TAG,"insert - FAVORITES " + stmt);

				break;
			}
			default: {
				throw new UnsupportedOperationException("Unknown uri: " + uri);

			}
		}
		getContext().getContentResolver().notifyChange(uri, null);

		return returnUri;

	}

	@Override
	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int numDeleted;
		switch(match){
			case FLAVOR:
				numDeleted = db.delete(
						FlavorsContract.FlavorEntry.TABLE_FLAVORS, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");
				Log.d(LOG_TAG,"delete - FLAVOR");

				break;
			case FLAVOR_WITH_ID:
				numDeleted = db.delete(FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						FlavorsContract.FlavorEntry._ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + 
						FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");
				Log.d(LOG_TAG,"delete - FLAVOR_WITH_ID");
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		return numDeleted;
	}

	@Override
	public int bulkInsert(@NonNull Uri uri,@NonNull ContentValues[] values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch(match){
			case FLAVOR:
				// allows for multiple transactions
				db.beginTransaction();

				// keep track of successful inserts
				int numInserted = 0;
				try{
					for(ContentValues value : values){

						if (value == null){
							throw new IllegalArgumentException("Cannot have null content values");
						}
						long _id = -1;
						try{
							_id = db.insertOrThrow(FlavorsContract.FlavorEntry.TABLE_FLAVORS,
									null, value);
						}catch(SQLiteConstraintException e) {
							Log.w(LOG_TAG, "Attempting to insert " +
									value.getAsString(
											FlavorsContract.FlavorEntry.TABLE_FLAVORS)
									+ " but value is already in database.");
						}
						if (_id != -1){
							numInserted++;
						}
					}
					if(numInserted > 0){
						// If no errors, declare a successful transaction.
						// database will not populate if this is not called
						db.setTransactionSuccessful();
					}
				} finally {
					// all transactions occur at once
					db.endTransaction();
				}
				if (numInserted > 0){
					// if there was successful insertion, notify the content resolver that there
					// was a change
					getContext().getContentResolver().notifyChange(uri, null);
				}
				Log.d(LOG_TAG,"bulkInsert - FLAVOR");

				return numInserted;
			default:
				Log.d(LOG_TAG,"bulkInsert (default) - FLAVOR");
				return super.bulkInsert(uri, values);
		}
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int numUpdated;

		if (contentValues == null){
			throw new IllegalArgumentException("Cannot have null content values");
		}

		switch(sUriMatcher.match(uri)){
			case FLAVOR:{
				numUpdated = db.update(FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						contentValues,
						selection,
						selectionArgs);
				Log.d(LOG_TAG,"update - FLAVOR");

				break;
			}
			case FLAVOR_WITH_ID: {
				numUpdated = db.update(FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						contentValues,
						FlavorsContract.FlavorEntry._ID + " = ?",
						new String[] {String.valueOf(ContentUris.parseId(uri))});
				Log.d(LOG_TAG,"update - FLAVOR_WITH_ID");

				break;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}

		if (numUpdated > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return numUpdated;
	}

}
