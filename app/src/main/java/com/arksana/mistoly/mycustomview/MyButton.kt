package com.arksana.mistoly.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.arksana.mistoly.R

class MyButton : AppCompatButton {

    private lateinit var enabledBg: Drawable
    private lateinit var disabledBg: Drawable

    private var buttonTextColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBg else disabledBg
        setTextColor(buttonTextColor)
        textSize = 14f
        gravity = Gravity.CENTER
    }

    private fun init() {
        buttonTextColor = ContextCompat.getColor(context, R.color.black)
        enabledBg = ContextCompat.getDrawable(context, R.drawable.my_bg_button) as Drawable
        disabledBg = ContextCompat.getDrawable(context, R.drawable.my_bg_button_disable) as Drawable
    }
}