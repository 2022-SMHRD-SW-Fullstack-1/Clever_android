package com.example.clever.utils

import java.text.SimpleDateFormat
import java.util.*

class Time {
    companion object {
        fun getTime(): String {
            val currentTime = Calendar.getInstance().time // 현재시간 추출
            return SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentTime) // 형식, 위치 지정
        }

        fun getHours(): String{
            val currentTime = Calendar.getInstance().time // 현재시간 추출
            return SimpleDateFormat("HH:mm", Locale.KOREA).format(currentTime) // 형식, 위치 지정
        }
    }
}