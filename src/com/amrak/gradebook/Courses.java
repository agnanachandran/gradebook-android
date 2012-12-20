package com.amrak.gradebook;

import java.util.ArrayList;

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
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Courses extends Activity {

	// database
	TermsDBAdapter termsDB = new TermsDBAdapter(this);
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
	CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
	EvaluationsDBAdapter evalsDB = new EvaluationsDBAdapter(this);
	
	// context
	Context context = this;

	// view(s)
	ListView listView;
	TextView termTitle;
	TextView termDate;
	TextView termMark;

	// listAdapters
	private CoursesListAdapter courseslistAdapter;

	// data
	public static final int CONTEXT_EDIT = 0;
	public static final int CONTEXT_DELETE = 1;
	TermData termData;
	// courses
	ArrayList<CourseData> courses = new ArrayList<CourseData>();
	
	// variables
	final private String TAG = "Courses";
	int[] refIDPass_Course;
	int refIDGet_Term;
	int contextSelection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);

		Intent iCourse = getIntent();
		refIDGet_Term = iCourse.getIntExtra("refID_Term", -1);
		// TODO Do something with reference gotten such as calculate term average
		//setTitle(String.valueOf(refIDGet));

		// initialization of views
		termTitle = (TextView) findViewById(R.id.tvCoursesTermTitle);
		termDate = (TextView) findViewById(R.id.tvCoursesTermDate);
		termMark = (TextView) findViewById(R.id.tvCoursesTermMark);
		listView = (ListView) findViewById(R.id.lvCourses);
		
		//term
		termsDB.open();
		Cursor cTerm = termsDB.getTerm(refIDGet_Term);
		
		termData = new TermData(cTerm.getInt(cTerm.getColumnIndex("_id")), 
				cTerm.getString(cTerm.getColumnIndex("termTitle")), 
				cTerm.getString(cTerm.getColumnIndex("termStartDate")), 
				cTerm.getString(cTerm.getColumnIndex("termEndDate")));
		
		termTitle.setText(termData.getTitle());
		termDate.setText(termData.getDateStart() + " - " + termData.getDateEnd()); 
		termMark.setText(String.valueOf(termData.getMark()));
		termsDB.close();
		
		// read data from database
		dataReadToList();
	
		// input listview data
		courseslistAdapter = new CoursesListAdapter(this, courses);
		listView.setAdapter(courseslistAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				try {
					@SuppressWarnings("rawtypes")
					Class cEvaluations;
					cEvaluations = Class.forName("com.amrak.gradebook.Evaluations");
					Intent iEvaluations = new Intent(Courses.this, cEvaluations);
					iEvaluations.putExtra("refID_Term", refIDGet_Term);
					iEvaluations.putExtra("refID_Course", refIDPass_Course[position]);
					startActivity(iEvaluations);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
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
			Intent iAddCourse = new Intent("com.amrak.gradebook.ADDCOURSE");
			iAddCourse.putExtra("refID_Term", refIDGet_Term);
			startActivity(iAddCourse);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Context menu
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		CourseData courseSelected = (CourseData) courseslistAdapter.getItem(info.position);
		String evalTitle = courseSelected.getTitle();
		menu.setHeaderTitle(evalTitle);
		menu.add(0, CONTEXT_EDIT, 0, "Edit Course");
		menu.add(0, CONTEXT_DELETE, 0, "Delete Course");
	}
	
	public boolean onContextItemSelected(MenuItem menuItem) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
		contextSelection = info.position;
		
		switch (menuItem.getItemId()) {
		case CONTEXT_EDIT:
			Intent iAddCourse = new Intent("com.amrak.gradebook.ADDCOURSE");
			iAddCourse.putExtra("refID_Term", refIDGet_Term);
			iAddCourse.putExtra("idEdit_Item", refIDPass_Course[contextSelection]);
			iAddCourse.putExtra("id_Mode", 1);
			startActivity(iAddCourse);
			dataReset();
			return true;
		case CONTEXT_DELETE:
			
			coursesDB.open();
			Cursor cDelete = coursesDB.getCourse(refIDPass_Course[contextSelection]);
			String titleDelete = cDelete.getString(cDelete.getColumnIndex("courseTitle"));
			coursesDB.close();
			
		    new AlertDialog.Builder(this)
		    .setTitle("Delete Course, Categories and Evaluations?")
		    .setMessage("Are you sure you want to delete \"" + titleDelete + "\" and ALL Categories and Evaluations within it?")
		    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					coursesDB.open();
					Cursor cDelete = coursesDB.getCourse(refIDPass_Course[contextSelection]);
					String titleDelete = cDelete.getString(cDelete.getColumnIndex("courseTitle"));
					coursesDB.deleteCourse(refIDPass_Course[contextSelection]);
					coursesDB.close();
					
					evalsDB.open();
					evalsDB.deleteEvaluationsOfCourse(refIDPass_Course[contextSelection]);
					evalsDB.close();
					
					categoriesDB.open();
					categoriesDB.deleteCategoriesOfCourse(refIDPass_Course[contextSelection]);
					categoriesDB.close();
					
					Toast toast = Toast.makeText(context, titleDelete + " and ALL Categories and Evaluations within it was deleted successfully.", Toast.LENGTH_SHORT);
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
		courses.clear();
		// read database
		dataReadToList();

		// input listview data
		courseslistAdapter.notifyDataSetChanged();
	}
	
	public void dataReadToList() {
		coursesDB.open();
		Cursor c = coursesDB.getCoursesOfTerm(refIDGet_Term);
		//Cursor c = coursesDB.getAllCourses();
		int i = 0;
		refIDPass_Course = new int[c.getCount()];
		if (c.moveToFirst()) {
			do {
				refIDPass_Course[i] = c.getInt(c.getColumnIndex("_id")); // get ids of each.
				courses.add(new CourseData(c.getInt(c.getColumnIndex("_id")), 
						c.getString(c.getColumnIndex("courseTitle")), 
						c.getString(c.getColumnIndex("courseCode")), 
						c.getInt(c.getColumnIndex("courseUnits")), 
						c.getString(c.getColumnIndex("notes")), 
						c.getInt(c.getColumnIndex("termRef")),
						context));
				i++;
			} while (c.moveToNext());
		}
		coursesDB.close();
	}
}