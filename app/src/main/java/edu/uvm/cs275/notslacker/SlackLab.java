package edu.uvm.cs275.notslacker;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * This class is a singleton. Only one will exist at a time.
 * This class will be a list of Crime objects.
 */
public class SlackLab {
    private static SlackLab sSlackLab;

    private List<Slack> mSlacks;

    private static final String TAG = "SlackLab";

    /*
     * CONSTRUCTOR
     * Populate the mSlacks list with 100 arbitrarily generated slacks.
     */
    private SlackLab(Context context){
        mSlacks = new ArrayList<>();
        for (int i=0; i<100; i++){
            Slack slack = new Slack();
            slack.setTitle("Crime #" + i);
            slack.setCompleted(i % 2 == 0); // Every other slack will be completed
            mSlacks.add(slack);
        }
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
