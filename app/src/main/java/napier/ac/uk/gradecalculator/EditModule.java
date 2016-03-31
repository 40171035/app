package napier.ac.uk.gradecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.view.View;

public class EditModule extends AppCompatActivity{

    DBHelper dbHelper;
    private String module;

    Button save;
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

        table = (TableLayout) findViewById(R.id.table);
        save = (Button) findViewById(R.id.save);
        mark = (EditText) findViewById(R.id.addMark);
        percentage = (EditText) findViewById(R.id.addPercentage);
        reference = (EditText) findViewById(R.id.addReference);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int markint = Integer.parseInt(mark.getText().toString());
                int percentageint = Integer.parseInt(percentage.getText().toString());
                String referencestring = reference.getText().toString();

                dbHelper.addResult(module, markint, percentageint, referencestring);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_module, menu);
        return true;
    }
}