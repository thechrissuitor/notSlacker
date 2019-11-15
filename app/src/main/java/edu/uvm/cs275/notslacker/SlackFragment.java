package edu.uvm.cs275.notslacker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class SlackFragment extends Fragment {
    private Slack mSlack;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private Button mDueDateButton;
    private CheckBox mCompletedCheckBox;
    private static final String ARG_SLACK_ID = "slack_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID slackID = (UUID) getArguments().getSerializable(ARG_SLACK_ID);
        mSlack = SlackLab.get(getActivity()).getSlack(slackID);
    }

    @Override
    public void onPause() {
        super.onPause();
        SlackLab.get(getActivity()).updateSlack(mSlack); // update the database
    }

    // called when a menu is needed
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail_view_menu, menu);
    }

    /*
     * Let's the app to respond to selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_slack:
                SlackLab.get(getActivity()).deleteSlack(mSlack);
                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
         * @param: R.layout.fragment_slack = find the fragment with the corresponding id
         * @param: container = view's parent
         * @param: false = tells the layout inflater whether to add the inflated view to the view's parent.
         *         "false" was passed because the view will be added to the activity's code.
         * The line of code below inflates the fragment's view.
         */
        View v = inflater.inflate(R.layout.fragment_slack, container, false);

        mTitleField = (EditText) v.findViewById(R.id.slack_title); // find an EditText with the corresponding id
        mTitleField.setText(mSlack.getTitle()); // fill the crime's title with what it's been set as
        // make a listener
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            //  call toString() on the CharSequence that is the user’s input. Set the returned string as the Crime's title
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSlack.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mDescriptionField = (EditText) v.findViewById(R.id.slack_description); // find an EditText with the corresponding id
        mDescriptionField.setText(mSlack.getDescription()); // fill the crime's title with what it's been set as
        // make a listener
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            //  call toString() on the CharSequence that is the user’s input. Set the returned string as the Crime's title
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSlack.setDescription(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mCompletedCheckBox = (CheckBox)v.findViewById(R.id.slack_completed); // find a checkbox with the id "slack_completed"
        mCompletedCheckBox.setChecked(mSlack.isCompleted()); // set it as completed/not-completed if it's already been set
        // set a listener for checkboxes
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                mSlack.setCompleted(isChecked); // update the Crime class boolean, mSolved
            }
        });

        mDueDateButton = (Button) v.findViewById(R.id.slack_due_date); // find a button with the correct id
        updateDate();
        // show a DatePickerFragment when the date button is pressed
        mDueDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mSlack.getDueDate());
                dialog.setTargetFragment(SlackFragment.this, REQUEST_DATE); // CrimeFragment is now the target fragment of the DatePickerFragment
                dialog.show(manager, DIALOG_DATE);
            }
        });

        return v;
    }

    // This method retrieves the extra, sets the date on the Slack, and refreshes the text of the date button.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mSlack.setDueDate(date);
            updateDate();
        }
    }

    /* This method creates a bundle to save states for a fragment. */
    public static SlackFragment newInstance(UUID slackID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SLACK_ID, slackID);

        SlackFragment fragment = new SlackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // this method updates the text on the date button
    private void updateDate() {
        mDueDateButton.setText(mSlack.getDueDate().toString());
    }
}
