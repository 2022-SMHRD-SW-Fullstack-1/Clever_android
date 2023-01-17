package com.example.clever.view.home.todo

import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.adapter.ToDoTab1Adapter
import com.example.clever.databinding.FragmentTodoTab1Binding
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoTab1Fragment : Fragment() {

    private var _binding: FragmentTodoTab1Binding? = null
    private val binding get() = _binding!!

    lateinit var cate_seq: String

    // item
    val todoList = ArrayList<ToDoVo>()

    // adapter
    lateinit var adapter: ToDoTab1Adapter

    lateinit var selectDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab1Binding.inflate(inflater, container, false)

        cate_seq = activity?.intent?.getStringExtra("cate_seq")!!

        // container
        // todoTab1Rv

        // template
        // fragment_todo_tab1

        // item
        getTodolist()

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

    fun selectDate(res: String) {
        selectDate = res
    }

    private fun getTodolist() {
        Log.d("tab 1", "getTodolist")
        val req = ToDoVo(cate_seq.toInt())
        RetrofitClient.api.getToDoList(req).enqueue(object : Callback<List<ToDoVo>> {
            override fun onResponse(call: Call<List<ToDoVo>>, response: Response<List<ToDoVo>>) {
                val res = response.body()
                for (i in 0 until res!!.size) {
                    todoList.add(res[i])
                }
                getCmpl()
            }

            override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCmpl() {
        Log.d("tab 1", "getCmpl")
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
                        for (i in 0 until todoList.size) {
                            if(todoList[i].todo_seq == resSeq){
                                todoList.removeAt(i)
                                break
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