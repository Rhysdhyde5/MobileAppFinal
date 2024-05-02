package com.example.mobileappfinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobileappfinal.TaskModel

const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "task"
private const val TABLE_TASKS = "tasks"

private const val COLUMN_ID = "_id"
private const val COLUMN_TASK_TITLE = "taskname"
private const val COLUMN_TASK_DESCRIPTION = "taskdesc"
private const val COLUMN_TASK_TIME = "tasktime"

//Class to manage database functions for our to-do list app, inherits from inbuilt DB
class TaskDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    //Initialise database table
    override fun onCreate(db: SQLiteDatabase) {
        //here's some raw SQL
        val CREATE_TASKS_TABLE = "CREATE TABLE $TABLE_TASKS($COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TASK_TITLE TEXT," + "$COLUMN_TASK_DESCRIPTION TEXT," + "$COLUMN_TASK_TIME TEXT)"
        db.execSQL(CREATE_TASKS_TABLE)
    }

    //If we need to change the database schema
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    //Major function: read all tasks
    fun listTasks(): MutableList<TaskModel> {
        //our SQL query
        val sql = "SELECT * FROM $TABLE_TASKS"

        //local database
        val db = this.readableDatabase

        val storeTasks = arrayListOf<TaskModel>()

        //remember we look through databases with a cursor to iterate
        val cursor = db.rawQuery(sql, null)

        //go to the top, and if it's not null
        if (cursor.moveToFirst())
            //get the (auto-generated) ID from the first column and text from the second
            do {
                val id = (cursor.getInt(0))
                val name = cursor.getString(1)
                val desc = cursor.getString(2)
                val time = cursor.getString(3)

                //put in arraylist
                storeTasks.add(TaskModel(id, name, desc, time))
                    //keep running until we run out of results
            } while (cursor.moveToNext())

        //don't forget to release the cursor!
            cursor.close()
        return storeTasks
    }

    //Put a new task in the database
    fun addTask(taskName: String, taskDescription: String, taskTime: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_TITLE, taskName)
            put(COLUMN_TASK_DESCRIPTION, taskDescription)
            put(COLUMN_TASK_TIME, taskTime)
        }
        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    //removing a task
    fun deleteTask(id: Int) {
        //a clause to use for matching records in the db
        val where = "$COLUMN_ID = ?"
        val db = this.writableDatabase

        //the delete method supports removing multiple records so wants an array
        db.delete(TABLE_TASKS, where, arrayOf(id.toString()))
    }


    //not used in this demo--consider implementing an interface for it
    fun updateTask(task: TaskModel) {
        //need that key-value pair
        val values = ContentValues()
        values.put(COLUMN_TASK_TITLE, task.taskName)
        val db = this.writableDatabase

        //update a certain table with the values using task ID
        db.update(
            TABLE_TASKS,
            values,
            "$COLUMN_ID = ?",
            arrayOf((task.taskID).toString())
        )
    }

    //not used in this demo--consider writing a front-end for it!
    fun findTask(name: String): TaskModel? {
        //query to execute, looking for all fields of a task by name
        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TASK_TITLE = name"
        val db = this.writableDatabase
        var foundTask : TaskModel? = null


        //again we get an iterator over some (potentially empty) results
        val cursor = db.rawQuery(query, null)

        //if there is a first result, that'll do
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val desc = cursor.getString(2)
            val time = cursor.getString(3)
            foundTask = TaskModel(id, name, desc, time)
        }

        //don't forget to close the cursor
        cursor.close()
        return foundTask
    }
}