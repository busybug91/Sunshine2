package com.example.nitin.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nitin on 8/12/14.
 */
public class ForecastFragment extends Fragment {
    String TAG=FetchWeatherTask.class.getSimpleName();
    String[] result=null;
    ArrayAdapter<String> adapter=null;
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //Telling android that our activity will have an action bar
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecastfragment, menu); // here we inflate the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id= item.getItemId();
      return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG,"Update weather called");
        updateWeather();
    }

    public void updateWeather()
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location=sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
        fetchWeatherTask.execute(location);
        return;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
      //  String[] s={"asdf","asdf"};

        List<String> weatherList= new ArrayList<String>();
//context, the row layout, TextView id int the row layout as ArrayAdapter will print a toString() of the object in the text view, list of objects
        adapter= new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textView,weatherList);
        ListView listView=(ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),adapter.getItem(position),Toast.LENGTH_LONG).show();
                Intent intent= new Intent(getActivity(),DetailedActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT,adapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<String,Void,String []> {


        @Override
        protected void onPostExecute(String[] strings) {
            if(strings!=null) {
               adapter.clear();
              adapter.addAll(Arrays.asList(strings));
                adapter.setNotifyOnChange(true);
            }

        }

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader bufferedReader = null;
            URL url = null;
            String jsonResponse = null;

            //values;
            int days=7;
            String units="metric";
            String format="json";
           final String BaseUrl="http://api.openweathermap.org/data/2.5/forecast/daily";

            //parameter names
            String query_param="q";
            String mode_param="mode";
            String cnt_param="cnt";
            try {

                Uri builtUri= Uri.parse(BaseUrl).buildUpon().appendQueryParameter(query_param, params[0]).appendQueryParameter(mode_param,format)
                        .appendQueryParameter(cnt_param, Integer.toString(days)).build();
                url= new URL(builtUri.toString());

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream inputStream = conn.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null)
                {
                    return null;
                }
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    buffer.append(line + "\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                if (buffer.length() > 0) {
                    jsonResponse = buffer.toString();
                    //Parsing the jsonResponse

                }

            } catch (Exception ex) {

                Log.e(TAG, "Error", ex);
            } finally {

                if (conn != null) {
                    conn.disconnect();
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Exception e) {
                            Log.e(TAG, "Unable to close bufferReader", e);
                        }
                    }
                }

            }
            try{

                WeatherDataParser parser= new WeatherDataParser(getActivity());
                result=parser.getWeatherDataFromJson(jsonResponse, days);
                return result;
            }
            catch(Exception e)
            {
                Log.e(TAG,"Error parsing weather data",e);
            }
            return null;
        }
    }

}