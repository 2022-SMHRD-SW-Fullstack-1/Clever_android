package com.example.clever.view.home.todo

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.clever.R
import com.example.clever.databinding.ActivityTodoListBinding
import com.example.clever.view.home.cal.*
import com.example.clever.view.profile.ProfileActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoListImgBack.setOnClickListener {
            finish()
        }

        binding.todoListImgProfile.setOnClickListener {
            val intent = Intent(this@TodoListActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        val current = LocalDate.now()
        val formatterY = current.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))

        // 초기화시 오늘 날짜 선택, 연월 숨기기
        binding.todoCalendar.selectedDate = CalendarDay.today()
        binding.todoCalendarYear.text = formatterY
        binding.todoCalendar.topbarVisible = false

        // 달력 요일별 색, 선택시 색
        binding.todoCalendar.addDecorator(WeekdaysDecorator())
        binding.todoCalendar.addDecorator(SaturdayDecorator())
        binding.todoCalendar.addDecorator(SundayDecorator())
        binding.todoCalendar.addDecorator(CalendarDecorator())
        binding.todoCalendar.addDecorator(SelectDecorator(this@TodoListActivity))

        // header, 요일 한글로 변경
        binding.todoCalendar.setTitleFormatter { day -> "${day!!.year}년 ${day.month + 1}월" }
        binding.todoCalendar.setOnMonthChangedListener { widget, date ->
            var year = date.year.toString()
            var month = (date.month+1).toString()

            if(month.toInt()<10) month = "0$month"

            binding.todoCalendarYear.text = "${year}년 ${month}월"
        }
        binding.todoCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)));

        // 주간, 월간 변경
        binding.todoCalendarWeek.setOnClickListener {
            binding.todoCalendarMonth.setTextColor(Color.parseColor("#B3B3B3"))
            binding.todoCalendarWeek.setTextColor(Color.parseColor("#000000"))
            binding.todoCalendar.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit()
        }
        binding.todoCalendarMonth.setOnClickListener {
            binding.todoCalendarWeek.setTextColor(Color.parseColor("#B3B3B3"))
            binding.todoCalendarMonth.setTextColor(Color.parseColor("#000000"))
            binding.todoCalendar.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit()
        }

        // 날짜 클릭 이벤트
        binding.todoCalendar.setOnDateChangedListener { widget, date, selected ->

        }
    }

}