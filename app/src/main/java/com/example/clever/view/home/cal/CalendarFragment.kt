package com.example.clever.view.home.cal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.CalendarAdapter
import com.example.clever.databinding.FragmentCalendarBinding
import com.example.clever.decorator.*
import com.example.clever.decorator.calendar.*
import com.example.clever.model.AttendanceVO
import com.example.clever.retrofit.RetrofitClient
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    lateinit var group_seq: String

    // 달력에 표시할 리스트
    val workList = ArrayList<AttendanceVO>()
    val calList = ArrayList<CalendarDay>()

    // 리사이클러뷰에 표시할 리스트
    val selectedList = ArrayList<AttendanceVO>()

    lateinit var selectedDate: String

    lateinit var adapter : CalendarAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        loginSp = requireContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        group_seq = activity?.intent?.getStringExtra("group_seq").toString()

        // container
        // todoCalendar

        // template
        // template_cal_rv

        // item
        getAttendance()

        // adapter
        adapter = CalendarAdapter(
            requireContext(),
            selectedList
        )

        // adapter, container 연결
        binding.calendarRvSchedule.adapter = adapter
        binding.calendarRvSchedule.layoutManager = GridLayoutManager(requireContext(), 1)

        // event 처리
        binding.calendarBtnSalary.setOnClickListener {
            val intent = Intent(requireContext(), CalculateActivity::class.java)
            intent.putExtra("mem_id", memId)
            intent.putExtra("group_seq", group_seq)
            requireContext().startActivity(intent)
        }

        val current = LocalDate.now()
        val formatterY = current.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
        val formatterD = current.format(DateTimeFormatter.ofPattern("yyyy.MM.DD"))
        selectedDate = current.format((DateTimeFormatter.ofPattern("yyyy-MM-DD")))

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
            selectDecorator,
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
            binding.calendarCv.addDecorator(AttendanceDecorator(Color.parseColor("#ff0000"), calList))
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
            selectedDate = "${year}-${month}-${day}"
            selectedList()

            binding.calendarCv.removeDecorators()
            binding.calendarCv.addDecorators(
                weekdaysDecorator,
                saturdayDecorator,
                sundayDecorator,
                calendarDecorator,
                selectDecorator
            )
            binding.calendarCv.addDecorator(AttendanceDecorator(Color.parseColor("#ff0000"), calList))
            binding.calendarCv.addDecorator(OtherMonthDecorator(month.toInt() - 1))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAttendance() {
        val req = AttendanceVO(memId, group_seq.toInt())
        RetrofitClient.api.getAttendance(req).enqueue(object : Callback<List<AttendanceVO>> {
            override fun onResponse(
                call: Call<List<AttendanceVO>>,
                response: Response<List<AttendanceVO>>
            ) {
                workList.clear()
                selectedList.clear()
                calList.clear()
                val res = response.body()
                for (i in 0 until res!!.size) {
                    workList.add(res[i])
                    val year = res[i].att_date?.substring(0, 4)
                    val month = res[i].att_date?.substring(5, 7)
                    val day = res[i].att_date?.substring(8, 10)
                    calList.add(CalendarDay(year!!.toInt(), month!!.toInt()-1, day!!.toInt()))
                    if (selectedDate == res[i].att_date!!.substring(0, 10)) {
                        selectedList.add(res[i])
                    }
                }
                binding.calendarCv.addDecorator(AttendanceDecorator(Color.parseColor("#ff0000"), calList))
            }

            override fun onFailure(call: Call<List<AttendanceVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun selectedList() {
        selectedList.clear()
        for (i in 0 until workList.size) {
            if (selectedDate == workList[i].att_date!!.substring(
                    0,
                    10
                )
            ) {
                selectedList.add(workList[i])
            }
        }
        adapter.notifyDataSetChanged()
    }
}