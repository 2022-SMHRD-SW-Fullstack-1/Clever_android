package com.example.clever.view.home.todo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.clever.R
import com.example.clever.adapter.TodoPageAdapter
import com.example.clever.adapter.TodoUniqueAdapter
import com.example.clever.databinding.ActivityTodoListBinding
import com.example.clever.decorator.*
import com.example.clever.decorator.calendar.*
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import com.example.clever.view.home.cal.*
import com.example.clever.view.profile.ProfileActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoListBinding

    lateinit var groupSp: SharedPreferences
    lateinit var group_seq: String

    lateinit var cate_seq: String
    lateinit var cate_name: String

    var selectDate: String = Time.getTime()

    val uniqueList = ArrayList<ToDoCompleteVO>()

    lateinit var adapter: TodoUniqueAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        groupSp = getSharedPreferences("groupInfo", Context.MODE_PRIVATE)
        group_seq = groupSp.getString("group_seq", "").toString()

        cate_seq = intent.getStringExtra("cate_seq").toString()
        cate_name = intent.getStringExtra("cate_name").toString()

        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // container
        // todoListRvUniq

        // template
        // template_todo_unique_rv

        // item
        getMemo()

        // adapter
        adapter = TodoUniqueAdapter(
            this@TodoListActivity,
            uniqueList
        )

        // 연결
        binding.todoListRvUniq.adapter = adapter
        binding.todoListRvUniq.layoutManager = GridLayoutManager(this@TodoListActivity, 1)

        // event

        binding.todoListTvGroupName.text = cate_name

        var todoTab1Fragment = TodoTab1Fragment()
        var todoTab2Fragment = TodoTab2Fragment()

        // 슬라이드 viewPager2 사용
        val pagerAdapter = TodoPageAdapter(this@TodoListActivity)
        pagerAdapter.addFragment(todoTab1Fragment)
        pagerAdapter.addFragment(todoTab2Fragment)
        binding.todoListVp.adapter = pagerAdapter

        // viewPager 와 TabLayout 연결
        TabLayoutMediator(binding.todoListTl, binding.todoListVp) { tab, position ->
            if (position == 0) {
                tab.text = "미완료"
            } else {
                tab.text = "완료"
            }
        }.attach()

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
        val weekdaysDecorator = WeekdaysDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val sundayDecorator = SundayDecorator()
        val calendarDecorator = CalendarDecorator()
        val selectDecorator = SelectDecorator(this@TodoListActivity)

        binding.todoCalendar.addDecorators(
            weekdaysDecorator,
            saturdayDecorator,
            sundayDecorator,
            calendarDecorator,
            selectDecorator
        )

        // header, 요일 한글로 변경
        binding.todoCalendar.setTitleFormatter { day -> "${day!!.year}년 ${day.month + 1}월" }
        binding.todoCalendar.setOnMonthChangedListener { widget, date ->
            var year = date.year.toString()
            var month = (date.month + 1).toString()

            if (month.toInt() < 10) month = "0$month"

            binding.todoCalendarYear.text = "${year}년 ${month}월"

            binding.todoCalendar.removeDecorators()
            binding.todoCalendar.addDecorators(
                weekdaysDecorator,
                saturdayDecorator,
                sundayDecorator,
                calendarDecorator,
                selectDecorator
            )
            binding.todoCalendar.addDecorator(OtherMonthDecorator(month.toInt() - 1))
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
            var year = date.year.toString()
            var month = (date.month + 1).toString()
            var day = date.day.toString()

            if (month.toInt() < 10) month = "0$month"
            if (day.toInt() < 10) day = "0$day"

            selectDate = "${year}-${month}-${day}"

            todoTab1Fragment.getTodoList(cate_seq, selectDate)
            todoTab2Fragment.getCmplList(cate_seq, selectDate)

            binding.todoCalendar.removeDecorators()
            binding.todoCalendar.addDecorators(
                weekdaysDecorator,
                saturdayDecorator,
                sundayDecorator,
                calendarDecorator,
                selectDecorator
            )
            binding.todoCalendar.addDecorator(OtherMonthDecorator(month.toInt() - 1))
        }

        binding.todoListVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    todoTab1Fragment.getTodoList(cate_seq, selectDate)
                } else {
                    todoTab2Fragment.getCmplList(cate_seq, selectDate)
                }
            }
        })

        var calView = false
        binding.todoListImgCalendar.setOnClickListener {
            if (calView) {
                binding.todoListClCalendar.visibility = View.GONE
                calView = false
            } else {
                binding.todoListClCalendar.visibility = View.VISIBLE
                calView = true
            }
        }
    }

    private fun getMemo() {
        val today = Time.getTime()
        RetrofitClient.api.getMemo(today, group_seq.toInt())
            .enqueue(object : Callback<List<ToDoCompleteVO>> {
                override fun onResponse(
                    call: Call<List<ToDoCompleteVO>>,
                    response: Response<List<ToDoCompleteVO>>
                ) {
                    val res = response.body()
                    if (res!!.isEmpty()) {
                        binding.todoListRvUniq.visibility = View.GONE
                    } else {
                        for (i in 0 until res!!.size) {
                            if (res[i].cmpl_memo != "" && res[i].cmpl_strange == "Y") uniqueList.add(
                                res[i]
                            )
                        }
                        if (uniqueList.size == 0) binding.todoListRvUniq.visibility = View.GONE
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

    }

}