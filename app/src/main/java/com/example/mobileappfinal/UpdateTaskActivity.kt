package com.example.mobileappfinal

import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.ActivityUpdateTaskBinding
import com.example.mobileappfinal.databinding.FragmentListBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TaskDatabase
    private lateinit var taskAdapter: Adapter
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDatabase(this)

        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1){
            finish()
            return
        }


        val singleTask = db.getTaskByID(taskId)
        binding.nameUpdate.setText(singleTask.taskName)
        binding.descUpdate.setText(singleTask.taskDec)
        binding.addTimeUpdate.setText(singleTask.taskTime)

        binding.saveButtonUpdate.setOnClickListener{
            val newName = binding.nameUpdate.text.toString()
            val newDesc = binding.descUpdate.text.toString()
            val newTime = binding.addTimeUpdate.text.toString()
            val updatedTask = TaskModel(taskId, newName, newDesc, newTime, singleTask.completed, singleTask.image)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}