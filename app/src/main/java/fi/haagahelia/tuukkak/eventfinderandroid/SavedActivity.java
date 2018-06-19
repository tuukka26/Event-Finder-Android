package fi.haagahelia.tuukkak.eventfinderandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

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
        setContentView(R.layout.activity_saved);

        db = new SQLiteDatabaseHandler(this);

        List<Event> events = db.allEvents();

        if (events != null) {
            String[] eventItems = new String[events.size()];

            for (int i = 0; i < events.size(); i++) {
                eventItems[i] = events.get(i).toString();
            }

            ListView list = findViewById(R.id.lvList);
            list.setAdapter(new ArrayAdapter<>(this,
                    R.layout.custom_text, eventItems));
        }
    }
}
