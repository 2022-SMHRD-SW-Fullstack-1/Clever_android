package com.example.clever.view.home.more

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clever.R
import com.example.clever.databinding.FragmentCalendarBinding
import com.example.clever.databinding.FragmentMoreBinding
import com.example.clever.view.InquiryActivity

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        binding.moreFImgMember.setOnClickListener {
            val intent = Intent(requireContext(), MoreMemberActivity::class.java)
            startActivity(intent)
        }

        binding.moreLlInquiry.setOnClickListener {
            val intent = Intent(requireContext(), InquiryActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}