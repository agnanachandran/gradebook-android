package com.amrak.gradebook.db.adapter;

import java.util.HashMap;

import com.amrak.gradebook.CustomSuggestionProvider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class EvaluationsDBAdapter extends DBAdapter {

    // TODO decide if notes field are needed for evaluations

    // database fields
    public static final String ROW_ID = "_id";
    public static final String EVAL_TITLE = "evalTitle";
    public static final String EVAL_MARK = "evalMark";
    public static final String EVAL_OUTOF = "evalOutOf";
    public static final String EVAL_WEIGHT = "evalWeight";
    public static final String EVAL_DATE = "evalDate";
    public static final String TERM_REFERENCE = "termRef";
    public static final String COURSE_REFERENCE = "courseRef";
    public static final String CATEGORY_REFERENCE = "catRef";
    private static final String TAG = "EvaluationsDBAdapter";

    // database name
    public static final String DATABASE_TABLE = "evaluations";
	
/*	// Projection map for search suggestions
    private static HashMap<String, String> mColumnMapEvaluations = buildColumnMapForEvaluations();
    
    // Projection map factory method
	private static HashMap<String, String> buildColumnMapForEvaluations() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ROW_ID, ROW_ID + " AS " + ROW_ID);
		map.put(EVAL_TITLE, EVAL_TITLE + " AS " + EVAL_TITLE);
		return map;
	}*/
	
	//Uri for Search Suggest
	public static final Uri SUGGEST_URI = Uri.parse("content://" + CustomSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/" + DATABASE_TABLE);

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            The Context within which to work
     */
    public EvaluationsDBAdapter(Context ctx) {
        super(ctx);
    }

    /**
     * Create an evaluation. If the evaluation is successfully created return
     * the new rowId for that evaluation, otherwise return a -1 to indicate
     * failure.
     * 
     * @param evalTitle
     *            title of the evaluation
     * @param evalMark
     *            mark of the evaluation
     * @param evalOutOf
     *            out of of the evaluation
     * @param evalWeight
     *            weight of the evaluation
     * @param evalDate
     *            date of the evaluation
     * @param catRef
     *            category reference of the evaluation
     * @return rowId or -1 if failed
     */
    public long createEvaluation(String evalTitle, double evalMark, double evalOutOf,
            double evalWeight, String evalDate, int termRef, int courseRef, int catRef) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(EVAL_TITLE, evalTitle);
        initialValues.put(EVAL_MARK, evalMark);
        initialValues.put(EVAL_OUTOF, evalOutOf);
        initialValues.put(EVAL_WEIGHT, evalWeight);
        initialValues.put(EVAL_DATE, evalDate);
        initialValues.put(TERM_REFERENCE, termRef);
        initialValues.put(COURSE_REFERENCE, courseRef);
        initialValues.put(CATEGORY_REFERENCE, catRef);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the evaluation with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvaluation(long rowId) {

        return mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Delete the evaluation with the given termRefID
     * 
     * @param termRefID
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvaluationsOfTerm(long termRefID) {

        return mDb.delete(DATABASE_TABLE, TERM_REFERENCE + "=" + termRefID, null) > 0; //$NON-NLS-1$
    }

    /**
     * Delete the evaluation with the given courseRefID
     * 
     * @param courseRefID
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvaluationsOfCourse(long courseRefID) {

        return mDb.delete(DATABASE_TABLE, COURSE_REFERENCE + "=" + courseRefID, null) > 0; //$NON-NLS-1$
    }

    /**
     * Delete the evaluation with the given categoryRefID
     * 
     * @param courseRefID
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvaluationsOfCategory(long categoryRefID) {

        return mDb.delete(DATABASE_TABLE, CATEGORY_REFERENCE + "=" + categoryRefID, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all evaluations in the database
     * 
     * @return Cursor over all evaluations
     */
    public Cursor getAllEvaluations() {

        return mDb.query(DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at all the evaluation that matches the given
     * courseRefID
     * 
     * @param courseRefID
     *            ref id
     * 
     * @return Cursor positioned to all evaluations of course, if found
     * @throws SQLException
     *             if evaluation could not be found/retrieved
     */
    public Cursor getEvaluationsOfCourse(long courseRefID) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                COURSE_REFERENCE + "=" + courseRefID, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at all the evaluation that matches the given
     * categoryRefID
     * 
     * @param courseRefID
     *            ref id
     * 
     * @return Cursor positioned to all evaluations of course, if found
     * @throws SQLException
     *             if evaluation could not be found/retrieved
     */
    public Cursor getEvaluationsOfCategory(long categoryRefID) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                CATEGORY_REFERENCE + "=" + categoryRefID, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the evaluation that matches the given rowId
     * 
     * @param rowId
     * @return Cursor positioned to matching evaluation, if found
     * @throws SQLException
     *             if evaluation could not be found/retrieved
     */
    public Cursor getEvaluation(long rowId) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // (ALL CATEGORIES) cursor that gets evaluations by date in asc order, look
    // at second last parameter of the query function
    public Cursor getEvaluationSortByDate(long courseRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                COURSE_REFERENCE + "=" + courseRefID, null, null, null, EVAL_DATE + " ASC", null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // (ALL CATEGORIES) cursor that gets evaluations by name in asc order, look
    // at second last parameter of the query function
    public Cursor getEvaluationSortByName(long courseRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                COURSE_REFERENCE + "=" + courseRefID, null, null, null, EVAL_TITLE + " ASC", null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
	
    /**
     * Return a cursor that has evaluations from the entire database whose title
     * partially matches the query.
     * @param query
     * @return cursor of evaluations sorted by EVAL_TITLE in ascending order
     * */
    public Cursor getEvaluationSortByName(String query) {
    	SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    	String[] columns = new String[] { ROW_ID, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE, EVAL_TITLE};
    	String selection = EVAL_TITLE + " LIKE ?";
    	String[] selectionArgs = new String[] {"%" + query + "%"};
    	String orderBy = EVAL_TITLE;
    	
		builder.setTables(DATABASE_TABLE);
		Cursor c = builder.query(mDb, columns, selection, selectionArgs, null, null, orderBy);

		return c;
    }

    // (ALL CATEGORIES) cursor that gets evaluations by weight in desc order,
    // look at second last parameter of the query function
    public Cursor getEvaluationSortByWeight(long courseRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                COURSE_REFERENCE + "=" + courseRefID, null, null, null, EVAL_WEIGHT + " DESC", null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // (ALL CATEGORIES) cursor that gets evaluations by category in asc order,
    // look at second last parameter of the query function
    public Cursor getEvaluationSortByCategory(long courseRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                COURSE_REFERENCE + "=" + courseRefID, null, null, null,
                CATEGORY_REFERENCE + " ASC", null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // (SPEC.CATEGORIES) cursor that gets evaluations by date in asc order, look
    // at second last parameter of the query function
    public Cursor getEvalCatSortByDate(long categoryRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                CATEGORY_REFERENCE + "=" + categoryRefID, null, null, null, EVAL_DATE + " ASC",
                null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getEvalCatSortByName(long categoryRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                CATEGORY_REFERENCE + "=" + categoryRefID, null, null, null, EVAL_TITLE + " ASC",
                null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getEvalCatSortByWeight(long categoryRefID) throws SQLException {
        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, EVAL_TITLE, EVAL_MARK, EVAL_OUTOF,
                EVAL_WEIGHT, EVAL_DATE, TERM_REFERENCE, COURSE_REFERENCE, CATEGORY_REFERENCE },
                CATEGORY_REFERENCE + "=" + categoryRefID, null, null, null, EVAL_WEIGHT + " DESC",
                null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the evaluation.
     * 
     * @param rowId
     * @param evalTitle
     *            title of the evaluation
     * @param evalMark
     *            mark of the evaluation
     * @param evalOutOf
     *            out of of the evaluation
     * @param evalWeight
     *            weight of the evaluation
     * @param evalDate
     *            date of the evaluation
     * @param termRef
     *            term reference of the evaluation
     * @param courseRef
     *            course reference of the evaluation
     * @param catRef
     *            category reference of the evaluation
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateEvaluation(long rowId, String evalTitle, double evalMark,
            double evalOutOf, double evalWeight, String evalDate, int termRef, int courseRef,
            int catRef) {
        ContentValues args = new ContentValues();

        args.put(EVAL_TITLE, evalTitle);
        args.put(EVAL_MARK, evalMark);
        args.put(EVAL_OUTOF, evalOutOf);
        args.put(EVAL_WEIGHT, evalWeight);
        args.put(EVAL_DATE, evalDate);
        args.put(TERM_REFERENCE, termRef);
        args.put(COURSE_REFERENCE, courseRef);
        args.put(CATEGORY_REFERENCE, catRef);

        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
    }
	
}