package com.amrak.gradebook;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class DBAdapter {

    public static final String TAG = "DBAdapter";
    protected final Context mCtx; 
    private DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
	
    //Database Name and Version, Change version if updates through "onUpgrade" method after app release
    public static final String DATABASE_NAME = "GradeTrackrDB"; //$NON-NLS-1$
    public static final int DATABASE_VERSION = 1;

    //Sql query for creating tables
    private static final String CREATE_TABLE_TERM =
       "CREATE TABLE IF NOT EXISTS terms (_id INTEGER primary key autoincrement, " //$NON-NLS-1$
    + TermsDBAdapter.TERM_TITLE + " VARCHAR not null," //$NON-NLS-1$
    + TermsDBAdapter.TERM_DATESTART + " DATETIME," //$NON-NLS-1$
    + TermsDBAdapter.TERM_DATEEND + " DATETIME"  //$NON-NLS-1$
    + ");"; //$NON-NLS-1$ //$NON-NLS-2$
    
    private static final String CREATE_TABLE_COURSE =
       "CREATE TABLE IF NOT EXISTS courses (_id INTEGER primary key autoincrement, " //$NON-NLS-1$
    + CoursesDBAdapter.COURSE_TITLE + " VARCHAR not null," //$NON-NLS-1$
    + CoursesDBAdapter.COURSE_CODE + " VARCHAR," //$NON-NLS-1$
    + CoursesDBAdapter.COURSE_UNITS + " DECIMAL(10,4)," 
    + CoursesDBAdapter.COURSE_NOTES + " VARCHAR," 
    + CoursesDBAdapter.TERM_REFERENCE + " INT" //$NON-NLS-1$
    + ");"; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String CREATE_TABLE_CATEGORY =
       "CREATE TABLE IF NOT EXISTS categories (_id INTEGER primary key autoincrement, " //$NON-NLS-1$
    + CategoriesDBAdapter.CAT_TITLE + " VARCHAR not null," //$NON-NLS-1$
    + CategoriesDBAdapter.CAT_WEIGHT + " DECIMAL(10,4)," //$NON-NLS-1$
    + CategoriesDBAdapter.TERM_REFERENCE + " INT," //$NON-NLS-1$
    + CategoriesDBAdapter.COURSE_REFERENCE + " INT" //$NON-NLS-1$
    + ");"; //$NON-NLS-1$ //$NON-NLS-2$
    
    private static final String CREATE_TABLE_EVALUATION =
       "CREATE TABLE IF NOT EXISTS evaluations (_id INTEGER primary key autoincrement, " //$NON-NLS-1$
    + EvaluationsDBAdapter.EVAL_TITLE + " VARCHAR not null," //$NON-NLS-1$
    + EvaluationsDBAdapter.EVAL_MARK + " DECIMAL(10,4)," //$NON-NLS-1$
    + EvaluationsDBAdapter.EVAL_OUTOF + " DECIMAL(10,4)," 
    + EvaluationsDBAdapter.EVAL_WEIGHT + " DECIMAL(10,4)," 
    + EvaluationsDBAdapter.EVAL_DATE + " DATETIME," 
    + EvaluationsDBAdapter.TERM_REFERENCE + " INT," //$NON-NLS-1$
    + EvaluationsDBAdapter.COURSE_REFERENCE + " INT," //$NON-NLS-1$
    + EvaluationsDBAdapter.CATEGORY_REFERENCE + " INT" //$NON-NLS-1$
    + ");"; //$NON-NLS-1$ //$NON-NLS-2$
    
    protected static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_TERM);
        	db.execSQL(CREATE_TABLE_COURSE);
            db.execSQL(CREATE_TABLE_CATEGORY);
        	db.execSQL(CREATE_TABLE_EVALUATION);
            
            Log.w(TAG, "Created new databases.");
        }

        //Upgrading version, transfer data before dropping
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            //db.execSQL("DROP TABLE IF EXISTS courses");
            onCreate(db);
        }
    }
    
    /**
     * Constructor
     * @param ctx
     */
    public DBAdapter(Context ctx)
    {
        this.mCtx = ctx;
        //this.DBHelper = new DatabaseHelper(this.context);
    }

   /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DBAdapter
     */
    public DBAdapter open() throws SQLException 
    {
    	mDbHelper = new DatabaseHelper(mCtx);
    	mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db 
     * return type: void
     */
    public void close() {
        mDbHelper.close();
    }

}
