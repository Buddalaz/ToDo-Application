package com.example.sycomusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "todo";
    private static final String TABLE_NAME = "todo";

    //column name
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String STARTED = "started";
    private static final String FINISHED ="finished";

    private static SQLiteDatabase sqLiteDatabase;

    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " " +
                "("
                +  ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + STARTED + " TEXT,"
                + FINISHED + " TEXT" +
                ");";

        db.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;

        db.execSQL(DROP_TABLE_QUERY);

        onCreate(db);
    }

    //method to add data to database
    public Long addToDo(ToDo toDo) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE, toDo.getTitle());
        contentValues.put(DESCRIPTION, toDo.getDescription());
        contentValues.put(STARTED, toDo.getStarted());
        contentValues.put(FINISHED, toDo.getFinished());

        //save to table
        long insert = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        //close database
        sqLiteDatabase.close();

        return insert;


    }

    public int countToDo(){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }

    //get all todos into a list
    public List<ToDo> getAllToDos(){

        List<ToDo> toDos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
                do {
                    //create new ToDo object
                    ToDo toDo = new ToDo();

                    //
                    toDo.setId(cursor.getInt(0));
                    toDo.setTitle(cursor.getString(1));
                    toDo.setDescription(cursor.getString(2));
                    toDo.setStarted(cursor.getLong(3));
                    toDo.setFinished(cursor.getLong(4));

                    //
                    toDos.add(toDo);

                }while (cursor.moveToNext());
        }
        return toDos;
    }

    public int deleteToDo(int id){
        SQLiteDatabase db = getWritableDatabase();
        int toDoDelete = db.delete(TABLE_NAME, ID + " =?", new String[]{String.valueOf(id)});
        db.close();
        return toDoDelete;
    }

    //Get a single ToDo
    public ToDo getSingleToDo(int id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[]{ID,TITLE,DESCRIPTION,STARTED,FINISHED},ID + "= ?",
                new String[]{String.valueOf(id)},null,null,null);

        ToDo toDo;
        if (cursor != null){
            cursor.moveToFirst();
            toDo = new ToDo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(3),
                    cursor.getLong(4)
            );
            return toDo;
        }
        return null;
    }


    //update singleToDo
    public int updateSingleToDo(ToDo toDo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE, toDo.getTitle());
        contentValues.put(DESCRIPTION, toDo.getDescription());
        contentValues.put(STARTED, toDo.getStarted());
        contentValues.put(FINISHED, toDo.getFinished());

        int status = db.update(TABLE_NAME,contentValues,ID + " =?",
                new String[]{String.valueOf(toDo.getId())});

        db.close();

        return status;
    }



}
