package codepathtodoapp.myandroid.com.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import SQL_db_files.TableDBHelperClass;

/**
 * Created by jsaluja on 1/2/2017.
 */
public class CustomListViewAdapter extends CursorAdapter {
    private static LayoutInflater inflater = null;
    public CustomListViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        view = inflater.inflate(R.layout.list_row, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView DueDate = (TextView)view.findViewById(R.id.textViewDueDate);
        TextView Priority = (TextView)view.findViewById(R.id.textViewPriority);
        String Priority_string;

        int db_priority_value = cursor.getInt(cursor.getColumnIndex(TableDBHelperClass.KEY_ITEMS_PRIORITY));

        if(db_priority_value == 3) {
            Priority_string = "High";
        } else if(db_priority_value == 2) {
            Priority_string = "Medium";
        } else {
            Priority_string = "Low";
        }

        title.setText(cursor.getString(cursor.getColumnIndex(TableDBHelperClass.KEY_ITEMS_TEXT)));
        DueDate.setText(cursor.getString(cursor.getColumnIndex(TableDBHelperClass.KEY_ITEMS_DUE_DATE)));
        Priority.setText(Priority_string);
    }
}
