package com.example.mobileappfinal

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileappfinal.databinding.ActivityTaskDetailsBinding
import com.example.mobileappfinal.databinding.ActivityUpdateTaskBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val taskName = intent.getStringExtra("taskName")
        val taskDescription = intent.getStringExtra("taskDescription")
        val taskTime = intent.getStringExtra("taskTime")
        val imageV = intent.getByteArrayExtra("blobExtra")

        // Populate UI with task details
        findViewById<TextView>(R.id.taskNameTextView).text = taskName
        findViewById<TextView>(R.id.taskDescriptionTextView).text = taskDescription
        findViewById<TextView>(R.id.taskTimeTextView).text = taskTime
        if(imageV != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageV, 0, imageV.size)
            findViewById<ImageView>(R.id.taskDisplayView).setImageBitmap(bitmap)
        }

        findViewById<Button>(R.id.closeButton).setOnClickListener {
            finish()

        }
    }
}