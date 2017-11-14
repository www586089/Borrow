package com.jyx.android.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 2016/4/21.
 */
public class MySqlistHelpe extends SQLiteOpenHelper{


    private static final String name = "user"; //数据库名称
    private static final int version = 1; //数据库版本


    public MySqlistHelpe(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(_id integer primary key autoincrement, userid varchar(30), phone varchar(20))");
        db.execSQL("create table follow(_id integer primary key autoincrement, userid varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
