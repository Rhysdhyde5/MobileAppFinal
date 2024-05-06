package com.example.mobileappfinal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobileappfinal.TaskModel


private val DATABASE_VERSION = 5
private const val DATABASE_NAME = "task"
private const val TABLE_TASKS = "tasks"
private const val COLUMN_ID = "_id"
private const val COLUMN_TASK_TITLE = "taskname"
private const val COLUMN_TASK_DESCRIPTION = "taskdesc"
private const val COLUMN_TASK_TIME = "tasktime"
private const val COLUMN_COMPLETED_TASK = "taskComplete"


//Class to manage database functions for our to-do list app, inherits from inbuilt DB
class TaskDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    //Initialise database table
    override fun onCreate(db: SQLiteDatabase) {
        //here's some raw SQL
        val CREATE_TASKS_TABLE = "CREATE TABLE $TABLE_TASKS($COLUMN_ID INTEGER PRIMARY KEY," +
                "$COLUMN_TASK_TITLE TEXT," +
                "$COLUMN_TASK_DESCRIPTION TEXT," +
                "$COLUMN_TASK_TIME TEXT," +
                "$COLUMN_COMPLETED_TASK BOOLEAN)"
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
                val completed = cursor.getInt(4) == 1

                //put in arraylist
                storeTasks.add(TaskModel(id, name, desc, time, completed))
                    //keep running until we run out of results
            } while (cursor.moveToNext())

        //don't forget to release the cursor!
            cursor.close()
            db.close()
        return storeTasks
    }

    //Put a new task in the database
    fun addTask(taskName: String, taskDescription: String, taskTime: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_TITLE, taskName)
            put(COLUMN_TASK_DESCRIPTION, taskDescription)
            put(COLUMN_TASK_TIME, taskTime)
            put(COLUMN_COMPLETED_TASK, false)
        }

        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    //removing a task
    fun deleteTask(taskId: Int) {
        //a clause to use for matching records in the db
        val db = this.writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())


        //the delete method supports removing multiple records so wants an array
        db.delete(TABLE_TASKS, whereClause, whereArgs)
        db.close()


    }


    //not used in this demo--consider implementing an interface for it
    fun updateTask(task: TaskModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_TITLE, task.taskName)
            put(COLUMN_TASK_DESCRIPTION, task.taskDec)
            put(COLUMN_TASK_TIME, task.taskTime)

        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(task.taskID.toString())
        db.update(TABLE_TASKS, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskByID(taskId: Int): TaskModel{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_ID = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(0)
        val name = cursor.getString(1)
        val desc = cursor.getString(2)
        val time = cursor.getString(3)
        val completed = cursor.getInt(4) == 1

        cursor.close()
        db.close()
        return TaskModel(id, name, desc, time, completed)
    }

    fun getRowCount(): Int {
        val db = this.readableDatabase
        var count = 0

        val query = "SELECT COUNT(*) FROM $TABLE_TASKS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return count
    }
    fun countCompletedTasks(): Int {
        val db = this.readableDatabase
        var count = 0

        val query = "SELECT COUNT(*) FROM $TABLE_TASKS WHERE $COLUMN_COMPLETED_TASK = 1"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return count
    }

    fun setTaskCompleted(taskId: Int, value: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_COMPLETED_TASK, value) // Set completed to true
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())

        val rowsAffected = db.update(TABLE_TASKS, contentValues, whereClause, whereArgs)

        db.close()
    }

}