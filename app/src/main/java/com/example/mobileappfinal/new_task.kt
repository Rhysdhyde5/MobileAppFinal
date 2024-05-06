package com.example.mobileappfinal

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mobileappfinal.databinding.FragmentNewTaskBinding
import java.util.Calendar
import java.util.Date

class new_task : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TaskDatabase
    private lateinit var taskAdapter: Adapter



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

        val saveButton = binding.saveButton
        saveButton.setOnClickListener {
            scheduleNotification()
            val name = binding.name.text.toString()
            val description = binding.desc.text.toString()
            val time = binding.addTime.text.toString()
            // Call addTask method to save the task
            db.addTask(name, description, time)

            binding.name.text?.clear()
            binding.desc.text?.clear()
            binding.addTime.text?.clear()



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


}

