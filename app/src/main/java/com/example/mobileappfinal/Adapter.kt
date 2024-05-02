package com.example.mobileappfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.TaskModel

class Adapter(private val dataList: MutableList<TaskModel>, private val db: TaskDatabase?) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val singleTask = dataList[position]
        holder.textView.text = singleTask.taskName
    }

    override fun getItemCount() = dataList.size

    fun getItemAt(position: Int): TaskModel{
        return dataList[position]
    }
}
