package com.ameegolabs.chidyaudd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    //------------------        Flying Object     ---------------------------
    private static final String TABLE_NAME_FLYING = "flying";
    private static final String KEY_FLYING = "value";

    //------------------        Non Flying Object     ---------------------------
    private static final String TABLE_NAME_NON_FLYING = "nonflying";
    private static final String KEY_NON_FLYING = "value";


    public DBHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*----------------------------------------Flying------------------------------------------------*/
        String CREATE_TABLE_FLYING = "CREATE TABLE " + TABLE_NAME_FLYING +
                "(" + KEY_FLYING + " text"
                + ")";

        db.execSQL(CREATE_TABLE_FLYING);
        MyUtils.logThis("DBHelper - Table Flying created");

        /*----------------------------------------Non Flying------------------------------------------------*/
        String CREATE_TABLE_NON_FLYING = "CREATE TABLE " + TABLE_NAME_NON_FLYING +
                "(" + KEY_NON_FLYING + " text"
                + ")";

        db.execSQL(CREATE_TABLE_NON_FLYING);
        MyUtils.logThis("DBHelper - Table Non Flying created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FLYING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NON_FLYING);
        onCreate(db);
        MyUtils.logThis("DBHelper - Tables upgraded");
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FLYING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NON_FLYING);
        onCreate(db);
    }

    public void addFlyingObject(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FLYING, name);
        db.insert(TABLE_NAME_FLYING, null, values);
        MyUtils.logThis("DBHelper - Flying object added : value = " + name);
        db.close();

    }

    public void addNonFlyingObject(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NON_FLYING, name);
        db.insert(TABLE_NAME_NON_FLYING, null, values);
        MyUtils.logThis("DBHelper - Non Flying object added : value = " + name);
        db.close();

    }

    public ArrayList<String> getAllFlyingObjects() {

        ArrayList<String> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_FLYING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            db.close();
            return arrayList;
        }
        cursor.close();
        db.close();
        return null;
    }

    public ArrayList<String> getAllNonFlyingObjects() {

        ArrayList<String> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_NON_FLYING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
            db.close();
            return arrayList;
        }
        cursor.close();
        db.close();
        return null;
    }

    public void deleteFlyingObject(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_FLYING, KEY_FLYING + "='" + name + "'", null);
        db.close();
    }

    public void deleteNonFlyingObject(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_NON_FLYING, KEY_NON_FLYING + "='" + name + "'", null);
        db.close();
    }

    /*ArrayList<String> arrayList = new ArrayList<>();
        String selectQuery = "DELETE FROM " + TABLE_NAME_FLYING + " WHERE " + KEY_FLYING + " = '" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(selectQuery, null);
        */


}//DBHelper