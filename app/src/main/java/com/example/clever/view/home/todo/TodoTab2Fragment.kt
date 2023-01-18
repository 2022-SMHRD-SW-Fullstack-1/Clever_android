package com.example.clever.view.home.todo

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

class TodoTab2Fragment : Fragment() {

    private var _binding: FragmentTodoTab2Binding? = null
    private val binding get() = _binding!!

    lateinit var cate_seq: String

    // item
    val cmplList = ArrayList<ToDoCompleteVO>()

    // adapter
    lateinit var adapter: ToDoTab2Adapter

    lateinit var selectDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoTab2Binding.inflate(inflater, container, false)

        cate_seq = activity?.intent?.getStringExtra("cate_seq")!!

        // container
        // todoTab2Rv

        // template
        // fragment_todo_tab2

        // item
        getCmplList()

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

    fun selectDate(res: String) {
        selectDate = res
    }

    private fun getCmplList() {
        val req = ToDoCompleteVO(cate_seq.toInt(), selectDate)
        RetrofitClient.api.getToDoComplete(req)
            .enqueue(object : Callback<List<ToDoCompleteVO>> {
                override fun onResponse(
                    call: Call<List<ToDoCompleteVO>>,
                    response: Response<List<ToDoCompleteVO>>
                ) {
                    cmplList.clear()
                    val res = response.body()
                    Log.d("tab2", res.toString())
                    for (i in 0 until res!!.size) {
                        val today = Time.getTime()
                        val date = res[i].cmpl_time
                        val dateSub = date!!.substring(0, 10)
                        if (today == dateSub) cmplList.add(res[i])
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

}