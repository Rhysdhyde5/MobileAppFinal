package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileappfinal.databinding.FragmentListBinding
import com.example.mobileappfinal.databinding.FragmentNewTaskBinding
import com.example.mobileappfinal.List

class new_task : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: TaskDatabase
    private lateinit var taskAdapter: Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)

        db = TaskDatabase(requireContext()) // Initialize db
        taskAdapter = Adapter(db.listTasks(), db) // Initialize taskAdapter

        val saveButton = binding.saveButton
        saveButton.setOnClickListener {
            val name = binding.name.text.toString()
            val description = binding.desc.text.toString()
            val time = binding.addTime.text.toString()

            // Call addTask method to save the task
            db.addTask(name, description, time)

            navigateToTaskListFragment()
        }

        return binding.root
    }

    // Optional method to navigate back to the task list fragment
    private fun navigateToTaskListFragment() {
        // Replace the current fragment with the task list fragment
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.FrameLayout, List())
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

