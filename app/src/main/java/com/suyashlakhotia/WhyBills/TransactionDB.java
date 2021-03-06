package com.suyashlakhotia.WhyBills;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TransactionDB {


    private static final String DATABASE_NAME = "transactions";

    private static final int DATABASE_VERSION = 5;

    public static String DATABASE_TABLE = "TransactionTable";
    public static final String KEY_TIME = "timeStamp";
    public static final String KEY_STORE = "store";
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_ITEM_PRICE = "Price";
    public static final String KEY_QTY = "qty";
    public static final String KEY_TOTPRICE = "totprice";
    public static final String KEY_TRANSACTION_ID = "transactionID";
    public static final String KEY_ID = "_id";

    private static final String TAG = "TransactionDB";
    public DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    /**
     * Database creation SQL statement.
     */
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ID + " int primary key, " + KEY_TIME + " text not null, "
                    + KEY_STORE + " text not null, "
                    + KEY_ITEM_NAME + " text not null, "
                    + KEY_ITEM_PRICE + " float(7,4) not null, "
                    + KEY_QTY + " integer not null, "
                    + KEY_TOTPRICE + " float(7,4) not null, "
                    + KEY_TRANSACTION_ID + " integer not null);";


    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }


    /**
     * Constructor - takes the context to allow the database to be
     * opened/created.
     */
    public TransactionDB(Context ctx) {
        this.mCtx = ctx;
    }


    /**
     * Opens the database. If it cannot be opened, tries to create a new
     * instance of the database. If it cannot be created, throws an exception to
     * signal the failure.
     */
    public TransactionDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Creates a new record.
     */
    public void AddItems(String timestamp, String store,String itemName, float price, int qty, int trID) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIME, timestamp);
        initialValues.put(KEY_STORE, store);
        initialValues.put(KEY_ITEM_NAME, itemName);
        initialValues.put(KEY_ITEM_PRICE, price);
        initialValues.put(KEY_QTY, qty);
        initialValues.put(KEY_TOTPRICE, price*qty);
        initialValues.put(KEY_TRANSACTION_ID, trID);


        long n =  mDb.insert(DATABASE_TABLE, null, initialValues);
    }


    /**
     * Deletes the entire transactions table.
     */
    public boolean deleteTransaction() {

        mDb.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        return true;
    }


    /**
     * Returns a Cursor over the list of all the records in the database.
     */
    public Cursor fetchItem(int trID) throws SQLException {
        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ID, KEY_TIME,
                                KEY_STORE, KEY_ITEM_NAME, KEY_ITEM_PRICE, KEY_QTY, KEY_TOTPRICE, KEY_TRANSACTION_ID}, KEY_TRANSACTION_ID + "=" + trID, null,
                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}