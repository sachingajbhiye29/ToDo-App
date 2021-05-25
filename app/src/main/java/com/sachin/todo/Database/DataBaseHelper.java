package com.sachin.todo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sachin.todo.Model.ToDoModel;
import com.sachin.todo.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Database object to perform operation
    private SQLiteDatabase db;

    //Database name;
    private static final String DATABASE_NAME = "TODO_DATABASE";

    //Table name;
    private static final String TABLE_NAME = "TODO_TABLE";

    //Column name for table;
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop the table if exists;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //method for inserting data into table
    public  void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        //status will be 0 as uncheck;
        values.put(COL_3, 0);

        //now insert the data using insert method;
        db.insert(TABLE_NAME, null, values);
    }

    //method for updating the task;
    public  void updateTask(int id, String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);

        //now update the task using update method
        db.update(TABLE_NAME, values, "ID=?", new String[] {String.valueOf(id)});
    }

    //method to update status also
    public void updateStatus(int id, int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);

        //update the status with update method;
        db.update(TABLE_NAME, values, "ID=?",new String[]{String.valueOf(id)});
    }

    //method to delete the task;
    public void deleteTask(int id){

        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }


    //method to get all the data and view it;
    // this method will return list of dats so it's return type must be of List<>;
    public List<ToDoModel> getAllTask(){
        db = this.getWritableDatabase();
        Cursor  cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try{
            // now add the data in the cursor
            cursor = db.query(TABLE_NAME, null, null, null, null, null,null);

            //check if the cursor has data
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{

                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));

                        modelList.add(task);

                    }while(cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }


}
