package com.example.nitin.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent= new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.view_on_map)
        {
            viewPrefLocOnMap();

        }
        return super.onOptionsItemSelected(item);
    }


    private void viewPrefLocOnMap()
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String location=sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

        Uri geoUri= Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",location).build();

        Intent intent= new Intent(Intent.ACTION_VIEW, geoUri);
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(intent);
        }
        else{
          Toast.makeText(getApplicationContext(),"Map application not found on device",Toast.LENGTH_LONG).show();
        }

    }
}