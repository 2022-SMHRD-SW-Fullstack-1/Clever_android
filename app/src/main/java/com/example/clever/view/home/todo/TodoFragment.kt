package com.example.clever.view.home.todo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clever.R
import com.example.clever.adapter.MainAdapter
import com.example.clever.adapter.TodoFragmentAdapter
import com.example.clever.databinding.FragmentNoticeBinding
import com.example.clever.databinding.FragmentTodoBinding
import com.example.clever.decorator.main.MainRvDecorator
import com.example.clever.decorator.main.TodoFRvDecorator
import com.example.clever.model.CategoryVO
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.notice.NoticeFolderInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    lateinit var groupSp: SharedPreferences
    lateinit var group_seq : String

    // item
    private val categoryList = ArrayList<CategoryVO>()

    // Adapter
    lateinit var adapter: TodoFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        groupSp = requireContext().getSharedPreferences("groupInfo", Context.MODE_PRIVATE)
        group_seq = groupSp.getString("group_seq", "").toString()

        // container
        // binding.todoCategoryRv

        // template
        // template_todo_category_rv.xml

        // item
        getCategory()

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategory(){
        val req = CategoryVO(group_seq.toInt(), "ToDo")
        RetrofitClient.api.getCategory(req).enqueue(object : Callback<List<CategoryVO>>{
            override fun onResponse(
                call: Call<List<CategoryVO>>,
                response: Response<List<CategoryVO>>
            ) {
                val res = response.body()
                for(i in 0 until res!!.size){
                    categoryList.add(res[i])
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<CategoryVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}