package com.example.mobileappfinal

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var dataList: MutableList<TaskModel>, private val db: TaskDatabase) : RecyclerView.Adapter<Adapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val descView: TextView = itemView.findViewById(R.id.descView)
        val checkBox: CheckBox = itemView.findViewById(R.id.todoCheckBox)
        val updateButton: ImageView =  itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView =  itemView.findViewById(R.id.deleteButton)
        val exportButton: ImageView = itemView.findViewById(R.id.exportButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val singleTask = dataList[position]
        holder.textView.text = singleTask.taskName
        holder.descView.text = singleTask.taskDec
        holder.checkBox.isChecked = singleTask.completed


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                db.setTaskCompleted(singleTask.taskID, true)
            } else if (!isChecked) {
                db.setTaskCompleted(singleTask.taskID, false)
            }
        }

        holder.exportButton.setOnClickListener {
            val cardView = holder.itemView.findViewById<CardView>(R.id.cardView)
            ImageUtils.saveViewImageToGallery(holder.itemView.context, cardView, "image_$position")
        }


        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_id", singleTask.taskID)
                db.let { it1 -> refreshData(it1.listTasks()) }
                notifyDataSetChanged()
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener{
            db.deleteTask(singleTask.taskID)
            db.let { it1 -> refreshData(it1.listTasks()) }
            Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
            }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra("taskId", singleTask.taskID)
                putExtra("taskName", singleTask.taskName)
                putExtra("taskDescription", singleTask.taskDec)
                putExtra("taskTime", singleTask.taskTime)
                putExtra("blobExtra", singleTask.image)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = dataList.size

    fun refreshData(newTask: MutableList<TaskModel>){
        dataList = newTask
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): TaskModel{
        return dataList[position]
    }

    fun removeItem(position: Int) {
        //handling a bad parameter
        if (position < dataList.size) {
            val task = dataList[position]

            //remove from recycler
            dataList.removeAt(position)

            //remove from db
            db.deleteTask(task.taskID)

            //we need to tell the layout manager to allow the list to be rebuilt if needed
            notifyItemRemoved(position)
        }// Adjust method based on your database implementation
    }

}
