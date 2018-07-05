package ikakus.com.tbilisinav.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.v4.content.ContextCompat
import android.view.View

class DrawableHelper {
    companion object {
        fun setDrawableColor(context: Context, view: View, colorInt: Int) {
            val background = view.background
            when (background) {
                is ShapeDrawable -> background.paint.color = ContextCompat.getColor(context, colorInt)
                is GradientDrawable -> background.setColor(ContextCompat.getColor(context, colorInt))
                is ColorDrawable -> background.color = ContextCompat.getColor(context, colorInt)
            }
        }
    }
}