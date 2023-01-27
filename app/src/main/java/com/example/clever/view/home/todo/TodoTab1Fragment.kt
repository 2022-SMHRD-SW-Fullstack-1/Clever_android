package com.example.clever.view.home.todo

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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

    lateinit var groupSp: SharedPreferences
    lateinit var group_seq: String
    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    // item
    var todoList = ArrayList<ToDoVo>()

    // adapter
    private lateinit var adapter: ToDoTab1Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab1Binding.inflate(inflater, container, false)

        groupSp = requireContext().getSharedPreferences("groupInfo", Context.MODE_PRIVATE)
        group_seq = groupSp.getString("group_seq", "").toString()
        loginSp = requireContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        // container
        // todoTab1Rv

        // template
        // fragment_todo_tab1

        // item
        // getTodoList

        // adapter
        adapter = ToDoTab1Adapter(
            requireContext(),
            todoList,
            onClickDeleteIcon = { //3. onBindViewHolder에서 listposition을 전달받고 이 함수를 실행하게 된다.
                deleteTask(it)      //deleteTask함수가 포지션값인 it을 받고 지운다.
            }
        )

        // adapter, container 연결
        binding.todoTab1Rv.adapter = adapter
        binding.todoTab1Rv.layoutManager = GridLayoutManager(requireContext(), 1)

        // event

        return binding.root
    }

    fun deleteTask(todo: ToDoVo) {
        todoList.remove(todo)
        adapter?.notifyDataSetChanged()
    }

    fun getTodoList(cate_seq: String, selectDate: String, cate_type: String) {
        val req = ToDoVo(cate_seq.toInt())

        if (cate_type == "Default") {
            RetrofitClient.api.getMyToDo(group_seq.toInt(), memId)
                .enqueue(object : Callback<List<ToDoVo>> {
                    override fun onResponse(
                        call: Call<List<ToDoVo>>,
                        response: Response<List<ToDoVo>>
                    ) {
                        todoList.clear()
                        val res = response.body()
                        for (i in 0 until res!!.size) {
                            todoList.add(res[i])

                            todoList[i].select_day = selectDate
                        }

                        // nature
                        if (selectDate != null) {
                            getCmpl(cate_seq, selectDate, cate_type)
                        }
                    }

                    override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            RetrofitClient.api.getToDoList(req).enqueue(object : Callback<List<ToDoVo>> {
                override fun onResponse(
                    call: Call<List<ToDoVo>>,
                    response: Response<List<ToDoVo>>
                ) {
                    todoList.clear()
                    val res = response.body()

                    for (i in 0 until res!!.size) {
                        todoList.add(res[i])
                        todoList[i].select_day = selectDate
                    }
                    // nature
                    getCmpl(cate_seq, selectDate, cate_type)
                }

                override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }


    fun getCmpl(cate_seq: String, selectDate: String, cate_type: String) {
        val req = ToDoCompleteVO(cate_seq.toInt(), selectDate)
        if (cate_type == "Default") {
            Log.d("todoDefault 2", todoList.toString())
            RetrofitClient.api.getMyToDoComplete(group_seq.toInt(), memId, selectDate)
                .enqueue(object : Callback<List<ToDoCompleteVO>> {
                    override fun onResponse(
                        call: Call<List<ToDoCompleteVO>>,
                        response: Response<List<ToDoCompleteVO>>
                    ) {
                        val res = response.body()
                        for (i in 0 until res!!.size) {
                            val resSeq = res[i].todo_seq
                            for (i in 0 until todoList.size) {
                                if (todoList[i].todo_seq == resSeq) {
                                    todoList.removeAt(i)
                                    adapter.notifyItemRemoved(i)
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
        } else {
            RetrofitClient.api.getToDoComplete(req)
                .enqueue(object : Callback<List<ToDoCompleteVO>> {
                    @SuppressLint("NotifyDataSetChanged")
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
                                        Log.d("todoList fun getcmpl", todoList.size.toString())
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

}


