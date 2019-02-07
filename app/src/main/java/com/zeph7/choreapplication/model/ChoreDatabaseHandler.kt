package com.zeph7.choreapplication.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zeph7.choreapplication.data.*

class ChoresDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {

        //SQL - Structured Query Language

        var CREATE_CHORE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"+
                KEY_CHORE_NAME + " TEXT," +
                KEY_CHORE_ASSIGNED_BY + " TEXT," +
                KEY_CHORE_ASSIGNED_TO + " TEXT," +
                KEY_CHORE_ASSIGNED_TIME + " LONG" +");"

        db?.execSQL(CREATE_CHORE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        //create table again
        onCreate(db)
    }

    /**
     *  CRUD - Create, Read, Update, Delete
     */

    fun createChore(chore: Chore) {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        var insert = db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun readAChore(id: Int): Chore {
        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(
                KEY_ID,
                KEY_CHORE_NAME, KEY_CHORE_ASSIGNED_BY,
                KEY_CHORE_ASSIGNED_TIME,
                KEY_CHORE_ASSIGNED_TO
            ), KEY_ID + "=?", arrayOf(id.toString()),
            null, null, null, null)

        if (cursor != null)
            cursor.moveToFirst()

        var chore = Chore()
        chore.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        chore.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
        chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
        chore.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))
        chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))

        return chore

    }

    fun readChores(): ArrayList<Chore> {
        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<Chore> = ArrayList()

        //Select all chores from table
        var selectAll = "SELECT * FROM " + TABLE_NAME

        var cursor: Cursor = db.rawQuery(selectAll, null)

        //loop through our chores
        if (cursor.moveToFirst()) {
            do {
                var chore = Chore()

                chore.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                chore.choreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
                chore.assignedTo = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TO))
                chore.timeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_TIME))
                chore.assignedBy = cursor.getString(cursor.getColumnIndex(KEY_CHORE_ASSIGNED_BY))

                list.add(chore)

            }while (cursor.moveToNext())
        }
        return list

    }

    fun updateChore(chore: Chore): Int {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        //update a row
        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(chore.id.toString()))
    }


    fun deleteChore(id: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))

        db.close()
    }

    fun getChoresCount(): Int {
        var db: SQLiteDatabase = readableDatabase
        var countQuery = "SELECT * FROM " + TABLE_NAME
        var cursor: Cursor = db.rawQuery(countQuery, null)

        return cursor.count
    }
}