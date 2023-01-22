package com.example.clever.decorator.calendar

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class AttendanceDecorator(val color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {

    private val dates: HashSet<CalendarDay>

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, color))
    }

    init {
        this.dates = HashSet(dates)
    }

}
