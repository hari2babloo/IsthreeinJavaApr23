package com.example.hari.isthreeinjava.Models;

import android.content.Context;
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
