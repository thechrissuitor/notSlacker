package edu.uvm.cs275.notslacker;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

/*
 * This class creates and manages the ViewPager.
 * The ViewPager allows the user to swipe left and right to switch between items.
 */
public class PagerActivity extends AppCompatActivity {

    private static final String EXTRA_SLACK_ID = "csuitor.uvm.edu.slack_id";

    private ViewPager mViewPager;
    private List<Slack> mSlacks;

    public static Intent newInent(Context packageContext, UUID slackID) {
        Intent intent = new Intent(packageContext, PagerActivity.class);
        intent.putExtra(EXTRA_SLACK_ID, slackID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState); // on create
        setContentView(R.layout.activity_pager); // grab the xml file

        UUID slackID = (UUID) getIntent().getSerializableExtra(EXTRA_SLACK_ID);

        mViewPager = (ViewPager) findViewById(R.id.slack_view_pager);

        mSlacks = SlackLab.get(this).getCrimes(); // get the list of crimes
        FragmentManager fragmentManager = getSupportFragmentManager();

        /*  FragmentSatePagerAdaper is the agent managing the conversation with ViewPager.
         *  A FragmentManager is passed as a parameter, so the FragmentStatePagerAdapter
         *  can do its job. */
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            /*  This method fetches the Slack instance for the given position in the data set.
             *  Then, it uses that Slack's ID to create and return a properly configured SlackFragment.*/
            public Fragment getItem(int position) {
                Slack slack = mSlacks.get(position);
                return SlackFragment.newInstance(slack.getID()); // return an instance of a specific slack
            }

            @Override
            // Return the number of items in the array list.
            public int getCount() {
                return mSlacks.size();
            }
        });

        /*
            By default, the ViewPager shows the first item in its PagerAdapter. You can have it show the crime
            that was selected by setting the ViewPager’s current item to the index of the selected crime.
            At the end of PagerActivity.onCreate(Bundle), find the index of the crime to display by
            looping through and checking each slack’s ID. When you find the Slack instance whose mID matches
            the slackID in the intent extra, set the current item to the index of that Slack.
         */
        for(int i = 0; i < mSlacks.size(); i++){
            if(mSlacks.get(i).getID().equals(slackID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
