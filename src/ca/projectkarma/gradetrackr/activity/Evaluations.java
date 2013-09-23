package ca.projectkarma.gradetrackr.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import ca.projectkarma.gradetrackr.EditMode;
import ca.projectkarma.gradetrackr.R;
import ca.projectkarma.gradetrackr.adapter.EvaluationsExpListAdapter;
import ca.projectkarma.gradetrackr.db.adapter.CategoriesDBAdapter;
import ca.projectkarma.gradetrackr.db.adapter.CoursesDBAdapter;
import ca.projectkarma.gradetrackr.db.adapter.EvaluationsDBAdapter;
import ca.projectkarma.gradetrackr.model.CategoryData;
import ca.projectkarma.gradetrackr.model.CourseData;
import ca.projectkarma.gradetrackr.model.EvalData;

public class Evaluations extends Activity {

    // database
    CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
    CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
    EvaluationsDBAdapter evalsDB = new EvaluationsDBAdapter(this);

    // context
    Context context = this;

    // view(s)
    ExpandableListView expListView;
    TextView catTitle;
    TextView catMark;
    View catColor;
    TextView courseName; // course title and course code, e.g. ECE 105
    TextView noEvalAddEval;
    TextView tvNoEvals;
    RelativeLayout rLayoutLabels;
    View vEvalDivLine;

    // listAdapters
    private EvaluationsExpListAdapter expListAdapter;

    // variables
    int[] refIDPass_Evaluation;
    int refIDGet_Term;
    int refIDGet_Course;
    // this is initiated for savedInstanceState bundle; savedInstanceState needs
    // Category ref when
    // previous state was in filtered mode. Refer to protected void
    // savedInstanceState method
    int refIDGet_Category;
    int contextSelection;

    // this category id is for onCreate so that depending on which category sent
    // the intent to
    // this evaluations activity, the evaluations corresponding to the cat will
    // show
    int refIDReceive_Cat = -5;

    // variable passed into datareadtolist for sorting; default to zero, which
    // is sorting by date
    int sort = 0;
    int prevSort = 0; // variable used for context menu when unfiltering,
    // returns to old sorted format

    // string constant for sort when you place in the onSavedInstanceState()
    // method to keep
    // sort value even when phone has been turned so filters and sort options
    // are not changed
    static final String SORT = "sortVariable";
    static final String REFIDCAT = "refIdCat";
    static final String REFRECCAT = "refRecCat";

    // data
    public static final int CONTEXT_EDIT = 0;
    public static final int CONTEXT_DELETE = 1;
    public static final int CONTEXT_FILTER = 2;
    public static final int CONTEXT_UNFILTER = 3;

    CourseData courseData;
    CategoryData categoryData;
    // expListview parent rows
    List<EvalData> eval_parent = new ArrayList<EvalData>();
    // expListview child rows
    List<List<EvalData>> eval_childs = new ArrayList<List<EvalData>>();
    List<EvalData> eval_child = new ArrayList<EvalData>();

    DecimalFormat twoDForm = new DecimalFormat("0.00");

