package com.arksana.mistoly.utils

import android.content.res.Resources
import android.util.Patterns
import com.arksana.mistoly.R
import java.util.regex.Pattern

class Validator {
    companion object {
        fun email(resources: Resources, string: String): String {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            return if (!pattern.matcher(string).matches()) {
                resources.getString(R.string.invalid_email)
            } else ""
        }
    }

}