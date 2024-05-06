package com.example.mobileappfinal


import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mobileappfinal.databinding.FragmentParticipationBinding


class Participation : Fragment() {

    private var _binding: FragmentParticipationBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TaskDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParticipationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = TaskDatabase(requireContext())
        val totalTask = db.getRowCount()
        val completedTask = db.countCompletedTasks()
        val percentage = (completedTask.toFloat() / totalTask.toFloat() * 100).toInt()

        updateCircleAndText(percentage)
    }

    private fun determineColor(percentage: Int): Int {
        return when {
            percentage < 30 -> ContextCompat.getColor(requireContext(), R.color.red)
            percentage < 60 -> ContextCompat.getColor(requireContext(), R.color.yellow)
            else -> ContextCompat.getColor(requireContext(), R.color.green)
        }
    }

    private fun updateCircleAndText(percentage: Int) {
        val circleColor = determineColor(percentage)
        binding.circleView.setColorFilter(circleColor, PorterDuff.Mode.SRC_IN)

        val text = determineText(percentage)
        binding.participationL.text = text
    }

    private fun determineText(percentage: Int): String {
        return when {
            percentage < 30 -> "L"
            percentage < 60 -> "M"
            else -> "H"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}