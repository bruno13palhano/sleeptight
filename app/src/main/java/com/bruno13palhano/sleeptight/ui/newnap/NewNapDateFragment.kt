package com.bruno13palhano.sleeptight.ui.newnap

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNewNapDateBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewNapDateFragment : Fragment() {
    private var _binding: FragmentNewNapDateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNapViewModel by activityViewModels()

    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_nap_date, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.date.collect {
                    setDatePicker(it)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToStart() {
        findNavController().navigate(
            NewNapDateFragmentDirections.actionDateToStart())
    }

    fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(requireParentFragment().parentFragmentManager, "date dialog")
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
            viewModel.setDate(
                year = calendar[Calendar.YEAR],
                month = calendar[Calendar.MONTH],
                day = calendar[Calendar.DAY_OF_MONTH]
            )
        }
    }
}