package com.bruno13palhano.sleeptight.ui.createaccount

import android.icu.util.Calendar
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
import com.bruno13palhano.sleeptight.databinding.FragmentBabyBirthAccountBinding
import com.bruno13palhano.sleeptight.ui.settings.EditFloatingInputDialog
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyBirthAccountFragment : Fragment() {
    private var _binding: FragmentBabyBirthAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateAccountViewModel by activityViewModels()

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_birth_account, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.date.collect {
                        setDatePicker(it)
                    }
                }
                launch {
                    viewModel.time.collect {
                        setTimePicker(it)
                    }
                }
                launch {
                    viewModel.height.collect {

                    }
                }
                launch {
                    viewModel.weight.collect {

                    }
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToHome() {
        findNavController().navigate(
            BabyBirthAccountFragmentDirections.actionBabyBirthToHome())
        viewModel.insertUser()
    }

    fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(requireParentFragment().parentFragmentManager, "login_date_dialog")
    }

    private fun setDatePicker(date: Long) {
        datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(date)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(it)
        }
    }

    fun onTimeClick() {
        if (!timePicker.isAdded)
            timePicker.show(requireParentFragment().parentFragmentManager, "login_time_dialog")
    }

    private fun setTimePicker(time: Long) {
        timePicker = TimePickerUtil.prepareTimePicker(time)
        timePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute

            viewModel.setTime(calendar.timeInMillis)
        }
    }

    fun onHeightClick() {
        val editHeightDialog = EditFloatingInputDialog(
            object : EditFloatingInputDialog.EditDialogListener {
                override fun onDialogPositiveClick(newValue: Float) {
                    viewModel.setHeight(newValue)
                }
            },
            getString(R.string.birth_height_label),
            R.drawable.baseline_square_foot_24
        )
        editHeightDialog.show(
            requireParentFragment().parentFragmentManager,
            "login_height_dialog"
        )
    }

    fun onWeightClick() {
        val editWeightDialog = EditFloatingInputDialog(
            object : EditFloatingInputDialog.EditDialogListener {
                override fun onDialogPositiveClick(newValue: Float) {
                    viewModel.setWeight(newValue)
                }
            },
            getString(R.string.new_weight_label),
            R.drawable.baseline_balance_24
        )
        editWeightDialog.show(
            requireParentFragment().parentFragmentManager,
            "login_weight_dialog"
        )
    }
}