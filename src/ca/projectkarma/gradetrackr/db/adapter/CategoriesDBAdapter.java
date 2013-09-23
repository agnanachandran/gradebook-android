package ca.projectkarma.gradetrackr.db.adapter;

import ca.projectkarma.gradetrackr.CustomSuggestionProvider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CategoriesDBAdapter extends DBAdapter {

    // database fields
    public static final String ROW_ID = "_id";
    public static final String CAT_TITLE = "catTitle";
    public static final String CAT_WEIGHT = "catWeight";
    public static final String TERM_REFERENCE = "termRef";
    public static final String COURSE_REFERENCE = "courseRef";
    public static final String CAT_COLOR = "catColor";
    public static final String CAT_AVERAGE = "catAverage";

    // database name
    public static final String DATABASE_TABLE = "categories";
    
    //Uri for Search Suggest
  	public static final Uri SUGGEST_URI = Uri.parse("content://" + CustomSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/" + DATABASE_TABLE);

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            The Context within which to work
     */
    public CategoriesDBAdapter(Context ctx) {
        super(ctx);
    }

    /**
     * Create a category. If the category is successfully created return the new
     * rowId for that category, otherwise return a -1 to indicate failure.
     * 
     * @param catTitle
     *            title of the Category
     * @param catWeight
     *            weight of the Category
     * @param courseRef
     *            reference to the course
     * @param catColor
     *            color of the Category
     * @param catAverage
     *            average of the evaluations
     * @return rowId or -1 if failed
     */
    public long createCategory(String catTitle, double catWeight, int termRef, int courseRef,
            int catColor) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(CAT_TITLE, catTitle);
        initialValues.put(CAT_WEIGHT, catWeight);
        initialValues.put(TERM_REFERENCE, termRef);
        initialValues.put(COURSE_REFERENCE, courseRef);
        initialValues.put(CAT_COLOR, catColor);
        initialValues.put(CAT_AVERAGE, 100.00); /* Default average is 100.00 */

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the category with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteCategory(long rowId) {

        return mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Delete the category with the given courseRefID
     * 
     * @param courseRefID
     * @return true if deleted, false otherwise
     */
    public boolean deleteCategoriesOfCourse(long courseRefID) {

        return mDb.delete(DATABASE_TABLE, COURSE_REFERENCE + "=" + courseRefID, null) > 0; //$NON-NLS-1$
    }

    /**
     * Delete the category with the given termRefID
     * 
     * @param termRefID
     * @return true if deleted, false otherwise
     */
    public boolean deleteCategoriesOfTerm(long termRefID) {

        return mDb.delete(DATABASE_TABLE, TERM_REFERENCE + "=" + termRefID, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all courses in the database
     * 
     * @return Cursor over all categories
     */
    public Cursor getAllCategories() {

        return mDb.query(DATABASE_TABLE, new String[] { ROW_ID, CAT_TITLE, CAT_WEIGHT,
                TERM_REFERENCE, COURSE_REFERENCE, CAT_COLOR, CAT_AVERAGE }, null, null, null, null,
                null);
    }

    /**
     * Return a Cursor positioned at the category that matches the given rowId
     * 
     * @param rowId
     * @return Cursor positioned to matching category, if found
     * @throws SQLException
     *             if category could not be found/retrieved
     */
    public Cursor getCategory(long rowId) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, CAT_TITLE, CAT_WEIGHT,
                TERM_REFERENCE, COURSE_REFERENCE, CAT_COLOR, CAT_AVERAGE }, ROW_ID + "=" + rowId,
                null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the category that matches the given
     * courseRefID
     * 
     * @param courseRefID
     * @return Cursor positioned to matching category, if found
     * @throws SQLException
     *             if category could not be found/retrieved
     */
    public Cursor getCategoriesOfCourse(long courseRefID) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, CAT_TITLE, CAT_WEIGHT,
                TERM_REFERENCE, COURSE_REFERENCE, CAT_COLOR, CAT_AVERAGE }, COURSE_REFERENCE + "="
                + courseRefID, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Return a cursor that has categories from the entire database whose title
     * partially matches the query.
     * @param query
     * @return cursor of categories sorted by CAT_TITLE in ascending order
     * */
    public Cursor getCategoriesSortByName(String query) {
    	SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    	String[] columns = new String[] { ROW_ID, TERM_REFERENCE, COURSE_REFERENCE, CAT_TITLE};
    	String selection = CAT_TITLE + " LIKE ?";
    	String[] selectionArgs = new String[] {"%" + query + "%"};
    	String orderBy = CAT_TITLE;
    	
		builder.setTables(DATABASE_TABLE);
		Cursor c = builder.query(mDb, columns, selection, selectionArgs, null, null, orderBy);
		
		return c;
    }

    /**
     * Update the category.
     * 
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateCategory(long rowId, String catTitle, double catWeight, int termRef,
            int courseRef, int catColor) {
        ContentValues args = new ContentValues();

        args.put(CAT_TITLE, catTitle);
        args.put(CAT_WEIGHT, catWeight);
        args.put(TERM_REFERENCE, termRef);
        args.put(COURSE_REFERENCE, courseRef);
        args.put(CAT_COLOR, catColor);

        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
    }

    public boolean updateCategoryAverage(long rowId, double catAverage) {
        ContentValues args = new ContentValues();

        args.put(CAT_AVERAGE, catAverage);
        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
    }

}