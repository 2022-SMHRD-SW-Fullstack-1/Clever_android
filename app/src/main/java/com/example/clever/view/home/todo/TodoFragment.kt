package com.example.clever.view.home.todo

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.TodoFragmentAdapter
import com.example.clever.databinding.FragmentTodoBinding
import com.example.clever.decorator.other.TodoFRvDecorator
import com.example.clever.model.AttendanceVO
import com.example.clever.model.CategoryVO
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    lateinit var group_seq: String

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    // item
    private val categoryList = ArrayList<CategoryVO>()

    // Adapter
    lateinit var adapter: TodoFragmentAdapter

    var attStart = false
    var attEnd = false
    var att_seq: Int = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        group_seq = activity?.intent?.getStringExtra("group_seq").toString()
        loginSp = requireContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        // container
        // binding.todoCategoryRv

        // template
        // template_todo_category_rv.xml

        // item
        getCategory()

        getTodayAtt()

        // adapter
        adapter = TodoFragmentAdapter(
            requireContext(),
            categoryList
        )

        // adapter, container 연결
        binding.todoCategoryRv.adapter = adapter
        binding.todoCategoryRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.todoCategoryRv.addItemDecoration(TodoFRvDecorator(16))

        // event 처리

        binding.btnAtt.isEnabled = false

        binding.btnAtt.setOnClickListener {
            att()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun att() {
        val req = Time.getHours()
        if (attStart) {
            RetrofitClient.api.attStart(att_seq, req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if (res.toString() == "1") {
                        binding.btnAtt.isEnabled = false
                        Toast.makeText(requireContext(), "출근처리가 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.localizedMessage?.let { Log.d("resAtt", it) }
                }
            })
        }
        if (attEnd) {
            val layoutInflater = LayoutInflater.from(requireContext())
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

            val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(view).create()

            val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
            val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
            val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
            val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

            alertTvTitle.text = "퇴근"
            alertTvContent.text = "퇴근처리를 하시겠습니까?"

            alertBtnOk.setOnClickListener {
                RetrofitClient.api.attEnd(att_seq, req).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        if (res.toString() == "1") {
                            binding.btnAtt.isEnabled = false
                            Toast.makeText(requireContext(), "퇴근처리가 되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("resAtt", it) }
                    }
                })
            }
            alertBtnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun getCategory() {
        val req = CategoryVO(group_seq.toInt(), "ToDo")
        RetrofitClient.api.getCategory(req).enqueue(object : Callback<List<CategoryVO>> {
            override fun onResponse(
                call: Call<List<CategoryVO>>,
                response: Response<List<CategoryVO>>
            ) {
                val res = response.body()
                for (i in 0 until res!!.size) {
                    categoryList.add(res[i])
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<CategoryVO>>, t: Throwable) {
                t.localizedMessage?.let { Log.d("resAtt", it) }
            }
        })
    }

    fun getTodayAtt() {
        RetrofitClient.api.getTodayAtt(group_seq.toInt(), memId, Time.getTime())
            .enqueue(object : Callback<AttendanceVO> {
                override fun onResponse(
                    call: Call<AttendanceVO>,
                    response: Response<AttendanceVO>
                ) {
                    val res = response.body()
                    att_seq = res?.att_seq!!.toInt()
                    if (res?.att_real_start_time == null && res?.att_real_end_time == null) {
                        attStart = true
                        attEnd = false
                        binding.btnAtt.isEnabled = true
                        binding.btnAtt.setBackgroundResource(R.drawable.button_main)
                    } else if (res?.att_real_start_time != null && res?.att_real_end_time == null) {
                        attStart = false
                        attEnd = true
                        binding.btnAtt.isEnabled = true
                        binding.btnAtt.setBackgroundResource(R.drawable.button_main)
                        binding.btnAtt.text = "퇴근"
                    } else {
                        binding.btnAtt.isEnabled = false
                        attEnd = false
                        attStart = false
                    }
                }

                override fun onFailure(call: Call<AttendanceVO>, t: Throwable) {
                    t.localizedMessage?.let { Log.d("resAtt", it) }

                }
            })
    }

}