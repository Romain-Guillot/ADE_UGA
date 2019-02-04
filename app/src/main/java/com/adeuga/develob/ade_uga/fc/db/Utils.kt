package com.adeuga.develob.ade_uga.fc.db

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun getStandartDate(d:Date) : String {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            return sdf.format(d)
        }
    }
}
