package napier.ac.uk.gradecalculator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class EditModule extends AppCompatActivity{

    DBHelper dbHelper;
    private String module;
    private int total;
    private int rowcount;

    Button save;
    EditText mark;
    EditText percentage;
    EditText reference;
    EditText test;
    TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_module);

        Intent intent = getIntent();
        module = intent.getExtras().getString("m_name");
        setTitle(module);
        dbHelper = new DBHelper(this);
        rowcount = 0;
        total = 0;

        table = (TableLayout) findViewById(R.id.table);
        save = (Button) findViewById(R.id.save);
        /*mark = (EditText) findViewById(R.id.addMark1);
        percentage = (EditText) findViewById(R.id.addPercentage1);
        reference = (EditText) findViewById(R.id.addReference1);*/

        BuildTable();

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*int markint = Integer.parseInt(mark.getText().toString());
                int percentageint = Integer.parseInt(percentage.getText().toString());
                String referencestring = reference.getText().toString();

                dbHelper.addResult(module, markint, percentageint, referencestring);*/
                for (int j = 1; j < total; j=j+3) {
                    mark = (EditText) findViewById(j);
                    percentage = (EditText) findViewById(j+1);
                    reference = (EditText) findViewById(j+2);

                    int markint = Integer.parseInt(mark.getText().toString());
                    int percentageint = Integer.parseInt(percentage.getText().toString());
                    String referencestring = reference.getText().toString();

                    dbHelper.addResult(module, markint, percentageint, referencestring);
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_module, menu);
        return true;
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
            int dpAsPixels = (int) (5 * scale + 0.5f);
            row.setBackgroundColor(Color.parseColor("#ECEFF1"));
            row.setPadding(0, dpAsPixels, 0, 0);
            // inner for loop
            for (int j = 0; j < cols; j++) {

                EditText tv = new EditText(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1));
                tv.setText(c.getString(j));
                total++;
                tv.setId(total);
                row.addView(tv);

            }

            c.moveToNext();

            table.addView(row);
            rowcount++;

        }

    }

    private void addRow(){
        TableRow row = new TableRow(this);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (5*scale + 0.5f);
        row.setBackgroundColor(Color.parseColor("#ECEFF1"));
        row.setPadding(0, dpAsPixels, 0, 0);
        // inner for l
        for (int j = 0; j < 3; j++) {
            EditText tv = new EditText(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            total++;
            tv.setId(total);
            row.addView(tv);


        }


        table.addView(row);
        rowcount++;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.addText:
            addRow();
        }

        return super.onOptionsItemSelected(item);
    }
}