package com.example.mobileappfinal

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.example.mobileappfinal.databinding.FragmentNewTaskBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.Date

class new_task : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TaskDatabase
    private lateinit var taskAdapter: Adapter

    private val PICK_IMAGE_REQUEST = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)

        db = TaskDatabase(requireContext()) // Initialize db
        taskAdapter = Adapter(db.listTasks(), db) // Initialize taskAdapter

        val selectImage = binding.imageButton
        selectImage.setOnClickListener{
            launchImagePicker()
        }

        val saveButton = binding.saveButton
        saveButton.setOnClickListener {
            scheduleNotification()
            val name = binding.name.text.toString()
            val description = binding.desc.text.toString()
            val time = binding.addTime.text.toString()
            val imageView = binding.taskImageView



            db.addTask(name, description, time, imageViewToByteArray(imageView))

            binding.name.text?.clear()
            binding.desc.text?.clear()
            binding.addTime.text?.clear()
            binding.taskImageView.setImageBitmap(null)



        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleNotification() {
        val intent = Intent(context, Notification::class.java)
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        intent.putExtra(titleExtra, name)
        intent.putExtra(messageExtra, desc)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, 10000)

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            showAlert(calendar.timeInMillis, name, desc)

    }

    private fun showAlert(calender: Long, name: String, desc: String) {
        val date = Date(calender)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context)
            .setTitle("notification Scheduled")
            .setMessage("Title: " + name + "\n Description " + desc + "\n At " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_->}
            .show()

    }


    private fun createNotificationChannel()
    {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN
        channel.enableVibration(false)
        channel.description = desc
        channel.lightColor = Color.RED
        val notificationManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchImagePicker() {
        // Create an intent to pick an image from the device's storage
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri = data.data

            // Display the selected image in the ImageView
            val imageView = view?.findViewById<ImageView>(R.id.taskImageView)
            imageView?.setImageURI(imageUri)
        }
    }

    fun imageViewToByteArray(imageView: ImageView): ByteArray? {
        // Get the drawable displayed in the ImageView
        val drawable = imageView.drawable

        if (drawable is BitmapDrawable) {
            // If the drawable is a BitmapDrawable, extract the Bitmap
            val bitmap = drawable.bitmap

            // Convert Bitmap to ByteArray
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        } else {
            // Handle case where the drawable is not a BitmapDrawable
            Log.e("imageViewToByteArray", "ImageView does not display a BitmapDrawable")
            return null
        }
    }


}