    // when phone rotates, onCreate is called again
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluations);

        // up button in action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization of views
        catTitle = (TextView) findViewById(R.id.tvEvalCatTitle);
        catMark = (TextView) findViewById(R.id.tvEvalCatMark);
        catColor = (View) findViewById(R.id.catColorEvalAct);
        courseName = (TextView) findViewById(R.id.tvEvalCourseName);
        expListView = (ExpandableListView) findViewById(R.id.elvEvalList);
        tvNoEvals = (TextView) findViewById(R.id.tvNoEvals);
        rLayoutLabels = (RelativeLayout) findViewById(R.id.rLayoutLabelEvaluations);
        vEvalDivLine = (View) findViewById(R.id.vEvalDivLine);

        // initialization of values
        Intent iEvaluations = getIntent();
        refIDGet_Term = iEvaluations.getIntExtra("refID_Term", -1);
        refIDGet_Course = iEvaluations.getIntExtra("refID_Course", -1);
        refIDReceive_Cat = iEvaluations.getIntExtra("refID_Category", -1);

        // course
        coursesDB.open();
        Cursor cCourse = coursesDB.getCourse(refIDGet_Course);

        courseData = new CourseData(cCourse.getInt(cCourse.getColumnIndex("_id")),
                cCourse.getString(cCourse.getColumnIndex("courseTitle")), cCourse.getString(cCourse
                        .getColumnIndex("courseCode")), cCourse.getInt(cCourse
                        .getColumnIndex("courseUnits")), cCourse.getString(cCourse
                        .getColumnIndex("notes")),
                cCourse.getInt(cCourse.getColumnIndex("termRef")), context);

        coursesDB.close();

        if (refIDReceive_Cat == 0)
        {
            catTitle.setText("All");
            catMark.setText(String.valueOf(twoDForm.format(courseData.getMark())));
            catColor.setVisibility(View.INVISIBLE);
        }
        else
        {
            // category
            categoriesDB.open();
            Cursor cCategory = categoriesDB.getCategory(refIDReceive_Cat);
            categoryData = new CategoryData(cCategory.getInt(cCategory.getColumnIndex("_id")),
                    cCategory.getString(cCategory.getColumnIndex("catTitle")),
                    cCategory.getInt(cCategory.getColumnIndex("catWeight")),
                    cCategory.getInt(cCategory.getColumnIndex("courseRef")),
                    cCategory.getInt(cCategory.getColumnIndex("termRef")),
                    cCategory.getInt(cCategory.getColumnIndex("catColor")), context);
            categoriesDB.close();
            catTitle.setText(categoryData.getTitle());
            catMark.setText(String.valueOf(twoDForm.format(categoryData.getMark())));
            catColor.setBackgroundColor(categoryData.getColor());
            sort = 5;
        }

        courseName.setText(courseData.getCode());

        // if there was a onSavedInstanceState before, retrieve original sort
        // variable
        if (savedInstanceState != null)
        {
            sort = savedInstanceState.getInt(SORT);
            refIDGet_Category = savedInstanceState.getInt(REFIDCAT);
            refIDReceive_Cat = savedInstanceState.getInt(REFRECCAT);
        }

        // read database
        dataReadToList();

        // input listview data
        expListAdapter = new EvaluationsExpListAdapter(this, eval_parent, eval_childs);
        expListView.setAdapter(expListAdapter);

        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                /*
                 * int len = expListAdapter.getGroupCount();
                 * 
                 * for(int i=0; i<len; i++) { if(i != groupPosition) {
                 * expListView.collapseGroup(i); } }
                 */
            }

        });
        registerForContextMenu(expListView);

    }

    // saved instance state to store bundle information (e.g. sort variable used
    // for determining
    // how the data is sorted) so it prevents data from resetting when phone is
    // turned sideways (example)
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SORT, sort);
        savedInstanceState.putInt(REFIDCAT, refIDGet_Category);
        savedInstanceState.putInt(REFRECCAT, refIDReceive_Cat);
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
            case R.id.addeval:
                Intent iAddEval = new Intent("ca.projectkarma.gradetrackr.activity.ADDEVAL");
                iAddEval.putExtra("refID_Course", refIDGet_Course);
                iAddEval.putExtra("refID_Term", refIDGet_Term);
                iAddEval.putExtra("refID_Category", refIDReceive_Cat);
                iAddEval.putExtra("id_Mode", EditMode.ADD_MODE);
                startActivity(iAddEval);
                break;
            case R.id.sortByDate:
                if (refIDReceive_Cat > 0) sort = 5;
                else
                    sort = 0;
                dataReset();
                break;
            case R.id.sortByName:
                if (refIDReceive_Cat > 0) sort = 6;
                else
                    sort = 1;
                dataReset();
                break;
            case R.id.sortByWeight:
                if (refIDReceive_Cat > 0) sort = 7;
                else
                    sort = 2;
                dataReset();
                break;
            case R.id.sortByCategory:
                if (refIDReceive_Cat > 0)
                {
                    break;
                }
                else
                {
                    sort = 3;
                    dataReset();
                }
                break;
            case R.id.collapseAll:
                int len = expListAdapter.getGroupCount();

                for (int h = 0; h < len; h++)
                {
                    expListView.collapseGroup(h);
                }
                break;
            case R.id.tasks:
                Intent iTask = new Intent("ca.projectkarma.gradetrackr.activity.TASKLIST");
                startActivity(iTask);
                break;
            case android.R.id.home:
                Intent iCategories = new Intent(this, Categories.class);
                iCategories.putExtra("refID_Course", refIDGet_Course);
                iCategories
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iCategories);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Context menu setup
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int group = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        // Only create a context menu for child items
        if (type == 0)
        {
            // Array created earlier when we built the expandable list
            EvalData evalSelected = (EvalData) expListAdapter.getGroup(group);
            String evalTitle = evalSelected.getTitle();

            // this is used when filtering a category, filters are controlled in
            // the context menu
            refIDGet_Category = evalSelected.getCategoryRef();
            menu.setHeaderTitle(evalTitle);
            menu.add(0, CONTEXT_EDIT, 0, "Edit Evaluation");
            menu.add(0, CONTEXT_DELETE, 0, "Delete Evaluation");
            if (refIDReceive_Cat > 0)
            {
            }
            else
            {
                menu.add(0, CONTEXT_FILTER, 0, "Filter by this Category");
                menu.add(0, CONTEXT_UNFILTER, 0, "Remove Filter");
            }
        }
    }

    // context menu selecting options (4 different options managed by a switch
    // statement
    public boolean onContextItemSelected(MenuItem menuItem) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuItem.getMenuInfo();
        int groupPos = 0;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP)
        {
            groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        }
        // Pull values from the array we built when we created the list
        contextSelection = groupPos;

        switch (menuItem.getItemId())
        {
            case CONTEXT_EDIT:
                Intent iAddEval = new Intent("ca.projectkarma.gradetrackr.activity.ADDEVAL");
                iAddEval.putExtra("refID_Course", refIDGet_Course);
                iAddEval.putExtra("refID_Term", refIDGet_Term);
                iAddEval.putExtra("refID_Category", refIDReceive_Cat);
                iAddEval.putExtra("idEdit_Item", refIDPass_Evaluation[contextSelection]);
                iAddEval.putExtra("id_Mode", EditMode.EDIT_MODE);
                startActivity(iAddEval);
                dataReset();
                return true;
            case CONTEXT_DELETE:

                evalsDB.open();
                Cursor cDelete = evalsDB.getEvaluation(refIDPass_Evaluation[contextSelection]);
                String titleDelete = cDelete.getString(cDelete.getColumnIndex("evalTitle"));
                evalsDB.close();

                new AlertDialog.Builder(this).setTitle("Delete Evaluation?")
                        .setMessage("Are you sure you want to delete \"" + titleDelete + "\"?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                evalsDB.open();
                                Cursor cDelete = evalsDB
                                        .getEvaluation(refIDPass_Evaluation[contextSelection]);
                                String titleDelete = cDelete.getString(cDelete
                                        .getColumnIndex("evalTitle"));
                                evalsDB.deleteEvaluation(refIDPass_Evaluation[contextSelection]);
                                evalsDB.close();

                                Toast toast = Toast.makeText(context, titleDelete
                                        + " was deleted successfully.", Toast.LENGTH_SHORT);
                                try
                                {
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
            case CONTEXT_FILTER:
                prevSort = sort;
                sort = 4;
                dataReset();

                return true;

            case CONTEXT_UNFILTER:
                sort = prevSort;
                dataReset();

                return true;

            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    // clears the arraylists and then reads new information before repopulating
    // useful when an evaluation is deleted or added or a way to sort data is
    // chosen
    public void dataReset() {
        // clear data
        eval_parent.clear();
        eval_child.clear();
        eval_childs.clear();

        // read database
        dataReadToList();

        // update listview with new data
        expListAdapter.notifyDataSetChanged();

        // update category mark after evaluations are changed
        if (refIDReceive_Cat > 0)
        {
            categoriesDB.open();
            Cursor cCategory = categoriesDB.getCategory(refIDReceive_Cat);
            categoryData = new CategoryData(cCategory.getInt(cCategory.getColumnIndex("_id")),
                    cCategory.getString(cCategory.getColumnIndex("catTitle")),
                    cCategory.getInt(cCategory.getColumnIndex("catWeight")),
                    cCategory.getInt(cCategory.getColumnIndex("courseRef")),
                    cCategory.getInt(cCategory.getColumnIndex("termRef")),
                    cCategory.getInt(cCategory.getColumnIndex("catColor")), context);
            categoriesDB.close();
            catMark.setText(String.valueOf(twoDForm.format(categoryData.getMark())));
        }
        else
        {
            coursesDB.open();
            Cursor cCourse = coursesDB.getCourse(refIDGet_Course);
            courseData = new CourseData(cCourse.getInt(cCourse.getColumnIndex("_id")),
                    cCourse.getString(cCourse.getColumnIndex("courseTitle")),
                    cCourse.getString(cCourse.getColumnIndex("courseCode")), cCourse.getInt(cCourse
                            .getColumnIndex("courseUnits")), cCourse.getString(cCourse
                            .getColumnIndex("notes")), cCourse.getInt(cCourse
                            .getColumnIndex("termRef")), context);
            coursesDB.close();
            catMark.setText(String.valueOf(twoDForm.format(courseData.getMark())));
        }
    }

    // reads data from the database to the vectors
    public void dataReadToList() {
        evalsDB.open();

        Cursor c = evalsDB.getEvaluationSortByDate(refIDGet_Course);

        // sort variables determine which cursor is chosen. The cursor chosen
        // returns a specific
        // set of data a specific way. sort == 0 gives a cursor that returns
        // evaluations based on
        // date in ascending order. .getEvaluationSortByDate() functions are
        // found in EvaluationsDBAdapter.java

        // for the "all" category
        if (sort == 0)
        {
            c = evalsDB.getEvaluationSortByDate(refIDGet_Course);
        }
        else if (sort == 1)
        {
            c = evalsDB.getEvaluationSortByName(refIDGet_Course);
        }
        else if (sort == 2)
        {
            c = evalsDB.getEvaluationSortByWeight(refIDGet_Course);
        }
        else if (sort == 3)
        {
            c = evalsDB.getEvaluationSortByCategory(refIDGet_Course);
        }
        // for the all category; filtering purposes
        else if (sort == 4)
        {
            c = evalsDB.getEvaluationsOfCategory(refIDGet_Category);
            // for specific categories
        }
        else if (sort == 5)
        {
            c = evalsDB.getEvalCatSortByDate(refIDReceive_Cat);
        }
        else if (sort == 6)
        {
            c = evalsDB.getEvalCatSortByName(refIDReceive_Cat);
        }
        else if (sort == 7)
        {
            c = evalsDB.getEvalCatSortByWeight(refIDReceive_Cat);
        }
        else
        {
            if (refIDReceive_Cat > 0) c = evalsDB.getEvalCatSortByDate(refIDReceive_Cat);
            else
                c = evalsDB.getEvaluationSortByDate(refIDGet_Course);
        }

        // adding data from the database to the vectors
        int i = 0;

        // an array that is used to store column indexes of each evaluation for
        // later use
        // such as in deleting an evaluation. Size is the number of evaluations
        // to be put in vectors
        refIDPass_Evaluation = new int[c.getCount()];
        if (c.moveToFirst())
        {
            do
            {
                refIDPass_Evaluation[i] = c.getInt(c.getColumnIndex("_id"));
                eval_parent.add(new EvalData(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("evalTitle")), c.getInt(c
                        .getColumnIndex("evalMark")), c.getInt(c.getColumnIndex("evalOutOf")), c
                        .getDouble(c.getColumnIndex("evalWeight")), c.getString(c
                        .getColumnIndex("evalDate")), c.getInt(c.getColumnIndex("termRef")), c
                        .getInt(c.getColumnIndex("courseRef")),
                        c.getInt(c.getColumnIndex("catRef")), context));
                eval_child.add(new EvalData(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("evalTitle")), c.getInt(c
                        .getColumnIndex("evalMark")), c.getInt(c.getColumnIndex("evalOutOf")), c
                        .getDouble(c.getColumnIndex("evalWeight")), c.getString(c
                        .getColumnIndex("evalDate")), c.getInt(c.getColumnIndex("termRef")), c
                        .getInt(c.getColumnIndex("courseRef")),
                        c.getInt(c.getColumnIndex("catRef")), context));
                eval_childs.add(eval_child);
                eval_child = new ArrayList<EvalData>();
                i++;
            }
            while (c.moveToNext());

        }
        if (c.getCount() > 0)
        {
            rLayoutLabels.setVisibility(View.VISIBLE);
            vEvalDivLine.setVisibility(View.VISIBLE);
            tvNoEvals.setVisibility(View.INVISIBLE);

        }
        else
        {
            rLayoutLabels.setVisibility(View.INVISIBLE);
            vEvalDivLine.setVisibility(View.INVISIBLE);
            tvNoEvals.setVisibility(View.VISIBLE);

        }

        evalsDB.close();
    }
}