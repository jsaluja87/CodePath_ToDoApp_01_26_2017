package codepathtodoapp.myandroid.com.todoapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import SQL_db_files.TableDBHelperClass;

public class MainActivity extends AppCompatActivity implements EditDialogue.EditDialogueListener {

    private String ClickedItem;
    private int ClickedItemPriority;
    private int ClickedItemPosition;
    Cursor todoItems;
    CustomListViewAdapter aToDoAdapter;
    private EditText EtInput;
    private ListView LsView;
    SQLiteDatabase DataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TableDBHelperClass dbHelper = TableDBHelperClass.getInstance(this);// TableDBHelperClass(this);
        DataBase = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PopulateArrayItems();
        EtInput = (EditText) findViewById(R.id.EditId);
        LsView = (ListView) findViewById(R.id.listViewId);

        EtInput.requestFocus();
        LsView.setAdapter(aToDoAdapter);
        LsView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClickedItem = todoItems.getString(todoItems.getColumnIndex(TableDBHelperClass.KEY_ITEMS_TEXT));
                ClickedItemPriority = todoItems.getInt(todoItems.getColumnIndex(TableDBHelperClass.KEY_ITEMS_PRIORITY));

                String PriorityString = "None";
                ClickedItemPosition = todoItems.getInt(todoItems.getColumnIndex(TableDBHelperClass._ID));
                if(ClickedItemPriority == 3) {
                    PriorityString = "High";
                } else if(ClickedItemPriority == 2) {
                    PriorityString = "Medium";
                } else {
                    PriorityString = "Low";
                }

                //Calling different fragment
                FragmentManager fm = getSupportFragmentManager();
                EditDialogue editNameDialogFragment = EditDialogue.newInstance(ClickedItem, PriorityString);
                editNameDialogFragment.show(fm, "fragment_edit_name");

            }
        });
        LsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TableDBHelperClass.LOGTAG, "Deleting Position " + position);
                DataBase.beginTransaction();
                try {
                    DataBase.delete(TableDBHelperClass.TABLE_ITEMS,
                            TableDBHelperClass._ID + " = ?",
                            new String[]{String.valueOf(todoItems.getInt(todoItems.getColumnIndex(TableDBHelperClass._ID)))});
                    DataBase.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.d(TableDBHelperClass.LOGTAG, "Error while trying to update post to database");
                } finally {
                    DataBase.endTransaction();
                }
                readCursorItems();
                aToDoAdapter.swapCursor(todoItems);// notifyDataSetChanged();
                return true;
            }
        });

    }


    public void PopulateArrayItems() {
        readCursorItems();
        // aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        aToDoAdapter = new CustomListViewAdapter(this, todoItems, 0);
    }

    public void readCursorItems() {
        DataBase.beginTransaction();
        try {
            todoItems = DataBase.query(TableDBHelperClass.TABLE_ITEMS, null, null, null, null, null, TableDBHelperClass.KEY_ITEMS_PRIORITY+" DESC");
            DataBase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TableDBHelperClass.LOGTAG, "Error while trying to read the database");
        } finally {
            DataBase.endTransaction();
        }
    }

    public void OnAddItem(View view) {

        Calendar C = Calendar.getInstance();
        int CurrentMonth = 1 + C.get(Calendar.MONTH);
        String CurrentDate = C.get(Calendar.YEAR) + "/" + CurrentMonth + "/" + C.get(Calendar.DAY_OF_MONTH);
        int PriorityValue = 3; //Keeping default priority to "High"

        DataBase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TableDBHelperClass.KEY_ITEMS_TEXT, EtInput.getText().toString());
            values.put(TableDBHelperClass.KEY_ITEMS_DUE_DATE, CurrentDate);
            values.put(TableDBHelperClass.KEY_ITEMS_PRIORITY, PriorityValue);
            DataBase.insertOrThrow(TableDBHelperClass.TABLE_ITEMS, null, values);
            DataBase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TableDBHelperClass.LOGTAG, "Error while trying to add post to database");
        } finally {
            DataBase.endTransaction();
        }
        readCursorItems();
        aToDoAdapter.swapCursor(todoItems);
        EtInput.setText("");
        EtInput.setHint("Add a new Item");

    }

    //Function to get the result from EditDialogue fragment
    @Override
    public void onFinishEditDialog(String inputText, String inputDate, String inputPriority) {
        int PriorityValue = 1;
        if(Objects.equals(inputPriority, "High")) {
            PriorityValue = 3;
        } else if(Objects.equals(inputPriority, "Medium")) {
            PriorityValue = 2;
        } else {
            PriorityValue = 1;
        }
        DataBase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TableDBHelperClass.KEY_ITEMS_TEXT, inputText);
            values.put(TableDBHelperClass.KEY_ITEMS_DUE_DATE, inputDate);
            values.put(TableDBHelperClass.KEY_ITEMS_PRIORITY, PriorityValue);
            DataBase.update(TableDBHelperClass.TABLE_ITEMS, values,
                    TableDBHelperClass._ID + " = ?",
                    new String[]{String.valueOf(ClickedItemPosition)});
            DataBase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TableDBHelperClass.LOGTAG, "Error while trying to update post to database");
        } finally {
            DataBase.endTransaction();
        }
        readCursorItems();
        aToDoAdapter.swapCursor(todoItems);// notifyDataSetChanged();

    }

}