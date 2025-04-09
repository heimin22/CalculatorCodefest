package com.example.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "CALCULATOR";
    private static final String TABLE_NAME = "HISTORY_TBL";
    private static final String COL_ID = "ID";
    private static final String COL_EXPRESSION = "EXPRESSION";
    private static final String COL_RESULT = "RESULT";
    private static final String COL_TIMESTAMP = "TIMESTAMP";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void addToTable(String expression, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EXPRESSION, expression);
        values.put(COL_RESULT, result);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<String> readHistoryTbl() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> historyList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIMESTAMP, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String expression = cursor.getString(1);
                String result = cursor.getString(2);
                String timestamp = cursor.getString(3);

                historyList.add("ID: " + id + ",\nExpression: " + expression + ",\nResult: " + result + ",\nTimestamp: " + timestamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_EXPRESSION + " TEXT, "
                + COL_RESULT + " TEXT, "
                + COL_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }
}


