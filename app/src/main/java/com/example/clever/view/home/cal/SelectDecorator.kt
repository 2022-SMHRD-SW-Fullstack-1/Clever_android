package com.example.clever.view.home.cal

import android.app.Activity
import android.graphics.drawable.Drawable
import com.example.clever.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SelectDecorator(context: Activity?) : DayViewDecorator {

    private val drawable: Drawable?

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        if (drawable != null) {
            view?.setSelectionDrawable(drawable)
        }
    }

    init {
        drawable = context!!.getResources().getDrawable(R.drawable.calendar_select)
    }
}