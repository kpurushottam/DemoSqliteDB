package com.krp.android.demosqlitedb;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
    }

    public void onClickAddName(View view) {
        ContentValues values = new ContentValues();
        values.put("name", ((EditText) findViewById(R.id.txtName))
                .getText().toString());

        db = dbHelper.getWritableDatabase();
        if (db != null) {
            long rowID = db.insert(TABLE_NAME, "", values);
        }
    }

    public void onClickDisplayNames(View view) {
        db = dbHelper.getReadableDatabase();
        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);
            if(cursor != null) {
                cursor.moveToFirst();
                StringBuilder res=new StringBuilder();
                while (!cursor.isAfterLast()) {
                    res.append("\n"+cursor.getString(cursor.getColumnIndex("id"))+ "-"+
                            cursor.getString(cursor.getColumnIndex("name")));
                    cursor.moveToNext();
                }
                ((TextView) findViewById(R.id.res)).setText(res);
            }
        }
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "com.krp.android.db";
    static final String TABLE_NAME = "names";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " name TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
