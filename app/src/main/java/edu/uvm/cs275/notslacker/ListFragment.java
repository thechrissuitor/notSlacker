package edu.uvm.cs275.notslacker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * This is a controller class.
 */
public class ListFragment extends Fragment {

    private RecyclerView mList;
    private SlackAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    // create a RecyclerView.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_slack_list, container, false);

        mList = (RecyclerView) view.findViewById(R.id.slack_recycler_view);
        // RecycleView's require a LayoutManger to work.
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }


        updateUI();

        return view;
    }

    /* Add additional functionality to onResume().
     *  It now calls updateUI().*/
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    // this method saves the state of the subtitle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    /* This method connects the Adapter to the RecyclerView.
     * Sets up ListFragment's.*/
    private void updateUI(){
        SlackLab slackLab = SlackLab.get(getActivity());
        List<Slack> slacks = slackLab.getSlacks();

        if(mAdapter == null){
            mAdapter = new SlackAdapter(slacks);
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    // called when a menu is needed
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_slack_list, menu);

        // show or hide the "Show Subtitle" button
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    // let's ListFragment know that it needs to receive menu callbacks
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*
     * Let's the app to respond to selection
     * of the MenuItem by creating a new Slack, adding it to SlackLab, and then starting an instance of
     * PagerActivity to edit the new Slack.
     * Also, responds to the "Show Subtitle" button.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_slack:
                Slack slack = new Slack();
                SlackLab.get(getActivity()).addCrime(slack);
                Intent intent = PagerActivity.newIntent(getActivity(), slack.getID());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Create a ViewHolder to inflate and own the layout.
    private class SlackHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Slack mSlack;

        private TextView mTitleTextView;
        private TextView mDueDateTextView;
        private ImageView mCompletedImageView;

        public SlackHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_slack, parent, false));
            itemView.setOnClickListener(this); // make the row responsive to touch

            mTitleTextView = (TextView) itemView.findViewById(R.id.slack_title);
            mDueDateTextView = (TextView) itemView.findViewById(R.id.slack_due_date);
            mCompletedImageView = (ImageView) itemView.findViewById(R.id.slack_completed);
        }

        // Make each ViewHolder responsive to touch.
        @Override
        public void onClick(View view){
            // make a fragment
            Intent intent = PagerActivity.newIntent(getActivity(), mSlack.getID()); // pressing a list item in ListFragment starts an instance of PagerActivity
            startActivity(intent);
        }

        // this method is called each time a new Slack should be displayed.
        public void bind(Slack slack){
            mSlack = slack;
            mTitleTextView.setText(mSlack.getTitle());
            mDueDateTextView.setText(mSlack.getDueDate().toString());
            mCompletedImageView.setVisibility(slack.isCompleted() ? View.VISIBLE : View.GONE);
        }
    }

    // create an adapter
    private class SlackAdapter extends RecyclerView.Adapter<SlackHolder>{

        private List<Slack> mSlacks;

        // get the list of slacks
        public SlackAdapter(List<Slack> slacks){ mSlacks = slacks;}

        /* This method is called by the RecyclerView when it needs a new ViewHolder
         * to display an item with. In this method, a LayoutInflater is created and
         * used to construct a new SlackHolder.*/
        @Override
        public SlackHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new SlackHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SlackHolder holder, int position){
            Slack slack = mSlacks.get(position);
            holder.bind(slack);
        }

        @Override
        public int getItemCount(){
            return mSlacks.size();
        }
    }

    //  set the subtitle of the toolbar to display the number of crimes
    private void updateSubtitle() {
        SlackLab slackLab = SlackLab.get(getActivity());
        int slackCount = slackLab.getSlacks().size();
        String subtitle = getString(R.string.subtitle_format, slackCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


}