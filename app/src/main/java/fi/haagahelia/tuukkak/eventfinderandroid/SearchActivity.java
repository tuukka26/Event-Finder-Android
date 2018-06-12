package fi.haagahelia.tuukkak.eventfinderandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ListView tvResults;
    EditText etLocation;
    EditText etKeyword;

    ArrayList<HashMap<String, String>> eventsList;

    String appKey = "APP_KEY";
    String date = "All";


    // Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Define menu options' actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                Intent intentAdd = new Intent(this, MainActivity.class);
                startActivityForResult(intentAdd, 0);
                return true;
            case R.id.menu_item_saved:
                Intent intentInstructions = new Intent(this, SavedActivity.class);
                startActivityForResult(intentInstructions, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progressBar);
        tvResults =  findViewById(R.id.tvResults);
        etLocation = findViewById(R.id.etLocation);
        etKeyword = findViewById(R.id.etKeyword);
        eventsList = new ArrayList<>();

        // Set search button's onClickListener
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchEvents().execute();
            }
        });

    }

    //Fetch and parse the JSON response from Eventful API
    class FetchEvents extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... urls) {
            String location = etLocation.getText().toString();
            String keyword = etKeyword.getText().toString();
            eventsList.clear();

            try{
                URL url = new URL("http://api.eventful.com/json/events/search?app_key=" + appKey + "&keywords=" + keyword + "&where=" + location + "&date=" + date + "&page_size=20&sort_order=popularity&change_multi_day_start=true");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("/n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                showToast(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response == null) {
                showToast("An error occurred, please try again");
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);

            try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject eventsObj = object.getJSONObject("events");
                JSONArray eventsArr = eventsObj.getJSONArray("event");

               for (int i = 0; i < eventsArr.length(); i++) {
                    try {
                        JSONObject newObj = eventsArr.getJSONObject(i);
                        String title = newObj.getString("title");
                        String venue = newObj.getString("venue_name");
                        String start_time = newObj.getString("start_time");
                        String venue_address = newObj.getString("venue_address");
                        String city = newObj.getString("city_name");

                        HashMap<String, String> event = new HashMap<>();

                        event.put("title", title + " @ " + venue);
                        event.put("start_time", "Date: " + start_time);
                        event.put("venue_address", "Venue address: " + venue_address + ", " + city);

                        eventsList.add(event);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast(e.getMessage());
                    }
                }

                ListAdapter adapter = new SimpleAdapter(SearchActivity.this, eventsList, R.layout.list_item, new String[] { "title", "start_time", "venue_address" },
                        new int[] { R.id.title, R.id.start_time, R.id.venue_address });
                        tvResults.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        }

        //Change date depending on the radio button selection
        public void onRadioButtonClicked (View view) {

            boolean checked = ((RadioButton) view).isChecked();

            switch(view.getId()) {
                case R.id.rbToday:
                    if (checked)
                        date = "Today";
                    break;
                case R.id.rbThisWeek:
                    if (checked)
                        date = "Next week";
                    break;
                case R.id.rbNextWeek:
                    if (checked)
                        date = "This week";
                    break;
            }

        }

    public void showToast(String text) {
        int time = Toast.LENGTH_LONG;
        Context context = getApplicationContext() ;
        Toast toast = Toast.makeText(context, text, time);
        toast.show();
    }
}



  /*  public void showToast(String text) {
        int time = Toast.LENGTH_LONG;
        Context context = getApplicationContext() ;
        Toast toast = Toast.makeText(context, text, time);
        toast.show();
    } */

