package com.bruno13palhano.sleeptight.ui.lists.babystatus

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyStatusBinding
import com.bruno13palhano.sleeptight.ui.babystatus.BabyStatusViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyStatusFragment : Fragment() {
    private var _binding: FragmentBabyStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BabyStatusViewModel by viewModels()
    private var babyStatusId: Long = 0L
    private lateinit var datePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_status, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        babyStatusId = BabyStatusFragmentArgs.fromBundle(requireArguments()).statusId
        viewModel.getBabyStatus(babyStatusId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.common_item_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete -> {
                        deleteBabyStatus()
                        true
                    }
                    R.id.share -> {
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteBabyStatus() {
        viewModel.deleteBabyStatus(babyStatusId)
        findNavController().navigateUp()
    }

    fun updateBabyStatus() {
        findNavController().navigateUp()
        viewModel.updateBabyStatus(babyStatusId)
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
}