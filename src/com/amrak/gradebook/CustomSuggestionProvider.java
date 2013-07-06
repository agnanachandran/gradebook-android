package com.amrak.gradebook;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class CustomSuggestionProvider extends ContentProvider {
	
	public static String AUTHORITY = "com.amrak.gradebook.CustomSuggestionProvider";
	
	EvaluationsDBAdapter evalsDBAdapter;
	
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
		evalsDBAdapter = new EvaluationsDBAdapter(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		switch(uriMatcher.match(uri)) {
		case SEARCH_SUGGEST:
			return getSuggestions(uri.getLastPathSegment());
		default:
			return null;
		}
	}
	
	private Cursor getSuggestions(String query) {
		return evalsDBAdapter.getEvaluationSortByName(query);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
