package com.arksana.mistoly.mycustomview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.MyFormFieldBinding


class MyFormField : LinearLayout {
    private lateinit var formFieldBinding: MyFormFieldBinding

    private var errorText: String = ""
    private var lengthMin: Int = -1
    private var isRequired: Boolean = false
    private var label: String = ""

    var validator: ((text: String) -> String)? = null
    var afterTextChanged: ((text: String) -> Unit)? = null

    var text: Editable?
        get() = formFieldBinding.myEditText.text
        set(value) {
            formFieldBinding.myEditText.text = value
        }

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet? = null) {
        formFieldBinding = MyFormFieldBinding.inflate(LayoutInflater.from(context), this, true)

        val array = getContext().obtainStyledAttributes(attrs, R.styleable.MyFormField)

        try {
            val hintText = array.getString(R.styleable.MyFormField_hint)
            label = array.getString(R.styleable.MyFormField_label) ?: ""
            val isPassword = array.getBoolean(R.styleable.MyFormField_isPassword, false)
            val isLabelHide = array.getBoolean(R.styleable.MyFormField_hideLabel, false)

            isRequired = array.getBoolean(R.styleable.MyFormField_isRequired, false)
            lengthMin = array.getInteger(R.styleable.MyFormField_length, -1)

            formFieldBinding.myEditText.isPassword = isPassword
            formFieldBinding.myEditText.setPasswordTextVisible(false)

            if (isLabelHide) formFieldBinding.labelText.height = 0
            formFieldBinding.myEditText.hint = hintText
            formFieldBinding.labelText.text = label

            updateErrorText()

            formFieldBinding.myEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    validateText()
                    afterTextChanged?.invoke(s.toString())
                }
            })
        } finally {
            array.recycle()
        }
    }

    private fun updateErrorText() {
        if (errorText.isEmpty()) {
            formFieldBinding.errorText.visibility = View.GONE
        } else {
            formFieldBinding.errorText.visibility = VISIBLE
            formFieldBinding.errorText.text = errorText
        }
    }

    fun validateText() {
        errorText = validateError()
        updateErrorText()
    }

    private fun validateError(): String {
        return when {
            isRequired && formFieldBinding.myEditText.text.toString().isEmpty() -> {
                "${label.replaceFirstChar { it.uppercase() }} ${context.getString(R.string.isRequired)}"
            }
            lengthMin != -1 && (formFieldBinding.myEditText.text?.length ?: 0) < lengthMin -> {
                "${label.replaceFirstChar { it.uppercase() }} ${context.getString(R.string.needAtLeast)} $lengthMin ${
                    context.getString(R.string.character)
                }"
            }
            validator != null -> {
                validator?.invoke((formFieldBinding.myEditText.text.toString())) ?: ""
            }
            else -> {
                ""
            }
        }
    }
}