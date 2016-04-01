package napier.ac.uk.gradecalculator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Andrew on 25/03/2016.
 */
public class Module extends AppCompatActivity{

    private String module;
    TableLayout table;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module);


        Intent intent = getIntent();
        module = intent.getExtras().getString("m_name");

        setTitle(module);

        table = (TableLayout) findViewById(R.id.table1);

        BuildTable();


        /*final Cursor cursor = dbHelper.getModule(module);
        String [] columns = new String[] {
                DBHelper.RESULTS_COLUMN_MARK,
                DBHelper.RESULTS_COLUMN_PERCENTAGE,
                DBHelper.RESULTS_COLUMN_REFERENCE
        };
        int [] widgets = new int[] {
                R.id.mark,
                R.id.percentage,
                R.id.reference
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.module_display,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) Module.this.listView.getItemAtPosition(position);
                String module = itemCursor.getString(itemCursor.getColumnIndex(DBHelper.RESULTS_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), EditModule.class);
                intent.putExtra("m_name", module);
                startActivity(intent);
            }
        });*/
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_module, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.editModule:
                Intent intent = new Intent(getApplicationContext(), EditModule.class);
                intent.putExtra("m_name", module);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void BuildTable() {

        dbHelper = new DBHelper(this);

        Cursor c = dbHelper.getModule(module);

        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (5*scale + 0.5f);
            row.setBackgroundColor(Color.parseColor("#ECEFF1"));
            row.setPadding(0, dpAsPixels, 0, 0);
            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, 1));
                tv.setText(c.getString(j));

                row.addView(tv);

            }

            c.moveToNext();

            table.addView(row);

        }
    }
}
