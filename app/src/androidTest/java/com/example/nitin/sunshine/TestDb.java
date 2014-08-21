package com.example.nitin.sunshine;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.nitin.sunshine.data.WeatherDbHelper;


public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
    public void testInsertReadOperation()
    {
        //we can add code here to check if our insert and delete works or not.
        //Simply create some values and add them to a Content Values class via Contract class.
        //Insert these values into the database using a DbHelper.
        //Read these values from Db. No need to go through each and every value.
        //Just check that if the cursor has a row or not. If cursor is empty then that means our insert didn't work.

    }
}