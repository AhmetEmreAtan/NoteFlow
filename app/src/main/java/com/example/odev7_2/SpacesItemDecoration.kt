package com.example.odev7_2

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(
    private val verticalSpace: Int,
    private val horizontalSpace: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontalSpace
        outRect.right = horizontalSpace
        outRect.top = verticalSpace


        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = verticalSpace
        }
    }
}

