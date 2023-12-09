package com.example.myday.food

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FoodDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("I am the title")
            ?.setPositiveButton("확인") {
                dialog, which -> // do something
            }
            ?.setNegativeButton("취소") { dialog, which -> // do something
            }
        return super.onCreateDialog(savedInstanceState)
        // https://developer.android.com/develop/ui/views/components/dialogs?hl=ko
    }
}