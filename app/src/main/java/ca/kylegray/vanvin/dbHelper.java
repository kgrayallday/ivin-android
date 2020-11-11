package ca.kylegray.vanvin;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;
import android.widget.Toast;

// THIS IS THE SQLITE DB HELPER FOR INTERNAL DATABASE

public class dbHelper extends SQLiteOpenHelper{
    private static final String TAG = "*** DB Worker ***";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "ivindatabase.db";
    static final String TABLE_NAME = "ivinscans";
    static final String COLUMN_ID = "id";
    static final String COLUMN_1_JOB = "job";
    static final String COLUMN_2_VIN = "vin";
    static final String COLUMN_3_TIMESTAMP = "timestamp";
    static final String COLUMN_4_READING = "reading";
    static final String COLUMN_5_TIRE = "tire";
    static final String COLUMN_6_COMMENT = "comment";
    static final String COLUMN_7_DEVID = "devid" ;

    //Constructor
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqldb) {
        try {
            sqldb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_1_JOB + " TEXT, " +
                    COLUMN_2_VIN + " TEXT, " +
                    COLUMN_3_TIMESTAMP + " TEXT, " +
                    COLUMN_4_READING + " DECIMAL(2,2), " +
                    COLUMN_5_TIRE + " TEXT, " +
                    COLUMN_6_COMMENT + " TEXT, " +
                    COLUMN_7_DEVID + " TEXT" +")");
            Log.d(TAG,"database successfully created");
        }catch (Exception e){
            Log.d(TAG,"database not created");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqldb, int oldVersion, int newVersion) {
        sqldb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqldb);
        Log.d(TAG, "database upgraded");

    }

    public boolean toDB (String job, String stamp, String vin, String comment, String devid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScanContract.ScanEntry.COLUMN_1_JOB, job);
        values.put(ScanContract.ScanEntry.COLUMN_3_TIMESTAMP,stamp);
        values.put(ScanContract.ScanEntry.COLUMN_2_VIN, vin);
        values.put(ScanContract.ScanEntry.COLUMN_6_COMMENT, comment);
        values.put(ScanContract.ScanEntry.COLUMN_7_DEVID, devid);
        long inserted = db.insert(ScanContract.ScanEntry.TABLE_NAME,null ,values);

        if(inserted == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean toDB (String job, String stamp, String vin, Double reading, String comment, String devid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScanContract.ScanEntry.COLUMN_1_JOB, job);
        values.put(ScanContract.ScanEntry.COLUMN_3_TIMESTAMP,stamp);
        values.put(ScanContract.ScanEntry.COLUMN_2_VIN, vin);
        values.put(ScanContract.ScanEntry.COLUMN_4_READING, reading);
        values.put(ScanContract.ScanEntry.COLUMN_6_COMMENT, comment);
        values.put(ScanContract.ScanEntry.COLUMN_7_DEVID, devid);
        long inserted = db.insert(ScanContract.ScanEntry.TABLE_NAME,null ,values);

        if(inserted == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean toDB (String job, String stamp, String vin, String reading, String tire, String comment, String devid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScanContract.ScanEntry.COLUMN_1_JOB, job);
        values.put(ScanContract.ScanEntry.COLUMN_3_TIMESTAMP,stamp);
        values.put(ScanContract.ScanEntry.COLUMN_2_VIN, vin);
        values.put(ScanContract.ScanEntry.COLUMN_4_READING, reading);
        values.put(ScanContract.ScanEntry.COLUMN_5_TIRE, tire);
        values.put(ScanContract.ScanEntry.COLUMN_6_COMMENT, comment);
        values.put(ScanContract.ScanEntry.COLUMN_7_DEVID, devid);
        long inserted = db.insert(ScanContract.ScanEntry.TABLE_NAME,null ,values);

        if(inserted == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean toDB (String job, String stamp, String vin, String tire, String comment, String devid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScanContract.ScanEntry.COLUMN_1_JOB, job);
        values.put(ScanContract.ScanEntry.COLUMN_3_TIMESTAMP,stamp);
        values.put(ScanContract.ScanEntry.COLUMN_2_VIN, vin);
        values.put(ScanContract.ScanEntry.COLUMN_5_TIRE, tire);
        values.put(ScanContract.ScanEntry.COLUMN_6_COMMENT, comment);
        values.put(ScanContract.ScanEntry.COLUMN_7_DEVID, devid);
        long inserted = db.insert(ScanContract.ScanEntry.TABLE_NAME,null ,values);

        if(inserted == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getScans(String job) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
/*        String[] projection = {
                ScanContract.ScanEntry.COLUMN_ID,
                ScanContract.ScanEntry.COLUMN_1_JOB,
                ScanContract.ScanEntry.COLUMN_2_VIN,
                ScanContract.ScanEntry.COLUMN_3_TIMESTAMP,
                ScanContract.ScanEntry.COLUMN_4_READING,
                ScanContract.ScanEntry.COLUMN_5_TIRE,
                ScanContract.ScanEntry.COLUMN_6_COMMENT,
                ScanContract.ScanEntry.COLUMN_7_DEVID,
        };*/
        String selectionWhere = COLUMN_1_JOB + " = " + job;
        Cursor cursor = null;                    // sort by
        try {
            cursor = sqldb.query(
                    ScanContract.ScanEntry.TABLE_NAME, // Table to query
                    null,                     // Columns to return
                    selectionWhere,                    // Columns for Where clause
                    null,                     // Values for Where Clause
                    null,                     // Group By rows
                    null,                      // filer by row groups
                    null);
        } finally {

        }
        return cursor;
    }

    public void dumpScans(String job){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        sqldb.execSQL("DELETE FROM ivinscans WHERE job = " + job);
    }

}
