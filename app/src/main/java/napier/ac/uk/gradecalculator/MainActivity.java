package napier.ac.uk.gradecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String m_name;
    private ListView listView;
    DBHelper dbHelper;
    private Cursor mCursor;
    private SimpleCursorAdapter mCursorAd;
    TextView helptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helptext = (TextView) findViewById(R.id.help);
        listView = (ListView) findViewById(R.id.listView1);


        listView.setEmptyView(helptext);

        populate();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    private void refresh() {
        mCursor = dbHelper.getResults();
        mCursorAd.swapCursor(mCursor);
    }

    private void populate() {
        dbHelper = new DBHelper(this);

        mCursor = dbHelper.getResults();
        String[] columns = new String[]{
                DBHelper.RESULTS_COLUMN_ID
        };
        int[] widgets = new int[]{
                R.id.module
        };

        mCursorAd = new SimpleCursorAdapter(this, R.layout.module_list,
                mCursor, columns, widgets, 0);

        listView.setAdapter(mCursorAd);
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
        dbHelper.close();
    }

    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alertTitle);

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.alertAdd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_name = input.getText().toString();
                dbHelper.addSingleResult(m_name);
                refresh();
            }
        });
        builder.setNegativeButton(R.string.alertCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addModule:
                alert();
        }
        return super.onOptionsItemSelected(item);
    }
}