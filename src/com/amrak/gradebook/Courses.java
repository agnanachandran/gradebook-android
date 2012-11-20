package com.amrak.gradebook;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Courses extends ListActivity {

	TextView tvTitle;
	String courses[] = {"Introduction to Methods of Software Engineering",
			"Programming Principles",
			"Linear Algebra for Engineering",
			"Calculus 1 for Engineering",
			"Physics for Electrical Engineering",
			"Linear Circuits"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(Courses.this, android.R.layout.simple_list_item_1, courses));
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String chooseCourse = courses[position];
		try {
			@SuppressWarnings("rawtypes")
			Class ourClass;
			ourClass = Class.forName("com.amrak.gradebook.Evaluations");
	        Intent ourIntent = new Intent(Courses.this, ourClass);
	        startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_courses, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.preferences:
			Intent i = new Intent("com.amrak.gradebook.SETTINGS");
			startActivity(i);
			break;
		case R.id.addcourse:
			Intent j = new Intent("com.amrak.gradebook.ADDCOURSE");
			startActivity(j);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}