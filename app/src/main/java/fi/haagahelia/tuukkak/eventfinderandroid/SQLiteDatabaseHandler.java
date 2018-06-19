package fi.haagahelia.tuukkak.eventfinderandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import fi.haagahelia.tuukkak.eventfinderandroid.Event;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EventDB";
    private static final String TABLE_NAME = "Events";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date";
    private static final String KEY_CITY = "city";
    private static final String[] COLUMNS = { KEY_ID, KEY_TITLE, KEY_VENUE, KEY_ADDRESS, KEY_DATE, KEY_CITY };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Events ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, "
        + "venue TEXT, " + "address TEXT, " + "date TEXT, " + "city TEXT )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(event.getId()) });
        db.close();
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event();
        event.setId(Integer.parseInt(cursor.getString(0)));
        event.setTitle(cursor.getString(1));
        event.setVenue(cursor.getString(2));
        event.setAddress(cursor.getString(3));
        event.setDate(cursor.getString(4));
        event.setCity(cursor.getString(5));

        return event;
    }

    public List<Event> allEvents() {
        List<Event> events = new LinkedList<Event>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Event event = null;

        if (cursor.moveToFirst()) {
            do {
                event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setTitle(cursor.getString(1));
                event.setVenue(cursor.getString(2));
                event.setAddress(cursor.getString(3));
                event.setDate(cursor.getString(4));
                event.setCity(cursor.getString(5));
                events.add(event);
            } while (cursor.moveToNext());
        }
        return events;
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_VENUE, event.getVenue());
        values.put(KEY_ADDRESS, event.getAddress());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_CITY, event.getCity());

        db.insert(TABLE_NAME,null, values);
        db.close();
    }
}
