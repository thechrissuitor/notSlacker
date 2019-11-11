package edu.uvm.cs275.notslacker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.uvm.cs275.notslacker.SlackDBSchema.SlackTable;

public class SlackBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "slackBase.db";
    public SlackBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SlackTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SlackTable.Cols.UUID + ", " +
                SlackTable.Cols.TITLE + ", " +
                SlackTable.Cols.DESCRIPTION + ", " +
                SlackTable.Cols.DUE_DATE + ", " +
                SlackTable.Cols.COMPLETED + ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
