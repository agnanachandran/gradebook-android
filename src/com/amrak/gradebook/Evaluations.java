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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Evaluations extends Activity {

	// database
	CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
	EvaluationsDBAdapter evalsDB = new EvaluationsDBAdapter(this);

	// context
	Context context = this;

	// view(s)
	ExpandableListView expListView;
	TextView courseTitle;
	TextView courseMark;
	TextView courseCode;

	// listAdapters
	private CoursesListAdapter courseslistAdapter;
	private EvaluationsExpListAdapter expListAdapter;

	// variables
	final private String TAG = "Evaluations";
	int[] refIDPass_Evaluation;
	int refIDGet_Term;
	int refIDGet_Course;
	int contextSelection;
	
	// variable passed into datareadtolist for sorting; default to zero, which is sorting by date
	int sort = 0;
	
	// data
	public static final int CONTEXT_EDIT = 0;
	public static final int CONTEXT_DELETE = 1;
	CourseData courseData;
	// expListview parent rows
	ArrayList<EvalData> eval_parent = new ArrayList<EvalData>();
	// expListview child rows
	ArrayList<ArrayList<EvalData>> eval_childs = new ArrayList<ArrayList<EvalData>>();
	ArrayList<EvalData> eval_child = new ArrayList<EvalData>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluations);

		// initialization of views
		courseTitle = (TextView) findViewById(R.id.tvEvalCourseTitle);
		courseMark = (TextView) findViewById(R.id.tvEvalCourseMark);
		courseCode = (TextView) findViewById(R.id.tvEvalCourseCode);
		expListView = (ExpandableListView) findViewById(R.id.elvEvalList);

		// initialization of values
		Intent iEvaluations = getIntent();
		refIDGet_Term = iEvaluations.getIntExtra("refID_Term", -1);
		refIDGet_Course = iEvaluations.getIntExtra("refID_Course", -1);
		
		//term
		coursesDB.open();
		Cursor cCourse = coursesDB.getCourse(refIDGet_Course);
		
		courseData = new CourseData(cCourse.getInt(cCourse.getColumnIndex("_id")), 
				cCourse.getString(cCourse.getColumnIndex("courseTitle")), 
				cCourse.getString(cCourse.getColumnIndex("courseCode")), 
				cCourse.getInt(cCourse.getColumnIndex("courseUnits")), 
				cCourse.getString(cCourse.getColumnIndex("notes")), 
				cCourse.getInt(cCourse.getColumnIndex("termRef")));
		
		courseTitle.setText(courseData.getTitle());
		courseCode.setText(courseData.getCode()); 
		courseMark.setText(String.valueOf(courseData.getMark()));
		coursesDB.close();
		
		// read database
		dataReadToList();
		
		// input listview data
		expListAdapter = new EvaluationsExpListAdapter(this, eval_parent, eval_childs);
		expListView.setAdapter(expListAdapter);
		
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
			        int len = expListAdapter.getGroupCount();

			        for(int i=0; i<len; i++) {
			            if(i != groupPosition) {
			            	expListView.collapseGroup(i);
			            }
			        }
			}
			
		});
		registerForContextMenu(expListView);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		dataReset();
	}

	// Options menu 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_evaluations, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:
			Intent i = new Intent("com.amrak.gradebook.SETTINGS");
			startActivity(i);
			break;
		case R.id.addeval:
			Intent iAddEval = new Intent("com.amrak.gradebook.ADDEVAL");
			iAddEval.putExtra("refID_Course", refIDGet_Course);
			iAddEval.putExtra("refID_Term", refIDGet_Term);
			iAddEval.putExtra("id_Mode", 0);
			startActivity(iAddEval);
			break;
		case R.id.addcat:
			Intent iAddCat = new Intent("com.amrak.gradebook.ADDCAT");
			iAddCat.putExtra("refID_Course", refIDGet_Course);
			iAddCat.putExtra("refID_Term", refIDGet_Term);
			startActivity(iAddCat);
			break;
		case R.id.sortByDate:
			sort = 0;
			dataReset();
			break;
		case R.id.sortByName:
			sort = 1;
			dataReset();
			break;
		case R.id.sortByWeight:
			sort = 2;
			dataReset();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Context menu
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		int child = ExpandableListView.getPackedPositionChild(info.packedPosition);
		// Only create a context menu for child items
		if (type == 0) {
			// Array created earlier when we built the expandable list
			EvalData evalSelected = (EvalData) expListAdapter.getGroup(group);
			String evalTitle = evalSelected.getTitle();
			menu.setHeaderTitle(evalTitle);
			menu.add(0, CONTEXT_EDIT, 0, "Edit Evaluation");
			menu.add(0, CONTEXT_DELETE, 0, "Delete Evaluation");
		}
	}
	
	public boolean onContextItemSelected(MenuItem menuItem) {
		ExpandableListContextMenuInfo info =
		(ExpandableListContextMenuInfo) menuItem.getMenuInfo();
		int groupPos = 0, childPos = 0;
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
		groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
		}
		//Pull values from the array we built when we created the list
		contextSelection = groupPos;
		
		switch (menuItem.getItemId()) {
		case CONTEXT_EDIT:
			Intent iAddEval = new Intent("com.amrak.gradebook.ADDEVAL");
			iAddEval.putExtra("refID_Course", refIDGet_Course);
			iAddEval.putExtra("refID_Term", refIDGet_Term);
			iAddEval.putExtra("idEdit_Item", refIDPass_Evaluation[contextSelection]);
			iAddEval.putExtra("id_Mode", 1);
			startActivity(iAddEval);
			dataReset();
			return true;
		case CONTEXT_DELETE:
			
			evalsDB.open();
			Cursor cDelete = evalsDB.getEvaluation(refIDPass_Evaluation[contextSelection]);
			String titleDelete = cDelete.getString(cDelete.getColumnIndex("evalTitle"));
			evalsDB.close();
			
		    new AlertDialog.Builder(this)
		    .setTitle("Delete Evaluation?")
		    .setMessage("Are you sure you want to delete \"" + titleDelete + "\"?")
		    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					evalsDB.open();
					Cursor cDelete = evalsDB.getEvaluation(refIDPass_Evaluation[contextSelection]);
					String titleDelete = cDelete.getString(cDelete.getColumnIndex("evalTitle"));
					evalsDB.deleteEvaluation(refIDPass_Evaluation[contextSelection]);
					evalsDB.close();
					
					Toast toast = Toast.makeText(context, titleDelete + " was deleted successfully.", Toast.LENGTH_SHORT);
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
		eval_parent.clear();
		eval_child.clear();
		eval_childs.clear();

		// read database
		dataReadToList();

		// input listview data
		expListAdapter.notifyDataSetChanged();
	}
	
	public void dataReadToList() {
		evalsDB.open();

		Cursor c = evalsDB.getEvaluationSortByDate(refIDGet_Course);
		
		if (sort == 0) {
			c = evalsDB.getEvaluationSortByDate(refIDGet_Course);			
		}
		else if (sort == 1){
			c = evalsDB.getEvaluationSortByName(refIDGet_Course);
		}
		else if (sort == 2){
			c = evalsDB.getEvaluationSortByWeight(refIDGet_Course);
		}
		
		int i = 0;
		refIDPass_Evaluation = new int[c.getCount()];
		if (c.moveToFirst()) {
			do {
				refIDPass_Evaluation[i] = c.getInt(c.getColumnIndex("_id"));
				eval_parent.add(new EvalData(c.getString(c.getColumnIndex("evalTitle")), 
						c.getInt(c.getColumnIndex("evalMark")), 
						c.getInt(c.getColumnIndex("evalOutOf")),
						c.getInt(c.getColumnIndex("evalWeight")),
						c.getString(c.getColumnIndex("evalDate")),
						c.getInt(c.getColumnIndex("termRef")),
						c.getInt(c.getColumnIndex("courseRef")),
						c.getInt(c.getColumnIndex("catRef")),
						context));
				eval_child.add(new EvalData(c.getString(c.getColumnIndex("evalTitle")), 
						c.getInt(c.getColumnIndex("evalMark")), 
						c.getInt(c.getColumnIndex("evalOutOf")),
						c.getInt(c.getColumnIndex("evalWeight")),
						c.getString(c.getColumnIndex("evalDate")),
						c.getInt(c.getColumnIndex("termRef")),
						c.getInt(c.getColumnIndex("courseRef")),
						c.getInt(c.getColumnIndex("catRef")), 
						context));
				eval_childs.add(eval_child);
				eval_child = new ArrayList<EvalData>();
				i++;
			} while (c.moveToNext());
		}

		evalsDB.close();
	}
}