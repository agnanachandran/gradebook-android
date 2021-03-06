package ca.projectkarma.gradetrackr.db.adapter;

import ca.projectkarma.gradetrackr.CustomSuggestionProvider;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class TaskDBAdapter extends DBAdapter {

    // database fields
    public static final String ROW_ID = "_id";
    public static final String TASK_TITLE = "taskTitle";
    public static final String TASK_DATEDUE = "taskDateDue";
    public static final String TASK_DATEDUETIME = "taskDateDueTime";

    // database name
    public static final String DATABASE_TABLE = "tasks";
    
    //Uri for Search Suggest
  	public static final Uri SUGGEST_URI = Uri.parse("content://" + CustomSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/" + DATABASE_TABLE);

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            The Context within which to work
     */
    public TaskDBAdapter(Context ctx) {
        super(ctx);
    }

    /**
     * Create a task. If the task is successfully created return the new rowId
     * for that task, otherwise return a -1 to indicate failure.
     * 
     * @param taskTitle
     *            title of task
     * @param taskDateDue
     *            date the task is due
     * @param taskDateDueTime
     *            time the task is due
     * @return rowId or -1 if failed
     */
    public long createTask(String taskTitle, String taskDateDue, String taskDateDueTime) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(TASK_TITLE, taskTitle);
        initialValues.put(TASK_DATEDUE, taskDateDue);
        initialValues.put(TASK_DATEDUETIME, taskDateDueTime);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the task with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteTask(long rowId) {
        return mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all tasks in the database
     * 
     * @return Cursor over all tasks
     */
    public Cursor getAllTasks() {
        return mDb.query(DATABASE_TABLE, new String[] { ROW_ID, TASK_TITLE, TASK_DATEDUE,
                TASK_DATEDUETIME }, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the task that matches the given rowId
     * 
     * @param rowId
     * @return Cursor positioned to matching task, if found
     * @throws SQLException
     *             if task could not be found/retrieved
     */
    public Cursor getTask(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, TASK_TITLE,
                TASK_DATEDUE, TASK_DATEDUETIME }, ROW_ID + "=" + rowId, null, null, null, null,
                null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Return a cursor that has tasks from the entire database whose title
     * partially matches the query.
     * @param query
     * @return cursor of tasks sorted by TASK_TITLE in ascending order
     * */
    public Cursor getTasksSortByName(String query) {
    	SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    	String[] columns = new String[] { ROW_ID, TASK_TITLE};
    	String selection = TASK_TITLE + " LIKE ?";
    	String[] selectionArgs = new String[] {"%" + query + "%"};
    	String orderBy = TASK_TITLE;
    	
		builder.setTables(DATABASE_TABLE);
		Cursor c = builder.query(mDb, columns, selection, selectionArgs, null, null, orderBy);

		return c;
    }

    /**
     * Update the task.
     * 
     * @param rowId
     * @param taskTitle
     *            title of task
     * @param taskDateDue
     *            date the task is due
     * @param taskDateDueTime
     *            time the task is due
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateTask(long rowId, String taskTitle, String taskDateDue,
            String taskDateDueTime) {
        ContentValues args = new ContentValues();

        args.put(TASK_TITLE, taskTitle);
        args.put(TASK_DATEDUE, taskDateDue);
        args.put(TASK_DATEDUETIME, taskDateDueTime);

        return mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) > 0;
    }

}