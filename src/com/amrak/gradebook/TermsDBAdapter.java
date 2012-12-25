package com.amrak.gradebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class TermsDBAdapter extends DBAdapter {
	
	//database fields
    public static final String ROW_ID = "_id";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_DATESTART = "termStartDate";
    public static final String TERM_DATEEND = "termEndDate";
    private static final String TAG = "TermsDBAdapter";
    
    //database name
    private static final String DATABASE_TABLE = "terms";

    /**
     * Constructor - takes the context to allow the database to be opened/created
     * 
     * @param ctx The Context within which to work
     */
    public TermsDBAdapter(Context ctx) {
        super(ctx);
    }

    /**
     * Create a term. If the term is successfully created return the new
     * rowId for that term, otherwise return a -1 to indicate failure.
     * 
     * @param termTitle title of term
     * @param termDateStart start date of term
     * @param termDateEnd end date of term
     * @return rowId or -1 if failed
     */
    public long createTerm(String termTitle, String termDateStart, String termDateEnd){
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(TERM_TITLE, termTitle);
        initialValues.put(TERM_DATESTART, termDateStart);
        initialValues.put(TERM_DATEEND, termDateEnd);
        
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the term with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteTerm(long rowId) {

        return mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all terms in the database
     * 
     * @return Cursor over all terms
     */
    public Cursor getAllTerms() {

        return mDb.query(DATABASE_TABLE, new String[] { ROW_ID,
        		TERM_TITLE, TERM_DATESTART, TERM_DATEEND }, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the term that matches the given rowId
     * 
     * @param rowId
     * @return Cursor positioned to matching term, if found
     * @throws SQLException if term could not be found/retrieved
     */
    public Cursor getTerm(long rowId) throws SQLException {

        Cursor mCursor =

        mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,
        		TERM_TITLE, TERM_DATESTART, TERM_DATEEND }, ROW_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the term.
     * 
     * @param rowId
     * @param termTite title of term
     * @param termDateStart start date of term
     * @param termDateEnd end date of term
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateTerm(long rowId, String termTitle, String termDateStart, String termDateEnd){
        ContentValues args = new ContentValues();
        
        args.put(TERM_TITLE, termTitle);
        args.put(TERM_DATESTART, termDateStart);
        args.put(TERM_DATEEND, termDateEnd);

        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }
    
}