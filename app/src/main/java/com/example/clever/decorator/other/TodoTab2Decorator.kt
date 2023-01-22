package com.example.clever.decorator.other

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TodoTab2Decorator(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        with(outRect) {
            top = margin
            left = margin
            right = margin
            bottom = margin
        }
    }
}