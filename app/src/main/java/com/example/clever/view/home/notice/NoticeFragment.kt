package com.example.clever.view.home.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.adapter.NoticeFragmentAdapter
import com.example.clever.databinding.FragmentNoticeBinding
import com.example.clever.decorator.other.MainRvDecorator
import com.example.clever.model.CategoryVO
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    lateinit var group_seq: String

    // item
    private val categoryList = ArrayList<CategoryVO>()

    // Adapter
    lateinit var adapter: NoticeFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)

        group_seq = activity?.intent?.getStringExtra("group_seq").toString()

        // container
        // binding.noticeCategoryRv

        // template
        // template_notice_category_rv.xml

        // item
        getCategory()

        // adapter
        adapter = NoticeFragmentAdapter(
            requireContext(),
            categoryList
        )

        // adapter, container 연결
        binding.noticeCategoryRv.adapter = adapter
        binding.noticeCategoryRv.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.noticeCategoryRv.addItemDecoration(MainRvDecorator(16))

        // event 처리

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategory() {
        val req = CategoryVO(group_seq.toInt(), "Notice")
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
                TODO("Not yet implemented")
            }
        })
    }

}