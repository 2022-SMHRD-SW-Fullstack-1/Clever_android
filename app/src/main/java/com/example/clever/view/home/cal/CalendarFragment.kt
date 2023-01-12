package com.example.clever.view.home.cal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // 초기화시 오늘 날짜 선택
        binding.calendarCv.selectedDate = CalendarDay.today()

        // 달력 요일별 색, 선택시 색
        binding.calendarCv.addDecorator(WeekdaysDecorator())
        binding.calendarCv.addDecorator(SaturdayDecorator())
        binding.calendarCv.addDecorator(SundayDecorator())
        binding.calendarCv.addDecorator(CalendarDecorator())
        binding.calendarCv.addDecorator(SelectDecorator(requireActivity()))

        // header, 요일 한글로 변경
        binding.calendarCv.setTitleFormatter(object : TitleFormatter{
            override fun format(day: CalendarDay?): CharSequence {
                return "${day!!.year}년 ${day.month+1}월"
            }
        })
        binding.calendarCv.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)));

        // 날짜 클릭 이벤트
        binding.calendarCv.setOnDateChangedListener { widget, date, selected ->
            var year = date.year.toString()
            var month = (date.month+1).toString()
            var day = date.day.toString()

            if(month.toInt() < 10) month = "0$month"
            if(day.toInt() < 10) day = "0$day"

            var selectMsg = "${year}년 ${month}월 ${day}일"

            Toast.makeText(context, selectMsg, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}