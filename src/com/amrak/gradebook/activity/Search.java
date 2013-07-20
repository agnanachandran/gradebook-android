package com.amrak.gradebook.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.amrak.gradebook.CustomSuggestionProvider;
import com.amrak.gradebook.R;
import com.amrak.gradebook.adapter.SearchCursorAdapter;
import com.amrak.gradebook.db.adapter.EvaluationsDBAdapter;

public class Search extends Activity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
	
	private static final String TAG = "Search";
	
	private SearchCursorAdapter searchCursorAdapter;
	private Cursor cursor;
	private SearchView searchView;
	private ListView evalsListView;
	
	String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 };
	int[] to = new int[] { R.id.activity_search_listitem_text };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
        // up button in action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        searchCursorAdapter = new SearchCursorAdapter(this, R.layout.activity_search_listitem, null, from, to, 0);
        evalsListView = (ListView) findViewById(android.R.id.list);
        evalsListView.setAdapter(searchCursorAdapter);

	}
	
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
    	Log.i(TAG, "Search intent is received");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          System.out.println("Query is " + query);
        }
    }
    
    private void updateDisplay() {
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search, menu);
		
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        
        getLoaderManager().initLoader(0, null, this);
        
        if (searchCursorAdapter == null) Log.i(TAG, "searchCursorAdapter is null");
        else Log.i(TAG, "searchCursorAdapter is not null");
        searchView.setSuggestionsAdapter(searchCursorAdapter);
        searchView.setOnQueryTextListener(this);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            	finish(); //Simply pop Search activity off call stack
        }
        return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "onCreateLoader()");
		Uri uri = Uri.parse("content://" + CustomSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/" + searchView.getQuery());
		String[] projection = new String[] {EvaluationsDBAdapter.EVAL_TITLE};
		String selection = EvaluationsDBAdapter.EVAL_TITLE + " LIKE \'?\'";
		String[] selectionArgs = new String[] {"%" + searchView.getQuery() + "%"};
		
		return new CursorLoader(this, uri, projection, selection, selectionArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Log.i(TAG, "onLoadFinish()");
		searchCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Log.i(TAG, "onLoadReset()");
		searchCursorAdapter.swapCursor(null);		
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		getLoaderManager().restartLoader(0, null, this);
		//System.out.println("Cursor count is " + searchCursorAdapter.getCursor().getColumnCount());
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

}
