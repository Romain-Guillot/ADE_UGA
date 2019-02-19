package com.adeuga.develob.ade_uga.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import com.adeuga.develob.ade_uga.fc.TagManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


/**
 * ChipGroup adapter
 * Set all tag chips in [chipGroup] from list of tag (available in TagManager compagnion object)
 */
class TagChipsAdapter(private val chipGroup:ChipGroup, private val ctx:Context) {

    init {
        setChips()
    }


    /**
     * Init all chips from tag list (see TagManager compagnion object)
     */
    private fun setChips() {
        this.chipGroup.removeAllViewsInLayout()
        for((i, tag:TagManager.Tag) in TagManager.tags.withIndex()) {
            val chip = Chip(ctx)
            chip.text = tag.name
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(tag.color))
            this.chipGroup.addView(chip)
            chip.id = i
        }
    }


    /**
     * Return the tag checked (or a default tag if none is selected)
     */
    fun getCheckedTag() : TagManager.Tag {
        return when(this.chipGroup.checkedChipId >= 0 && this.chipGroup.checkedChipId <= TagManager.tags.size) {
            true -> TagManager.tags[this.chipGroup.checkedChipId]
            false -> TagManager.unknownTag
        }
    }
}