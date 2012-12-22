package com.amrak.gradebook;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Terms extends Activity {

	// database
	TermsDBAdapter termsDB = new TermsDBAdapter(this);
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
	CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
	EvaluationsDBAdapter evalsDB = new EvaluationsDBAdapter(this);

	// context
	Context context = this;

	// view(s)
	ListView listView;
	TextView title;
	TextView mark;
	TextView date;

	// listAdapters
	private TermsListAdapter termslistAdapter;

	// data
	public static final int CONTEXT_EDIT = 0;
	public static final int CONTEXT_DELETE = 1;
	// terms
	ArrayList<TermData> terms = new ArrayList<TermData>();
	
	// variables
	final private String TAG = "Terms";
	int[] refIDPass_Term;
	int contextSelection;

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms);

		// initialization of views
		title = (TextView) findViewById(R.id.tvTermTitle);
		mark = (TextView) findViewById(R.id.tvTermMark);
		date = (TextView) findViewById(R.id.tvTermDate);
		listView = (ListView) findViewById(R.id.lvTerms);

		// change title back to Terms since default title is the app's name
		getActionBar().setTitle("Terms");
		// read data from database
		dataReadToList();

		// input listview data
		termslistAdapter = new TermsListAdapter(this, terms);
		listView.setAdapter(termslistAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				try {
					@SuppressWarnings("rawtypes")
					Class cCourse;
					cCourse = Class.forName("com.amrak.gradebook.Courses");
					Intent iCourse = new Intent(Terms.this, cCourse);
					iCourse.putExtra("refID_Term", refIDPass_Term[position]);
					startActivity(iCourse);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
		registerForContextMenu(listView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		dataReset();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_term, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:
			Intent iSettings = new Intent("com.amrak.gradebook.SETTINGS");
			startActivity(iSettings);
			break;
		case R.id.addterm:
			Intent iAddTerm = new Intent("com.amrak.gradebook.ADDTERM");
			startActivity(iAddTerm);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Context menu
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			TermData termSelected = (TermData) termslistAdapter.getItem(info.position);
			String termTitle = termSelected.getTitle();
			menu.setHeaderTitle(termTitle);
			menu.add(0, CONTEXT_EDIT, 0, "Edit Term");
			menu.add(0, CONTEXT_DELETE, 0, "Delete Term");
		}
		
		public boolean onContextItemSelected(MenuItem menuItem) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
			contextSelection = info.position;
			
			switch (menuItem.getItemId()) {
			case CONTEXT_EDIT:
				Intent iAddTerm = new Intent("com.amrak.gradebook.ADDTERM");
				iAddTerm.putExtra("idEdit_Item", refIDPass_Term[contextSelection]);
				iAddTerm.putExtra("id_Mode", 1);
				startActivity(iAddTerm);
				dataReset();
				return true;
			case CONTEXT_DELETE:
				
				termsDB.open();
				Cursor cDelete = termsDB.getTerm(refIDPass_Term[contextSelection]);
				String titleDelete = cDelete.getString(cDelete.getColumnIndex("termTitle"));
				termsDB.close();
				
			    new AlertDialog.Builder(this)
			    .setTitle("Delete Term, Courses, Categories and Evaluations?")
			    .setMessage("Are you sure you want to delete \"" + titleDelete + "\" and ALL Courses, Categories and Evaluations within it?")
			    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						termsDB.open();
						Cursor cDelete = termsDB.getTerm(refIDPass_Term[contextSelection]);
						String titleDelete = cDelete.getString(cDelete.getColumnIndex("termTitle"));
						termsDB.deleteTerm(refIDPass_Term[contextSelection]);
						termsDB.close();
						
						coursesDB.open();
						coursesDB.deleteCoursesOfTerm(refIDPass_Term[contextSelection]);
						coursesDB.close();
						
						evalsDB.open();
						evalsDB.deleteEvaluationsOfTerm(refIDPass_Term[contextSelection]);
						evalsDB.close();
						
						categoriesDB.open();
						categoriesDB.deleteCategoriesOfTerm(refIDPass_Term[contextSelection]);
						categoriesDB.close();
						
						Toast toast = Toast.makeText(context, titleDelete + " and ALL Courses, Categories and Evaluations within it was deleted successfully.", Toast.LENGTH_SHORT);
						try {
							//center toast
							((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
						} catch (ClassCastException cce) {
							Log.d(TAG, cce.getMessage());
						}
						toast.show();
						dataReset();
					}
				})
			    .setNegativeButton("Cancel", null)
			    .show();
				
				return true;
			default:
				return super.onContextItemSelected(menuItem);
			}
		}
	
	
	public void dataReset() {
		//clear data
		terms.clear();
		// read database
		dataReadToList();

		// input listview data
		termslistAdapter.notifyDataSetChanged();
	}
	
	public void dataReadToList() {
		termsDB.open();
		Cursor c = termsDB.getAllTerms();
		int i = 0;
		refIDPass_Term = new int[c.getCount()];
		if (c.moveToFirst()) {
			do {
				refIDPass_Term[i] = c.getInt(c.getColumnIndex("_id")); // get ids of each.
				terms.add(new TermData(c.getInt(c.getColumnIndex("_id")), 
						c.getString(c.getColumnIndex("termTitle")), 
						c.getString(c.getColumnIndex("termStartDate")), 
						c.getString(c.getColumnIndex("termEndDate"))));
				i++;
			} while (c.moveToNext());
		}
		termsDB.close();
	}
	
}
