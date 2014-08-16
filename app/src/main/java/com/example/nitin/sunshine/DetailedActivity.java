package com.example.nitin.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import android.widget.ShareActionProvider;


public class DetailedActivity extends ActionBarActivity {

    private static String details;
    private static String appName=" #Sunshine #Androidapp";

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        // to display up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailed, menu);

        //find the menu item
        MenuItem menuItem= menu.findItem(R.id.action_share);

        // associate a shareActionProvider to it
        ShareActionProvider shareActionProvider= (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
        if(shareActionProvider!=null)
        {
            //attach the intent to this share action provider.
            shareActionProvider.setShareIntent(createShareIntent());
        }
        else
        {
            Log.e(DetailedActivity.class.getSimpleName(),"Share Action Provider is null");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
        public Intent createShareIntent()
        {
            Intent intent= new Intent(Intent.ACTION_SEND);
            // this flag brings us back to our application when data is sent by the intended app.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,details+appName);
            return intent;
        }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
            Intent intent= getActivity().getIntent();
            details=intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView textView=(TextView)rootView.findViewById(R.id.detailed_text);
            textView.setText(details);
            return rootView;
        }

    }
}
