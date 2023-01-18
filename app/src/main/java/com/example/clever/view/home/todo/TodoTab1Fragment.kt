package com.example.clever.view.home.todo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.adapter.ToDoTab1Adapter
import com.example.clever.databinding.FragmentTodoTab1Binding
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoTab1Fragment : Fragment() {

    private var _binding: FragmentTodoTab1Binding? = null
    private val binding get() = _binding!!

    // item
    val todoList = ArrayList<ToDoVo>()

    // adapter
    private lateinit var adapter: ToDoTab1Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab1Binding.inflate(inflater, container, false)

        // container
        // todoTab1Rv

        // template
        // fragment_todo_tab1

        // item
        // getTodoList

        // adapter
        adapter = ToDoTab1Adapter(
            requireContext(),
            todoList
        )

        // adapter, container 연결
        binding.todoTab1Rv.adapter = adapter
        binding.todoTab1Rv.layoutManager = GridLayoutManager(requireContext(), 1)

        // event

        return binding.root
    }

    fun getTodoList(cate_seq:String, selectDate: String) {
        val req = ToDoVo(cate_seq.toInt())
        RetrofitClient.api.getToDoList(req).enqueue(object : Callback<List<ToDoVo>> {
            override fun onResponse(call: Call<List<ToDoVo>>, response: Response<List<ToDoVo>>) {
                todoList.clear()
                val res = response.body()

                for (i in 0 until res!!.size) {
                    todoList.add(res[i])
                    Log.d("todoList $i", todoList[i].toString())
                    todoList[i].select_day = selectDate
                    Log.d("todoList 수정 $i", todoList[i].toString())
                }
                getCmpl(cate_seq, selectDate)
            }

            override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCmpl(cate_seq: String, selectDate: String) {
        val req = ToDoCompleteVO(cate_seq.toInt(), selectDate)
        RetrofitClient.api.getToDoComplete(req)
            .enqueue(object : Callback<List<ToDoCompleteVO>> {
                override fun onResponse(
                    call: Call<List<ToDoCompleteVO>>,
                    response: Response<List<ToDoCompleteVO>>
                ) {
                    val res = response.body()
                    for (i in 0 until res!!.size) {
                        val resSeq = res[i].todo_seq
                        val resTime = res[i].cmpl_time
                        val timeSub = resTime?.substring(0, 10)
                        for (i in 0 until todoList.size) {
                            if (todoList[i].todo_seq == resSeq) {
                                if (timeSub == selectDate) {
                                    todoList.removeAt(i)
                                    break
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }
}