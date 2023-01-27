package com.example.clever.view.home.todo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.adapter.ToDoTab2Adapter
import com.example.clever.databinding.FragmentTodoTab2Binding
import com.example.clever.decorator.other.TodoTab2Decorator
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class TodoTab2Fragment : Fragment() {

    private var _binding: FragmentTodoTab2Binding? = null
    private val binding get() = _binding!!

    // item
    val cmplList = ArrayList<ToDoCompleteVO>()

    // adapter
    private lateinit var adapter: ToDoTab2Adapter

    private var viewPoint = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab2Binding.inflate(inflater, container, false)

        // container
        // todoTab2Rv

        // template
        // fragment_todo_tab2

        // item
        // getCmplList()

        viewPoint = true
        // adapter
        adapter = ToDoTab2Adapter(
            requireContext(),
            cmplList
        )

        // adapter, container 연결
        binding.todoTab2Rv.adapter = adapter
        binding.todoTab2Rv.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.todoTab2Rv.addItemDecoration(TodoTab2Decorator(8))

        // event

        return binding.root
    }

    fun getCmplList(
        cate_seq: String,
        selectDate: String,
        cate_type: String,
        group_seq: Int,
        memId: String
    ) {
        val req = ToDoCompleteVO(cate_seq.toInt(), selectDate)
        if (cate_type == "Default") {
            RetrofitClient.api.getMyToDoComplete(group_seq, memId, selectDate)
                .enqueue(object : Callback<List<ToDoCompleteVO>> {
                    override fun onResponse(
                        call: Call<List<ToDoCompleteVO>>,
                        response: Response<List<ToDoCompleteVO>>
                    ) {
                        cmplList.clear()
                        val res = response.body()
                        for (i in 0 until res!!.size) {
                            cmplList.add(res[i])
                        }
                        if (viewPoint) adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            RetrofitClient.api.getToDoComplete(req)
                .enqueue(object : Callback<List<ToDoCompleteVO>> {
                    override fun onResponse(
                        call: Call<List<ToDoCompleteVO>>,
                        response: Response<List<ToDoCompleteVO>>
                    ) {
                        cmplList.clear()
                        val res = response.body()
                        for (i in 0 until res!!.size) {
                            val date = res[i].cmpl_time
                            val dateSub = date!!.substring(0, 10)
                            if (selectDate == dateSub) cmplList.add(res[i])
                        }
                        if (viewPoint) adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}