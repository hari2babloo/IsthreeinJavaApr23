package com.example.hari.isthreeinjava.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.hari.isthreeinjava.DatabaseHelper;

public class DBManager  {

    private DatabaseHelper dbHelper;
    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }



}
