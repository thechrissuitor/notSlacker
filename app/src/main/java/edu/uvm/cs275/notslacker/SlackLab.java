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

    // Return the mSlacks list
    public List<Slack> getSlacks(){
        return new ArrayList<>();
    }

    // add a Slack
    public void addSlack(Slack s) {
        ContentValues values = getContentValues(s);
        mDatabase.insert(SlackTable.NAME, null, values);
    }

    /*
     * @param: UUID id = the id of the slack you are looking
     * @return: return the slack that has the id of the param
     */
    public Slack getSlack(UUID id){
        return null;
    }

    // update the table where slack UUID == database UUID
    public void updateSlack(Slack slack) {
        String uuidString = slack.getID().toString();
        ContentValues values = getContentValues(slack);
        mDatabase.update(SlackTable.NAME, values, SlackTable.Cols.UUID + " = ?", new String[] { uuidString });
    }

    // receiving a data query
    private Cursor queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SlackTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return cursor;
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
