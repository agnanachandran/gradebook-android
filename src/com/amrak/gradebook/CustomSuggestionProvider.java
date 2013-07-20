package com.amrak.gradebook;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.amrak.gradebook.db.adapter.CategoriesDBAdapter;
import com.amrak.gradebook.db.adapter.CoursesDBAdapter;
import com.amrak.gradebook.db.adapter.EvaluationsDBAdapter;
import com.amrak.gradebook.db.adapter.TaskDBAdapter;
import com.amrak.gradebook.db.adapter.TermsDBAdapter;

public class CustomSuggestionProvider extends ContentProvider {
	
	private static final String TAG = "CustomSuggestionProvider";
	public static String AUTHORITY = "com.amrak.gradebook.CustomSuggestionProvider";
	
	TermsDBAdapter termsDBAdapter;
	CoursesDBAdapter coursesDBAdapter;
	CategoriesDBAdapter catsDBAdapter;
	EvaluationsDBAdapter evalsDBAdapter;
	TaskDBAdapter taskDBAdapter;
	
	private static final int SEARCH_SUGGEST = 0;
	private static final UriMatcher uriMatcher = buildUriMatcher();
	
	public CustomSuggestionProvider() {
	}
	
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		//to get suggestions...
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
		return matcher;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean onCreate() {
		termsDBAdapter = new TermsDBAdapter(getContext());
		coursesDBAdapter = new CoursesDBAdapter(getContext());
		catsDBAdapter = new CategoriesDBAdapter(getContext());
		evalsDBAdapter = new EvaluationsDBAdapter(getContext());
		taskDBAdapter = new TaskDBAdapter(getContext());
		
		termsDBAdapter.open();
		coursesDBAdapter.open();
		catsDBAdapter.open();
		evalsDBAdapter.open();
		taskDBAdapter.open();
		
		//Note, in Content Provider, DB Adapters do not need to be closed, please refer to
		//http://stackoverflow.com/questions/4547461/closing-the-database-in-a-contentprovider
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		Log.i(TAG, "Content provider received query. Uri is " + uri.toString());

		switch(uriMatcher.match(uri)) {
		case SEARCH_SUGGEST:
			Log.i(TAG, "URI Matcher matched SEARCH_SUGGEST");
			return getSuggestions(uri.getLastPathSegment());
		default:
			Log.i(TAG, "URI Matcher did not match anything");
			return null;
		}
	}
	
	private Cursor getSuggestions(String query) {
		Cursor c = evalsDBAdapter.getEvaluationSortByName(query);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
