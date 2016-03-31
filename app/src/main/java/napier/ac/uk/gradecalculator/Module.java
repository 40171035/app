package napier.ac.uk.gradecalculator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Andrew on 25/03/2016.
 */
public class Module extends AppCompatActivity{

    private String module;
    private ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module);


        Intent intent = getIntent();
        module = intent.getExtras().getString("m_name");

        setTitle(module);

        dbHelper = new DBHelper(this);

        final Cursor cursor = dbHelper.getModule(module);
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
        });
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
}
