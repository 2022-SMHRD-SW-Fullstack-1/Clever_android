package com.example.clever.view.home.cal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clever.R
import com.example.clever.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter

private lateinit var binding: FragmentCalendarBinding

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        binding = FragmentCalendarBinding.inflate(layoutInflater)


//
//        binding.calendarview.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
//        binding.calendarview.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        return view
    }

}