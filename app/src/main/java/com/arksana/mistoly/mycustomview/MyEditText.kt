package com.arksana.mistoly.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.arksana.mistoly.R

class MyEditText : AppCompatEditText, OnTouchListener {

    private lateinit var endButtonImage: Drawable
    var isPassword: Boolean = false
        set(value) {
            if (value) showButton() else hideButton()
            field = value
        }
    private var isTextShow: Boolean = false


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init(attrs: AttributeSet? = null) {
        endButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_close) as Drawable
        setOnTouchListener(this)
        val array = context.obtainStyledAttributes(attrs, R.styleable.MyFormField)

        addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isPassword) if (s.toString().isNotEmpty()) showButton() else hideButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        array.recycle()
    }


    private fun showButton() {
        setButtonDrawables(endOfTheText = endButtonImage)
    }

    private fun hideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    private fun toggleTextPasswordVisible() {
        isTextShow = !isTextShow
        setPasswordTextVisible(isTextShow)
    }

    fun setPasswordTextVisible(isTextShow: Boolean) {
        if (!isPassword) return

        this.isTextShow = isTextShow
        var drawable: Drawable? = null
        if (isTextShow) {
            drawable = ContextCompat.getDrawable(
                context, R.drawable.ic_outline_remove_red_eye
            ) as Drawable
            transformationMethod = null
        } else {
            drawable = ContextCompat.getDrawable(
                context, R.drawable.ic_baseline_remove_red_eye
            ) as Drawable
            transformationMethod = PasswordTransformationMethod.getInstance()
        }

        DrawableCompat.setTint(
            drawable, resources.getColor(R.color.secondaryColor, context.theme)
        )
        setButtonDrawables(endOfTheText = drawable)
        setSelection(length())
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (endButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - endButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        when {
                            text != null -> if (!isPassword) text?.clear()
                        }
                        if (isPassword) toggleTextPasswordVisible()
                        else hideButton()
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }
}