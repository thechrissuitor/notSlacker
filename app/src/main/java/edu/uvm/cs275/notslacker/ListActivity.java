package edu.uvm.cs275.notslacker;

import androidx.fragment.app.Fragment;

/*
 * This is a controller class.
 */
public class ListActivity extends SingleFragmentActivity {
    // this method creates a new fragment
    @Override
    protected Fragment createFragment() {
        return new ListFragment();
    }
}
