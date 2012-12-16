package com.amrak.gradebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoriesDBAdapter extends DBAdapter {
	
	//database fields
    public static final String ROW_ID = "_id";
    public static final String CAT_TITLE = "catTitle";
    public static final String CAT_WEIGHT = "catWeight";
    public static final String TERM_REFERENCE = "termRef";
    public static final String COURSE_REFERENCE = "courseRef"; 
    private static final String TAG = "CategoriesDBAdapter";
    
	//database name
    private static final String DATABASE_TABLE = "categories";

    /**
     * Constructor - takes the context to allow the database to be opened/created
     * 
     * @param ctx The Context within which to work
     */
    public CategoriesDBAdapter(Context ctx) {
        super(ctx);
    }

    /**
     * Create a category. If the category is successfully created return the new
     * rowId for that category, otherwise return a -1 to indicate failure.
     * 
     * @param catTitle title of the Category
     * @param catWeight weight of the Category
     * @param courseRef reference to the course
     * @return rowId or -1 if failed
     */
    public long createCategory(String catTitle, double catWeight, int termRef, int courseRef){
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(CAT_TITLE, catTitle);
        initialValues.put(CAT_WEIGHT, catWeight);
        initialValues.put(TERM_REFERENCE, termRef);
        initialValues.put(COURSE_REFERENCE, courseRef);
        
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

        return mDb.query(DATABASE_TABLE, new String[] { ROW_ID,
        		CAT_TITLE, CAT_WEIGHT, TERM_REFERENCE, COURSE_REFERENCE }, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the category that matches the given rowId
     * 
     * @param rowId
     * @return Cursor positioned to matching category, if found
     * @throws SQLException if category could not be found/retrieved
     */
    public Cursor getCategory(long rowId) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,
        		CAT_TITLE, CAT_WEIGHT, TERM_REFERENCE, COURSE_REFERENCE }, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Return a Cursor positioned at the category that matches the given courseRefID
     * 
     * @param courseRefID
     * @return Cursor positioned to matching category, if found
     * @throws SQLException if category could not be found/retrieved
     */
    public Cursor getCategoriesOfCourse(long courseRefID) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,
        		CAT_TITLE, CAT_WEIGHT, TERM_REFERENCE, COURSE_REFERENCE }, COURSE_REFERENCE + "=" + courseRefID, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the category.
     * 
     * @param rowId
     * @param catTitle title of the Category
     * @param catWeight weight of the Category
     * @param courseRef reference to the course
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateCategory(long rowId, String catTitle, double catWeight, int termRef, int courseRef){
        ContentValues args = new ContentValues();

        args.put(CAT_TITLE, catTitle);
        args.put(CAT_WEIGHT, catWeight);
        args.put(TERM_REFERENCE, termRef);
        args.put(COURSE_REFERENCE, courseRef);
        
        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }

}