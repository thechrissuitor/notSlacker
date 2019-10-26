package edu.uvm.cs275.notslacker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    // create a RecyclerView.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_slack_list, container, false);

        mList = (RecyclerView) view.findViewById(R.id.slack_recycler_view);
        // RecycleView's require a LayoutManger to work.
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    /* This method connects the Adapter to the RecyclerView.
     * Sets up ListFragment's.*/
    private void updateUI(){
        SlackLab crimeLab = SlackLab.get(getActivity());
        List<Slack> slacks = crimeLab.getSlacks();

        if(mAdapter == null){
            mAdapter = new SlackAdapter(slacks);
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
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
            Intent intent = PagerActivity.newInent(getActivity(), mSlack.getID()); // pressing a list item in CrimeListFragment starts an instance of CrimePagerActivity
            startActivity(intent);
        }

        // this method is called each time a new Crime should be displayed.
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

}