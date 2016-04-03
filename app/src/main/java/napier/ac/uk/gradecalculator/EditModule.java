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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class EditModule extends AppCompatActivity {

    DBHelper dbHelper;
    private String module;
    private int total;
    private int rowcount;
    private int totalpercentage;
    private boolean valid;

    Button save;
    Button delete;
    EditText mark;
    EditText percentage;
    EditText reference;
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
        delete = (Button) findViewById(R.id.delete);

        BuildTable();
        InitialRowAdd();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Validate()) {
                    dbHelper.deleteResults(module);
                    for (int j = 1; j < total; j = j + 3) {
                        mark = (EditText) findViewById(j);
                        percentage = (EditText) findViewById(j + 1);
                        reference = (EditText) findViewById(j + 2);

                        int markint = Integer.parseInt(mark.getText().toString());
                        int percentageint = Integer.parseInt(percentage.getText().toString());
                        String referencestring = reference.getText().toString();

                        dbHelper.addResult(module, markint, percentageint, referencestring);

                    }
                    dbHelper.Refine(module);
                    Module.instance.recreate();
                    finish();
                } else {
                    return;
                }

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_module, menu);
        return true;
    }

    private boolean Validate() {
        valid = true;
        totalpercentage = 0;
        for (int j = 1; j < total; j = j + 3) {
            mark = (EditText) findViewById(j);


            if (mark.getText().toString().matches("")) {
                mark.setText("0");

            }
        }
        for (int j = 1; j < total; j = j + 3) {
            percentage = (EditText) findViewById(j + 1);
            if (percentage.getText().toString().matches("")) {
                percentage.setText("0");

            }
        }
        for (int j = 1; j < total; j = j + 3) {
            mark = (EditText) findViewById(j);

            int markint = Integer.parseInt(mark.getText().toString());
            if (0 > markint || markint > 100) {
                mark.setError("Please enter a value between 0 and 100");
                valid = false;
            }
        }
        for (int j = 1; j < total; j = j + 3) {
            percentage = (EditText) findViewById(j + 1);
            int percentageint = Integer.parseInt(percentage.getText().toString());
            totalpercentage = totalpercentage + percentageint;
            if (percentageint < 0 || percentageint > 100) {
                percentage.setError("Please enter a value between 1 and 100");
                valid = false;
            }
        }
        for (int j = 1; j < total; j = j + 3) {
            if (totalpercentage > 100) {
                percentage.setError("Total Percentage cannot be over 100!");
                valid = false;
            }
        }
        return valid;
    }

    private void InitialRowAdd() {
        if (rowcount == 0) {
            addRow();
        }
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
            if ((rowcount % 2) == 0) {
                row.setBackgroundResource(R.color.tableRow);
            } else {
                row.setBackgroundResource(R.color.tableRowAlt);
            }
            row.setPadding(0, dpAsPixels, 0, 0);
            // inner for loop
            for (int j = 0; j < cols; j++) {

                EditText tv = new EditText(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1));
                tv.setText(c.getString(j));
                if (j == 0) {
                    tv.setHint(R.string.markHint);
                    tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (j == 1) {
                    tv.setHint(R.string.percentageHint);
                    tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                total++;
                tv.setId(total);
                row.addView(tv);

            }

            c.moveToNext();

            table.addView(row);
            rowcount++;

        }
        dbHelper.close();

    }

    private void addRow() {
        TableRow row = new TableRow(this);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (5 * scale + 0.5f);
        if ((rowcount % 2) == 0) {
            row.setBackgroundResource(R.color.tableRow);
        } else {
            row.setBackgroundResource(R.color.tableRowAlt);
        }
        row.setPadding(0, dpAsPixels, 0, 0);
        // inner for l
        for (int j = 0; j < 3; j++) {
            EditText tv = new EditText(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
            if (j == 0) {
                tv.setHint(R.string.markHint);
                tv.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (j == 1) {
                tv.setHint(R.string.percentageHint);
                tv.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            total++;
            tv.setId(total);
            row.addView(tv);
        }


        table.addView(row);
        rowcount++;
    }

    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this module?");

// Set up the buttons
        builder.setPositiveButton(R.string.alertYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteResults(module);
                finish();
                Module.instance.finish();
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

        //handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.addText:
                addRow();
        }

        return super.onOptionsItemSelected(item);
    }
}