package SQL_db_files;

/**
 * Created by jsaluja on 1/2/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TableDBHelperClass extends SQLiteOpenHelper implements BaseColumns {
    private static TableDBHelperClass sInstance;
    // Database Info
    public static final String LOGTAG = "ACCESS_ITEMS";
    private static final String DATABASE_NAME = "ItemsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_ITEMS = "ToDoItems";
    public static final String KEY_ITEMS_ID = "Item_number";
    public static final String KEY_ITEMS_TEXT = "Item_description";
    public static final String KEY_ITEMS_DUE_DATE = "Item_due_date_value";
    public static final String KEY_ITEMS_PRIORITY = "Item_priority_value";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ITEMS +
            "(" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_ITEMS_ID + " INTEGER, " +
                    KEY_ITEMS_DUE_DATE + " TEXT, " +
                    KEY_ITEMS_PRIORITY + " INTEGER, " +
                    KEY_ITEMS_TEXT + " TEXT);";

    public static synchronized TableDBHelperClass getInstance(Context context) {
        // Using application context to avoid any leaks at Activity's context.
        if (sInstance == null) {
            sInstance = new TableDBHelperClass(context.getApplicationContext());
        }
        return sInstance;
    }

    private TableDBHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //I can also call upgrade to add new column. Try this!!
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);
        onCreate(db);

    }
}
