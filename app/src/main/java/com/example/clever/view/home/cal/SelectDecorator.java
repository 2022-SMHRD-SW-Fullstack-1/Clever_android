package com.example.clever.view.home.cal;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.example.clever.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class SelectDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public SelectDecorator(Activity context) {
        this.drawable = context.getResources().getDrawable(R.drawable.calendar_select);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
