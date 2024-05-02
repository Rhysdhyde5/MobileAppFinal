package com.example.mobileappfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.ActivityMainBinding
import com.example.mobileappfinal.databinding.FragmentListBinding


class List : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)

        val db = TaskDatabase(requireContext())
        val taskList = db.listTasks()
        val taskAdapter = Adapter(taskList, db)
        val taskView = binding.taskList
        val manager = LinearLayoutManager(requireContext())
        taskView.layoutManager = manager
        taskView.adapter = taskAdapter
        return binding.root
    }
}