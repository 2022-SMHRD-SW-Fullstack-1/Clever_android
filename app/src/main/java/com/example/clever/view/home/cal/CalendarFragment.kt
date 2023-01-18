package com.example.clever.view.home.cal

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.clever.R
import com.example.clever.databinding.FragmentCalendarBinding
import com.example.clever.decorator.*
import com.example.clever.decorator.calendar.*
import com.example.clever.model.AttendanceVO
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    val workList = ArrayList<AttendanceVO>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // container
        // todoCalendar

        // template
        // template_cal_rv

        // item

        // adapter

        // adapter, container 연결

        // event 처리

        val current = LocalDate.now()
        val formatterY = current.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
        val formatterD = current.format(DateTimeFormatter.ofPattern("yyyy.MM.DD"))

        // 초기화시 오늘 날짜 선택, 연월 숨기기
        binding.calendarCv.selectedDate = CalendarDay.today()
        binding.calendarTvYear.text = formatterY
        binding.calendarTvDate.text = formatterD
        binding.calendarCv.topbarVisible = false

        // 달력 요일별 색, 선택시 색
        val weekdaysDecorator = WeekdaysDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val sundayDecorator = SundayDecorator()
        val calendarDecorator = CalendarDecorator()
        val selectDecorator = SelectDecorator(requireActivity())

        binding.calendarCv.addDecorators(
            weekdaysDecorator,
            saturdayDecorator,
            sundayDecorator,
            calendarDecorator,
            selectDecorator
        )

        // header, 요일 한글로 변경
        binding.calendarCv.setTitleFormatter { day -> "${day!!.year}년 ${day.month + 1}월" }
        binding.calendarCv.setOnMonthChangedListener { widget, date ->
            var year = date.year.toString()
            var month = (date.month + 1).toString()

            if (month.toInt() < 10) month = "0$month"

            binding.calendarTvYear.text = "${year}년 ${month}월"

            binding.calendarCv.removeDecorators()
            binding.calendarCv.addDecorators(
                weekdaysDecorator,
                saturdayDecorator,
                sundayDecorator,
                calendarDecorator,
                selectDecorator
            )
            binding.calendarCv.addDecorator(OtherMonthDecorator(month.toInt() - 1))
        }
        binding.calendarCv.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)));


        // 주간, 월간 변경
        binding.calendarTvWeek.setOnClickListener {
            binding.calendarTvMonth.setTextColor(Color.parseColor("#B3B3B3"))
            binding.calendarTvWeek.setTextColor(Color.parseColor("#000000"))
            binding.calendarCv.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit()
        }
        binding.calendarTvMonth.setOnClickListener {
            binding.calendarTvWeek.setTextColor(Color.parseColor("#B3B3B3"))
            binding.calendarTvMonth.setTextColor(Color.parseColor("#000000"))
            binding.calendarCv.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit()
        }

        // 날짜 클릭 이벤트
        binding.calendarCv.setOnDateChangedListener { widget, date, selected ->
            var year = date.year.toString()
            var month = (date.month + 1).toString()
            var day = date.day.toString()

            if (month.toInt() < 10) month = "0$month"
            if (day.toInt() < 10) day = "0$day"

            var selectDate = "${year}.${month}.${day}"
            binding.calendarTvDate.text = selectDate

            binding.calendarCv.removeDecorators()
            binding.calendarCv.addDecorators(
                weekdaysDecorator,
                saturdayDecorator,
                sundayDecorator,
                calendarDecorator,
                selectDecorator
            )
            binding.calendarCv.addDecorator(OtherMonthDecorator(month.toInt() - 1))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}