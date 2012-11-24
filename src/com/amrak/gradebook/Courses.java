package com.amrak.gradebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Courses extends ListActivity {

	TextView tvTitle;
	ArrayList <String> courses2 = new ArrayList<String>();
	String courses[] = { "Introduction to Methods of Software Engineering",
			"Programming Principles", "Linear Algebra for Engineering",
			"Calculus 1 for Engineering", "Physics for Electrical Engineering",
	"Linear Circuits" };

	DBAdapter db = new DBAdapter(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(Courses.this,
				android.R.layout.simple_list_item_1, courses));
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		try {
			String destPath = "/data/data/" + getPackageName()
					+ "/databases/AssignmentDB";
			File f = new File(destPath);
			if (!f.exists()) {
				CopyDB(getBaseContext().getAssets().open(destPath),
						new FileOutputStream(destPath));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		switch (item.getItemId()) {
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

	public void CopyDB(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
	}

	public void DisplayRecord(Cursor c) {
		Toast.makeText(
				this,
				"id: " + c.getString(0) + "\n" + "Title: " + c.getString(1)
				+ "\n" + "Due Date: " + c.getString(2),
				Toast.LENGTH_SHORT).show();
	}

}