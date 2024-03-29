package edu.uvm.cs275.notslacker;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.uvm.cs275.notslacker.SlackDBSchema.SlackTable;

// This method wraps a Cursor received from another place and add new methods on top of it
public class SlackCursorWrapper extends CursorWrapper {
    public SlackCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Slack getSlack() {
        String uuidString = getString(getColumnIndex(SlackTable.Cols.UUID));
        String title = getString(getColumnIndex(SlackTable.Cols.TITLE));
        String description = getString(getColumnIndex(SlackTable.Cols.DESCRIPTION));
        long dueDate = getLong(getColumnIndex(SlackTable.Cols.DUE_DATE));
        int isCompleted = getInt(getColumnIndex(SlackTable.Cols.COMPLETED));

        Slack slack = new Slack(UUID.fromString(uuidString));
        slack.setTitle(title);
        slack.setDescription(description);
        slack.setDueDate(new Date(dueDate));
        slack.setCompleted(isCompleted != 0);
        return slack;
    }
}
