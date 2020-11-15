package ca.kylegray.vanvin;

public final class ScanContract {

    private ScanContract(){
    }

    public static class ScanEntry {
        static final String TABLE_NAME = "ivinscans";
        static final String COLUMN_ID = "id";
        static final String COLUMN_1_JOB = "job";
        static final String COLUMN_2_VIN = "vin";
        static final String COLUMN_3_TIMESTAMP = "timestamp";
        static final String COLUMN_4_READING = "reading";
        static final String COLUMN_5_TIRE = "tire";
        static final String COLUMN_6_COMMENT = "comment";
        static final String COLUMN_7_DEVID = "devid";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_1_JOB + " TEXT, " +
                COLUMN_2_VIN + " TEXT, " +
                COLUMN_3_TIMESTAMP + " TEXT, " +
                COLUMN_4_READING + " REAL, " +
                COLUMN_5_TIRE + " TEXT, " +
                COLUMN_6_COMMENT + " TEXT, " +
                COLUMN_7_DEVID + " TEXT" +")";
    }


}
