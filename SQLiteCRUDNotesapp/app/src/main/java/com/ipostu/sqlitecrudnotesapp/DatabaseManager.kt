package com.ipostu.sqlitecrudnotesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DatabaseManager {

    companion object {
        var DB_NAME = "MyNotes"
        var DB_TABLE = "Notes"
        var COLUMN_ID = "ID"
        var COLUMN_TITLE = "Title"
        var COLUMN_DESCRIPTION = "Description"
        var DB_VERSION = 1

        var SQL_CREATE_TABLE = "CREATE TABLE IF NOTE EXISTS " + DB_TABLE +
                " ( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);"

        var SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DB_NAME + ";";

        var sqlDB: SQLiteDatabase? = null
    }

    constructor(context: Context) {
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        private var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(SQL_CREATE_TABLE)
            Toast.makeText(this.context, "Database created...", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL(SQL_DROP_TABLE)
        }
    }

    fun insert(values: ContentValues): Long {
        val id = sqlDB!!.insert(DB_TABLE, "", values)
        return id
    }

    fun query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ): Cursor {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = DB_TABLE
        val cursor = queryBuilder.query(
            sqlDB, projection, selection, selectionArgs, null, null, sortOrder
        )
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(DB_TABLE, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.update(DB_TABLE, values, selection, selectionArgs)
        return count
    }
}