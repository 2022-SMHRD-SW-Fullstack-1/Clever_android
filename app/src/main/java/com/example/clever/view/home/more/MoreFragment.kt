package com.example.clever.view.home.more

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.example.clever.R
import com.example.clever.databinding.FragmentMoreBinding
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.notice.InquiryActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    lateinit var group_seq: String
    lateinit var group_name: String

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    private lateinit var memName: String

    var state by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)

        group_seq = activity?.intent?.getStringExtra("group_seq").toString()
        group_name = activity?.intent?.getStringExtra("group_name").toString()

        loginSp = requireContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name", "").toString()

        groupInfo()

        binding.moreFClMember.setOnClickListener {
            val intent = Intent(requireContext(), MoreMemberActivity::class.java)
            intent.putExtra("group_seq", group_seq)
            startActivity(intent)
        }

        binding.moreLlInquiry.setOnClickListener {
            val intent = Intent(requireContext(), InquiryActivity::class.java)
            startActivity(intent)
        }

        binding.moreLlGroupOut.setOnClickListener {
            val layoutInflater = LayoutInflater.from(requireContext())
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

            val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setView(view).create()

            val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
            val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
            val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
            val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

            if (state) {
                alertTvTitle.text = "그룹 삭제하기"
                alertTvContent.text = "${group_name} 을/를 삭제하시겠습니까 ?"
            } else {
                alertTvTitle.text = "그룹 나가기"
                alertTvContent.text = "${group_name} 을/를 나가시겠습니까 ?"
            }

            alertBtnOk.setOnClickListener {
                loginSp = requireContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                memId = loginSp.getString("mem_id", "").toString()

                val req = GroupVO(group_seq.toInt(), memId)
                if (state) {
                    RetrofitClient.api.groupDelete(req).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }else{
                    RetrofitClient.api.groupOut(req).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }

            }

            alertBtnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun groupInfo() {
        val groupInfo = GroupVO(group_seq.toInt())

        RetrofitClient.api.groupInfo(groupInfo).enqueue(object : Callback<GroupVO> {
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()
                if (res!!.mem_id == memId) {
                    binding.moreFTvState.text = "그룹장"
                    binding.moreTvGroupOut.text = "그룹 삭제하기"
                    state = true
                } else {
                    binding.moreFTvState.text = "멤버"
                    state = false
                }
                joinDate(state)
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                t.localizedMessage?.let { Log.d("more fragment", it) }
            }
        })
    }

    private fun joinDate(state: Boolean) {
        val groupInfo = GroupVO(group_seq.toInt(), memId)

        RetrofitClient.api.joinDate(groupInfo).enqueue(object : Callback<GroupVO> {
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()
                val resultDt = res!!.join_dt.toString()
                val year = resultDt.substring(0, 4)
                val month = resultDt.substring(5, 7)
                val day = resultDt.substring(8, 10)

                if (state) {
                    binding.moreFTvDate.text = "${year}년 ${month}월 ${day}일 생성"
                } else {
                    binding.moreFTvDate.text = "${year}년 ${month}월 ${day}일 합류"
                }
                groupMemCount()
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun groupMemCount() {
        val groupInfo = GroupVO(group_seq.toInt())

        RetrofitClient.api.groupMem(groupInfo).enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(call: Call<List<GroupVO>>, response: Response<List<GroupVO>>) {
                val res = response.body()
                binding.moreFtvCount.text = res!!.size.toString()
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                t.localizedMessage?.let { Log.d("more fragment", it) }
            }
        })
    }
}

