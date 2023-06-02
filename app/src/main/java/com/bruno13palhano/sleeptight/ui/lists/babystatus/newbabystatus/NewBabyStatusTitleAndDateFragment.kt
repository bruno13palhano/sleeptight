package com.bruno13palhano.sleeptight.ui.lists.babystatus.newbabystatus

import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNewBabyStatusTitleAndDateBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewBabyStatusTitleAndDateFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentNewBabyStatusTitleAndDateBinding? = null
    private val binding get() = _binding!!
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var datePicker: MaterialDatePicker<Long>
    private val viewModel: NewBabyStatusViewModel by activityViewModels()
    private var isTitleNotEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_baby_status_title_and_date, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isTitleNotEmpty.collect {
                        isTitleNotEmpty = it
                        setButtonVisibility()
                    }
                }
                launch {
                    viewModel.date.collect {
                        setDatePicker(it)
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.requestFocus()
        inputMethodManager.showSoftInput(binding.title, InputMethodManager.SHOW_IMPLICIT)
        binding.title.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                onDateClick()
            }

            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToHeightAndWeight() {
        findNavController().navigate(
            NewBabyStatusTitleAndDateFragmentDirections.actionTitleAndDateToHeightAndWeight())
    }

    fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(requireParentFragment().parentFragmentManager, "date_dialog")
    }

    private fun setDatePicker(date: Long) {
        datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(date)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it

            viewModel.setDate(calendar.timeInMillis)
        }
    }

    override fun setButtonVisibility() {
        if (isTitleNotEmpty) {
            enableButton()
        } else {
            disableButton()
        }
    }

    override fun enableButton() {
        binding.next.visibility = VISIBLE
    }

    override fun disableButton() {
        binding.next.visibility = GONE
    }
}