package napier.ac.uk.gradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Module.db";
    private static final int DATABASE_VERSION = 1;
    public static final String RESULTS_TABLE_NAME = "results";
    public static final String RESULTS_COLUMN_R_ID = "r_id";
    public static final String RESULTS_COLUMN_ID = "_id";
    public static final String RESULTS_COLUMN_MARK = "mark";
    public static final String RESULTS_COLUMN_PERCENTAGE = "percentage";
    public static final String RESULTS_COLUMN_REFERENCE = "reference";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RESULTS_TABLE_NAME + "(" +
                        RESULTS_COLUMN_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RESULTS_COLUMN_ID + " TEXT, " +
                        RESULTS_COLUMN_MARK + " INTEGER, " +
                        RESULTS_COLUMN_PERCENTAGE + " INTEGER, " +
                        RESULTS_COLUMN_REFERENCE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean addResult(String _id, int mark, int percentage, String reference) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESULTS_COLUMN_ID, _id);
        contentValues.put(RESULTS_COLUMN_MARK, mark);
        contentValues.put(RESULTS_COLUMN_PERCENTAGE, percentage);
        contentValues.put(RESULTS_COLUMN_REFERENCE, reference);
        db.insert(RESULTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean addSingleResult(String _id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESULTS_COLUMN_ID, _id);
        db.insert(RESULTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT _id FROM " + RESULTS_TABLE_NAME, null);
        return res;
    }

    public Cursor getModule(String module) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT mark, percentage, reference FROM " + RESULTS_TABLE_NAME + " WHERE " +
                RESULTS_COLUMN_ID + "=? AND percentage IS NOT NULL", new String[]{module});
        return res;
    }

    public Cursor getCalc(String module) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT mark, percentage FROM " + RESULTS_TABLE_NAME + " WHERE " +
                RESULTS_COLUMN_ID + "=? AND percentage IS NOT NULL", new String[]{module});
        return res;
    }

    public Integer deleteResults(String module) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RESULTS_TABLE_NAME,
                RESULTS_COLUMN_ID + " = ? ",
                new String[]{module});
    }

    public Integer Refine(String module) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RESULTS_TABLE_NAME,
                RESULTS_COLUMN_ID + " = ? AND percentage IS NULL OR percentage = 0",
                new String[]{module});
    }
}
