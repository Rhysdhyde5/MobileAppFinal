package com.example.mobileappfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.FragmentListBinding


class List : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskdb: TaskDatabase

    private lateinit var taskAdapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        taskdb = TaskDatabase(requireContext())

        val taskList = taskdb.listTasks()
        taskAdapter = Adapter(taskList, taskdb) // Initialize taskAdapter
        val taskView = binding.taskList
        val manager = LinearLayoutManager(requireContext())
        taskView.layoutManager = manager
        taskView.adapter = taskAdapter
        taskAdapter.refreshData(taskList)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        // Refresh task data when the fragment resumes
        val taskList = taskdb.listTasks()
        taskAdapter.refreshData(taskList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }
}
