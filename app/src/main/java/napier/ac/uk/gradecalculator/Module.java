package napier.ac.uk.gradecalculator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class Module extends AppCompatActivity {

    private String module;
    private boolean valid;
    private float temp;
    private float fin;
    private int percentagetotal;

    TableLayout table;
    DBHelper dbHelper;

    TextView average;
    TextView average2;
    TextView remain;
    TextView remainCalc;
    TextView remainCalc2;
    TextView remainCalc3;
    TextView remainCalc4;
    EditText calculateIn;
    Button calculateBtn;


    public static Module instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module);

        instance = this;

        Intent intent = getIntent();
        module = intent.getExtras().getString("m_name");

        setTitle(module);

        table = (TableLayout) findViewById(R.id.table1);
        average = (TextView) findViewById(R.id.average);
        average2 = (TextView) findViewById(R.id.average2);
        remain = (TextView) findViewById(R.id.remain);
        remainCalc = (TextView) findViewById(R.id.remainCalc);
        remainCalc2 = (TextView) findViewById(R.id.remainCalc2);
        remainCalc3 = (TextView) findViewById(R.id.remainCalc3);
        remainCalc4 = (TextView) findViewById(R.id.remainCalc4);
        calculateIn = (EditText) findViewById(R.id.calculatein);
        calculateBtn = (Button) findViewById(R.id.calculatebtn);

        BuildTable();
        temp = 0;
        calc();
        average.setText("Your module average is");
        average2.setText(String.valueOf(String.format("%.1f%%", fin)));
        remain.setText("Over " + percentagetotal + "% of your modules");
        remainCalc.setText("You need to achieve");
        remainCalc3.setText("in your remaining assessments");
        remainCalc4.setText("to achieve 0% overall");

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    calcRemain();
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    return;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_module, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editModule:
                Intent intent = new Intent(getApplicationContext(), EditModule.class);
                intent.putExtra("m_name", module);
                startActivity(intent);
                return true;
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
            int dpAsPixels = (int) (5 * scale + 0.5f);
            if ((i % 2) == 0) {
                row.setBackgroundResource(R.color.tableRow);
            } else {
                row.setBackgroundResource(R.color.tableRowAlt);
            }
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
        dbHelper.close();
    }

    private void calc() {
        dbHelper = new DBHelper(this);

        Cursor c = dbHelper.getCalc(module);

        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();
        for (int i = 0; i < rows; i++) {

            // inner for loop
            for (int j = 0; j < cols; j++) {
                percentagetotal = percentagetotal + c.getInt(1);
            }

            c.moveToNext();
        }
        c.moveToFirst();
        percentagetotal = percentagetotal / 2;

        // outer for loop
        for (int i = 0; i < rows; i++) {

            // inner for loop
            for (int j = 0; j < cols; j++) {
                float mark = c.getInt(0);
                float percentage = c.getInt(1);
                temp = (percentage / percentagetotal) * mark;
            }
            fin = fin + temp;

            c.moveToNext();
        }
        dbHelper.close();

    }

    private void calcRemain() {
        float percent100 = (fin / 100) * percentagetotal;
        float reqInput = Float.valueOf(calculateIn.getText().toString());

        float remainpercent = reqInput - percent100;

        float reqperc = 100 - percentagetotal;
        float reqMark = (remainpercent * 100) / reqperc;

        remainCalc2.setText(String.valueOf(String.format("%.1f%%", reqMark)));
        remainCalc4.setText("to achieve " + (String.valueOf(String.format("%.1f%%", reqInput))) + " overall");

        remainCalc.setVisibility(View.VISIBLE);
        remainCalc2.setVisibility(View.VISIBLE);
        remainCalc3.setVisibility(View.VISIBLE);
        remainCalc4.setVisibility(View.VISIBLE);


    }

    private boolean Validate() {
        valid = true;

        if (calculateIn.getText().toString().matches("")) {
            valid = false;
            calculateIn.setText("0");
        }
        int achieve = Integer.parseInt(calculateIn.getText().toString());

        if (achieve < 1 || achieve > 100) {
            valid = false;
            calculateIn.setError("Please enter a value between 1 - 100");
        }
        return valid;
    }
}
