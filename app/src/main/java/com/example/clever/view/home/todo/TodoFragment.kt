package com.example.clever.view.home.todo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clever.R
import com.example.clever.databinding.FragmentNoticeBinding
import com.example.clever.databinding.FragmentTodoBinding
import com.example.clever.view.home.notice.NoticeFolderInActivity

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        binding.todoMove.setOnClickListener {
            val intent = Intent(requireContext(), TodoListActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}