package ca.projectkarma.gradetrackr.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ca.projectkarma.gradetrackr.EditMode;
import ca.projectkarma.gradetrackr.Utils;
import ca.projectkarma.gradetrackr.adapter.CoursesListAdapter;
import ca.projectkarma.gradetrackr.db.adapter.CategoriesDBAdapter;
import ca.projectkarma.gradetrackr.db.adapter.CoursesDBAdapter;
import ca.projectkarma.gradetrackr.db.adapter.EvaluationsDBAdapter;
import ca.projectkarma.gradetrackr.db.adapter.TermsDBAdapter;
import ca.projectkarma.gradetrackr.model.CourseData;
import ca.projectkarma.gradetrackr.model.TermData;

import ca.projectkarma.gradetrackr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Courses extends Activity {

    // database
    TermsDBAdapter termsDB = new TermsDBAdapter(this);
    CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
    CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
    EvaluationsDBAdapter evalsDB = new EvaluationsDBAdapter(this);
    RelativeLayout rLayoutLabels;
    TextView tvNoCourses;
    // context
    Context context = this;

    // view(s)
    ListView listView;
    TextView termTitle;
    TextView termDate;
    // TextView termMark;
    View vCourseDivLine;
    // listAdapters
    private CoursesListAdapter courseslistAdapter;

    // data
    public static final int CONTEXT_EDIT = 0;
    public static final int CONTEXT_DELETE = 1;
    TermData termData;

    // courses
    ArrayList<CourseData> courses = new ArrayList<CourseData>();

    // variables
    int refIDGet_Term;
    int contextSelection;

    DecimalFormat twoDForm = new DecimalFormat("0.00");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // up button in action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent iCourse = getIntent();
        refIDGet_Term = iCourse.getIntExtra("refID_Term", -1);
        // TODO Do something with reference gotten such as calculate term
        // average
        // setTitle(String.valueOf(refIDGet));

        // initialization of views
        termTitle = (TextView) findViewById(R.id.tvCoursesTermTitle);
        termDate = (TextView) findViewById(R.id.tvCoursesTermDate);
        // termMark = (TextView) findViewById(R.id.tvCoursesTermMark);
        listView = (ListView) findViewById(R.id.lvCourses);
        rLayoutLabels = (RelativeLayout) findViewById(R.id.rLayoutLabelCourses);
        tvNoCourses = (TextView) findViewById(R.id.tvNoCourses);
        vCourseDivLine = (View) findViewById(R.id.vCourseDivLine);

        // term
        termsDB.open();
        Cursor cTerm = termsDB.getTerm(refIDGet_Term);

        termData = new TermData(cTerm.getInt(cTerm.getColumnIndex("_id")), cTerm.getString(cTerm
                .getColumnIndex("termTitle")), cTerm.getString(cTerm
                .getColumnIndex("termStartDate")), cTerm.getString(cTerm
                .getColumnIndex("termEndDate")), context);

        termTitle.setText(termData.getTitle() + " ("
                + String.valueOf(twoDForm.format(termData.getMark())) + ")");
        termDate.setText(Utils.toFullMonthYear(termData.getDateStart()) + " - "
                + Utils.toFullMonthYear(termData.getDateEnd()));
        // termMark.setText(String.valueOf(twoDForm.format(termData.getMark())));
        termsDB.close();

        // read data from database
        dataReadToList();

        // input listview data
        courseslistAdapter = new CoursesListAdapter(this, courses);
        listView.setAdapter(courseslistAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                try
                {
                    @SuppressWarnings("rawtypes")
                    Class cCategories;
                    cCategories = Class.forName("ca.projectkarma.gradetrackr.activity.Categories");
                    Intent iCategories = new Intent(Courses.this, cCategories);
                    iCategories.putExtra("refID_Term", refIDGet_Term);
                    iCategories.putExtra("refID_Course", courses.get(position).getDatabaseID());
                    startActivity(iCategories);
                }
                catch (ClassNotFoundException e)
                {
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
        switch (item.getItemId())
        {
//            case R.id.preferences:
//                Intent i = new Intent("ca.projectkarma.gradetrackr.activity.SETTINGS");
//                startActivity(i);
//                break;
            case R.id.search:
            	Intent iSearch = new Intent(this, Search.class);
            	startActivity(iSearch);
            	break;
            case R.id.addcourse:
                Intent iAddCourse = new Intent("ca.projectkarma.gradetrackr.activity.ADDCOURSE");
                iAddCourse.putExtra("refID_Term", refIDGet_Term);
                startActivity(iAddCourse);
                break;
            case R.id.tasks:
                Intent iTask = new Intent("ca.projectkarma.gradetrackr.activity.TASKLIST");
                startActivity(iTask);
                break;
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed
                // in the Action Bar.
                Intent parentActivityIntent = new Intent(this, Terms.class);
                parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentActivityIntent);
                finish();
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem
                .getMenuInfo();
        contextSelection = info.position;

        switch (menuItem.getItemId())
        {
            case CONTEXT_EDIT:
                Intent iAddCourse = new Intent("ca.projectkarma.gradetrackr.activity.ADDCOURSE");
                iAddCourse.putExtra("refID_Term", refIDGet_Term);
                iAddCourse.putExtra("idEdit_Item", courses.get(contextSelection).getDatabaseID());
                iAddCourse.putExtra("id_Mode", EditMode.EDIT_MODE);
                startActivity(iAddCourse);
                dataReset();
                return true;
            case CONTEXT_DELETE:

                coursesDB.open();
                Cursor cDelete = coursesDB.getCourse(courses.get(contextSelection).getDatabaseID());
                String titleDelete = cDelete.getString(cDelete.getColumnIndex("courseTitle"));
                coursesDB.close();

                new AlertDialog.Builder(this)
                        .setTitle("Delete Course, Categories and Evaluations?")
                        .setMessage(
                                "Are you sure you want to delete \"" + titleDelete
                                        + "\" and ALL Categories and Evaluations within it?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                coursesDB.open();
                                Cursor cDelete = coursesDB
                                        .getCourse(courses.get(contextSelection).getDatabaseID());
                                String titleDelete = cDelete.getString(cDelete
                                        .getColumnIndex("courseTitle"));
                                coursesDB.deleteCourse(courses.get(contextSelection).getDatabaseID());
                                coursesDB.close();

                                evalsDB.open();
                                evalsDB.deleteEvaluationsOfCourse(courses.get(contextSelection).getDatabaseID());
                                evalsDB.close();

                                categoriesDB.open();
                                categoriesDB
                                        .deleteCategoriesOfCourse(courses.get(contextSelection).getDatabaseID());
                                categoriesDB.close();

                                Toast toast = Toast
                                        .makeText(
                                                context,
                                                titleDelete
                                                        + " and ALL Categories and Evaluations within it was deleted successfully.",
                                                Toast.LENGTH_SHORT);
                                try
                                {
                                    // center toast
                                    ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                                            .setGravity(Gravity.CENTER_HORIZONTAL);
                                }
                                catch (ClassCastException cce)
                                {
                                    
                                }
                                toast.show();
                                dataReset();
                            }
                        }).setNegativeButton("Cancel", null).show();

                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    public void dataReset() {
        // clear data
        courses.clear();
        // read database
        dataReadToList();

        // input listview data
        courseslistAdapter.notifyDataSetChanged();

        // refreshes term mark after sublevel changes
        termsDB.open();
        Cursor cTerm = termsDB.getTerm(refIDGet_Term);

        termData = new TermData(cTerm.getInt(cTerm.getColumnIndex("_id")), cTerm.getString(cTerm
                .getColumnIndex("termTitle")), cTerm.getString(cTerm
                .getColumnIndex("termStartDate")), cTerm.getString(cTerm
                .getColumnIndex("termEndDate")), context);

        termTitle.setText(termData.getTitle() + " ("
                + String.valueOf(twoDForm.format(termData.getMark())) + ")");
        termsDB.close();
    }

    public void dataReadToList() {
        coursesDB.open();
        Cursor c = coursesDB.getCoursesOfTerm(refIDGet_Term);
        // Cursor c = coursesDB.getAllCourses();
        if (c.moveToFirst())
        {
            do
            {
                courses.add(new CourseData(c.getInt(c.getColumnIndex("_id")), c.getString(c
                        .getColumnIndex("courseTitle")),
                        c.getString(c.getColumnIndex("courseCode")), c.getInt(c
                                .getColumnIndex("courseUnits")), c.getString(c
                                .getColumnIndex("notes")), c.getInt(c.getColumnIndex("termRef")),
                        context));
            }
            while (c.moveToNext());
        }

        // set label and divider to visible/invisible if there is at least 1
        // course
        if (c.getCount() > 0)
        {
            rLayoutLabels.setVisibility(View.VISIBLE);
            vCourseDivLine.setVisibility(View.VISIBLE);
            tvNoCourses.setVisibility(View.INVISIBLE);

        }
        else
        {
            rLayoutLabels.setVisibility(View.INVISIBLE);
            vCourseDivLine.setVisibility(View.INVISIBLE);
            tvNoCourses.setVisibility(View.VISIBLE);
        }

        coursesDB.close();
    }
}