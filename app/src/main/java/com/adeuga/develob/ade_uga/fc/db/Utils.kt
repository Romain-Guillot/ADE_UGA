package com.adeuga.develob.ade_uga.fc.db

import com.adeuga.develob.ade_uga.fc.TagManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utils {

    companion object {
        const val tagDelimiter = "&&"

        fun getStandartDate(d:Date) : String {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            return sdf.format(d)
        }

        @Deprecated(message = "Unique tag per task", level = DeprecationLevel.ERROR)
        fun serializeTags(tags:ArrayList<TagManager.Tag>) : String {
            var res = ""
            for(t:TagManager.Tag in tags) {
                res += this.tagDelimiter + t.name
            }
            return res
        }

        @Deprecated(message = "Unique tag per task", level = DeprecationLevel.ERROR)
        fun deserializeTags(tagsStr:String) : ArrayList<String> {
            return ArrayList<String>(tagsStr.split(tagDelimiter))
        }
    }
}
