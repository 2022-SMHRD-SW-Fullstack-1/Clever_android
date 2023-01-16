package com.example.clever.view.home.todo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clever.databinding.FragmentTodoTab1Binding

class TodoTab1Fragment : Fragment() {

    private var _binding: FragmentTodoTab1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab1Binding.inflate(inflater, container, false)

        return binding.root
    }

}