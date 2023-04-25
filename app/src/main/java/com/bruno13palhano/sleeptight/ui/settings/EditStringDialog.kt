package com.bruno13palhano.sleeptight.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.bruno13palhano.sleeptight.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditStringDialog(
    private val listener: EditDialogListener,
    private val hint: String,
    private val drawable: Int
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_edit_profile, null)
            val editLayout = view.findViewById<TextInputLayout>(R.id.edit_layout)
            editLayout.hint = hint
            val edit = view.findViewById<TextInputEditText>(R.id.edit)
            val icon = view.findViewById<ImageView>(R.id.edit_icon)
            icon.setImageDrawable(ResourcesCompat
                .getDrawable(resources, drawable, null))

            builder.setView(view)
                .setPositiveButton(getString(R.string.ok_label)) { _, _ ->
                    listener.onDialogPositiveClick(edit.text.toString())
                }
                .setNegativeButton(getString(R.string.cancel_label)) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    interface EditDialogListener {
        fun onDialogPositiveClick(newValue: String)
    }
}