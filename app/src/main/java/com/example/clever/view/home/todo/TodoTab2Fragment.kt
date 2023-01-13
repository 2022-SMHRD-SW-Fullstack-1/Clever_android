package com.example.clever.view.home.todo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clever.databinding.FragmentTodoTab2Binding

class TodoTab2Fragment : Fragment() {

    private var _binding: FragmentTodoTab2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab2Binding.inflate(inflater, container, false)

        binding.tab2Move.setOnClickListener {
            val intent = Intent(requireContext(), TodoCompleteActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}