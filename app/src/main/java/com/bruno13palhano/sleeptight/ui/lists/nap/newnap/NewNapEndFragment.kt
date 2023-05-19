package com.bruno13palhano.sleeptight.ui.lists.nap.newnap

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
import com.bruno13palhano.sleeptight.databinding.FragmentNewNapEndBinding
import com.bruno13palhano.sleeptight.ui.lists.newnap.NewNapEndFragmentDirections
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.launch

class NewNapEndFragment : Fragment() {
    private var _binding: FragmentNewNapEndBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNapViewModel by activityViewModels()

    private lateinit var timePicker: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_nap_end, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.endTime.collect {
                    setTimePicker(it)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun insertNap() {
        findNavController().navigate(
            NewNapEndFragmentDirections.actionEndToNaps()
        )
        viewModel.insertNap()
    }

    fun onTimeClick() {
        if (!timePicker.isAdded)
            timePicker.show(requireParentFragment().parentFragmentManager, "end time dialog")
    }

    private fun setTimePicker(time: Long) {
        timePicker = TimePickerUtil.prepareTimePicker(time)
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setEndTime(timePicker.hour, timePicker.minute)
        }
    }
}