package edu.uvm.cs275.notslacker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*
 * This class is a singleton. Only one will exist at a time.
 * This class will be a list of Crime objects.
 */
public class SlackLab {
    private static SlackLab sSlackLab;

    private List<Slack> mSlacks;
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
        mSlacks = new ArrayList<>();
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
        return mSlacks;
    }

    // add a Slack
    public void addCrime(Slack s) {
        mSlacks.add(s);
    }

    /*
     * @param: UUID id = the id of the slack you are looking
     * @return: return the slack that has the id of the param
     */
    public Slack getSlack(UUID id){
        for(Slack crime : mSlacks){
            if(crime.getID().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
