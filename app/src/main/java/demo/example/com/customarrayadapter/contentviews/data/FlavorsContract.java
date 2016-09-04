package demo.example.com.customarrayadapter.contentviews.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FlavorsContract{

	public static final String CONTENT_AUTHORITY = "demo.example.com.customarrayadapter.app";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


	public static final class FlavorEntry implements BaseColumns {
		// table name
		public static final String TABLE_FLAVORS = "flavor";
		// columns
		public static final String _ID = "_id";
		public static final String COLUMN_MOVIE_ID = "movie_id"; // long
		//public static final String COLUMN_ICON = "icon";
		//public static final String COLUMN_DESCRIPTION = "description";
		//public static final String COLUMN_VERSION_NAME = "version_name";
		//public static final String COLUMN_FILM_POSTER = "film_poster";

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

		// create content uri
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
			.appendPath(TABLE_FLAVORS).build();
		// create cursor of base type directory for multiple entries
		public static final String CONTENT_DIR_TYPE =
		ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FLAVORS;
		// create cursor of base type item for single entry
		public static final String CONTENT_ITEM_TYPE =
			ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FLAVORS;

		// for building URIs on insertion
		public static Uri buildFlavorsUri(long id){
        		return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	public static final class FavoritesEntry implements BaseColumns {
		// table name
		public static final String TABLE_FAVORITES = "favorites";
		// columns
		public static final String _ID = "_id";
		public static final String COLUMN_MOVIE_ID = "movie_id"; // long
		//public static final String COLUMN_ICON = "icon";
		//public static final String COLUMN_DESCRIPTION = "description";
		//public static final String COLUMN_VERSION_NAME = "version_name";
		//public static final String COLUMN_FILM_POSTER = "film_poster";

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

		// create content uri
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(TABLE_FAVORITES).build();
		// create cursor of base type directory for multiple entries
		public static final String CONTENT_DIR_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;
		// create cursor of base type item for single entry
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;

		// for building URIs on insertion
		public static Uri buildFavoritesUri(long id){
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	public static final class GenreEntry implements BaseColumns {
		public static final String TABLE_GENRE = "genre";

		public static final String _ID = "_id";
		public static final String COLUMN_GENRE_ID = "genre_id";
		public static final String COLUMN_GENRE_NAME = "genre_name";

		// create content uri
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(TABLE_GENRE).build();
		// create cursor of base type directory for multiple entries
		public static final String CONTENT_DIR_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_GENRE;
		// create cursor of base type item for single entry
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_GENRE;

		// for building URIs on insertion
		public static Uri buildGenreUri(long id){
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
