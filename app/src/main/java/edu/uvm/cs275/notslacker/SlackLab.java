package edu.uvm.cs275.notslacker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.uvm.cs275.notslacker.SlackDBSchema.SlackTable;

/*
 * This class is a singleton. Only one will exist at a time.
 * This class will be a list of Crime objects.
 */
public class SlackLab {
    private static SlackLab sSlackLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static final String TAG = "SlackLab";

    /*
     * CONSTRUCTOR
     */
    private SlackLab(Context context){
        // database stuff
        mContext = context.getApplicationContext();
        mDatabase = new SlackBaseHelper(mContext).getWritableDatabase();
        // Populate the mSlacks list with 100 arbitrarily generated slacks.
    }

    /*
     * This method, get, will check if SlackLab already exist.
     * If SlackLab does exist, then it will return the existing SlackLab.
     * If SlackLab does not exist, it will make a new one.
     */
    public static SlackLab get(Context context){
        if(sSlackLab == null){
            sSlackLab = new SlackLab(context);
        }
        return sSlackLab;
    }

    // get the slacks from the database
    /*
    * "Database cursors are called cursors because they always have their finger on a particular place in a
    *  query. So to pull the data out of a cursor, you move it to the first element by calling moveToFirst(),
    *  and then read in row data. Each time you want to advance to a new row, you call moveToNext(), until
    *  finally isAfterLast() tells you that your pointer is off the end of the data set."
    */
    public List<Slack> getSlacks(){
        List<Slack> slacks = new ArrayList<>();
        SlackCursorWrapper cursor = querySlacks(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                slacks.add(cursor.getSlack());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return slacks;
    }

    // add a Slack
    public void addSlack(Slack s) {
        ContentValues values = getContentValues(s);
        mDatabase.insert(SlackTable.NAME, null, values);
    }

    // delete a slack from the database
    public void deleteSlack(Slack s) {
        mDatabase.delete(SlackTable.NAME, "uuid=?", new String[] { s.getID().toString() });
    }

    /*
     * @param: UUID id = the id of the slack you are looking
     * @return: return the slack that has the id of the param
     * Loops through the database to find the correct assignment.
     */
    public Slack getSlack(UUID id){
        SlackCursorWrapper cursor = querySlacks(
                SlackTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSlack();
        } finally {
            cursor.close();
        }
    }

    // update the table where slack UUID == database UUID
    public void updateSlack(Slack slack) {
        String uuidString = slack.getID().toString();
        ContentValues values = getContentValues(slack);
        mDatabase.update(SlackTable.NAME, values, SlackTable.Cols.UUID + " = ?", new String[] { uuidString });
    }

    // receiving a data query
    private SlackCursorWrapper querySlacks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SlackTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new SlackCursorWrapper(cursor);
    }

    // The values that will be inserted into the database.
    private static ContentValues getContentValues(Slack slack) {
        ContentValues values = new ContentValues();
        values.put(SlackTable.Cols.UUID, slack.getID().toString());
        values.put(SlackTable.Cols.TITLE, slack.getTitle());
        values.put(SlackTable.Cols.DESCRIPTION, slack.getDescription());
        values.put(SlackTable.Cols.DUE_DATE, slack.getDueDate().getTime());
        values.put(SlackTable.Cols.COMPLETED, slack.isCompleted() ? 1 : 0);

        return values;
    }
}
