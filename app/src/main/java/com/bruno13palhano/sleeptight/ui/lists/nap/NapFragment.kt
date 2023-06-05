package com.bruno13palhano.sleeptight.ui.lists.nap

import android.graphics.Rect
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNapBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import com.bruno13palhano.sleeptight.ui.lists.CommonItemActions
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NapFragment : Fragment(), CommonItemActions, ButtonItemVisibility {
    private var _binding: FragmentNapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NapViewModel by viewModels()
    private var napId: Long = 0L
    private var isTitleNotEmpty = false

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var startTimePicker: MaterialTimePicker
    private lateinit var endTimePicker: MaterialTimePicker

    private val listener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        binding.root.getWindowVisibleDisplayFrame(rect)
        val screenHeight = binding.root.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > screenHeight * 0.15) {
            onKeypadOpen()
        } else {
            onKeypadClose()
        }
    }

    private fun onKeypadOpen() {
        (activity as MainActivity).hideBottomNavigation()
        disableButton()
    }

    private fun onKeypadClose() {
        (activity as MainActivity).showBottomNavigation()
        enableButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_nap, container, false)
        val view = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.uiEvents = this
        binding.viewModel = viewModel

        napId = NapFragmentArgs.fromBundle(requireArguments()).napId
        viewModel.getNap(napId)

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
                launch {
                    viewModel.startTime.collect {
                        setStartTimePicker(it)
                    }
                }
                launch {
                    viewModel.endTime.collect {
                        setEndTimePicker(it)
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
                        onDeleteItem()
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

    override fun onResume() {
        super.onResume()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    override fun onUpdateItem() {
        findNavController().navigateUp()
        viewModel.updateNap(napId)
    }

    override fun onDeleteItem() {
        viewModel.deleteNapById(napId)
        findNavController().navigateUp()
    }

    fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(requireParentFragment().parentFragmentManager, "dialog date")
    }

    fun onStartTimeClick() {
        if (!startTimePicker.isAdded)
            startTimePicker.show(requireParentFragment().parentFragmentManager, "dialog start time")
    }

    fun onEndTimeClick() {
        if (!endTimePicker.isAdded)
            endTimePicker.show(requireParentFragment().parentFragmentManager, "dialog end time")
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

    private fun setStartTimePicker(time: Long) {
        startTimePicker = TimePickerUtil.prepareTimePicker(time)
        startTimePicker.addOnPositiveButtonClickListener {
            viewModel.setStartTime(startTimePicker.hour, startTimePicker.minute)
        }
    }

    private fun setEndTimePicker(time: Long) {
        endTimePicker = TimePickerUtil.prepareTimePicker(time)
        endTimePicker.addOnPositiveButtonClickListener {
            viewModel.setEndTime(endTimePicker.hour, endTimePicker.minute)
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
        binding.done.visibility = VISIBLE
    }

    override fun disableButton() {
        binding.done.visibility = GONE
    }
}