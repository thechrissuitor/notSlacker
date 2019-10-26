package edu.uvm.cs275.notslacker;
import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "csuitor.uvm.edu.date";

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;

    /*  In order to pass data into a DatePickerFragment, it is necessary to store the data into the
     *  DatePickerFragment's arguments bundle, first.
     *  Within newInstance(Date), we stash the date into the bundle, so
     *  the DatePickerFragment can access it.*/
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        /* In order to get the integers I need, I must create a Calendar object to retrieve them from. */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Inflate the view. Later, .setView(v) sets the view on the dialog
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        // build an AlertDialog with a title and one OK button
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        // this .OnClickListener() retrieves the selected date and calls sendResult(...)
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date); /* When the user presses the positive button in the
                                                                         dialog, you want to retrieve the date from the DatePicker
                                                                         and send the result back to CrimeFragment. */
                            }
                        })
                .create();
    }

    /* This method creates an intent, puts the date on it as an extra, and then calls
     *  CrimFragment.onActivityResult(...)*/
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}