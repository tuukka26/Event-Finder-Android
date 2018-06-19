package fi.haagahelia.tuukkak.eventfinderandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    ListView lvList;
    TextView noData;

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
            case R.id.menu_item_search:
                Intent intentInstructions = new Intent(this, SearchActivity.class);
                startActivityForResult(intentInstructions, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        db = new SQLiteDatabaseHandler(this);
        noData = findViewById(android.R.id.empty);

        final List<Event> events = db.allEvents();

        if (events != null) {
            String[] eventItems = new String[events.size()];

            for (int i = 0; i < events.size(); i++) {
                eventItems[i] = events.get(i).toString();
            }

            noData.setVisibility(View.GONE);
            lvList = findViewById(R.id.lvList);
            lvList.setAdapter(new ArrayAdapter<>(this,
                    R.layout.custom_text, eventItems));
        }

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SavedActivity.this);

                alertDialogBuilder.setTitle(R.string.warning);

                alertDialogBuilder
                        .setMessage(R.string.warning_msg)
                        .setCancelable(false)
                        .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteOne(events.get(position));
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                // In case no events have been saved, a message will be shown
                lvList.setEmptyView(noData);

              //  db.deleteOne(events.get(position));
              //  startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lvList.setEmptyView(noData);
    }
}
