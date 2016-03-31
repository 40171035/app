package napier.ac.uk.gradecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.text.InputType;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity {

    private String m_name;
    private ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        final Cursor cursor = dbHelper.getResults();
        String [] columns = new String[] {
                DBHelper.RESULTS_COLUMN_ID
        };
        int [] widgets = new int[] {
                R.id.module
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.module_list,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) MainActivity.this.listView.getItemAtPosition(position);
                String module = itemCursor.getString(itemCursor.getColumnIndex(DBHelper.RESULTS_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), Module.class);
                intent.putExtra("m_name", module);
                startActivity(intent);
            }
        });
        
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    private void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_name = input.getText().toString();
                dbHelper.addSingleResult(m_name);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.addModule:
                alert();

        }

        return super.onOptionsItemSelected(item);
    }
}
