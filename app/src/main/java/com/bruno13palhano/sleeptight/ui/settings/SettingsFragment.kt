package com.bruno13palhano.sleeptight.ui.settings

import android.icu.util.Calendar
import android.os.Bundle
import android.util.TypedValue
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
import coil.load
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentSettingsBinding
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.babyNameUi.collect {
                        binding.babyName.text = it
                    }
                }
                launch {
                    viewModel.photoUi.collect {
                        binding.photo.load(it)
                    }
                }
                launch {
                    viewModel.isEditable.collect { isEditable ->
                        binding.birthDateLabel.setOnClickListener {
                            if (isEditable) {
                                setDatePicker()
                                datePicker.show(
                                    parentFragmentManager.beginTransaction(),
                                    "birth_date"
                                )
                            }
                        }

                        binding.timeLayout.setOnClickListener {
                            if (isEditable) {
                                setTimePicker()
                                timePicker.show(
                                    parentFragmentManager.beginTransaction(),
                                    "birth_time"
                                )
                            }
                        }

                        binding.localLayout.setOnClickListener {
                            if (isEditable) {
                                val editLocalDialog = EditLocalDialog(
                                    object : EditLocalDialog.EditDialogListener {
                                        override fun onDialogPositiveClick(newValue: String) {
                                            viewModel.localUi.value = newValue
                                        }
                                    },
                                    getString(R.string.new_local_label),
                                    R.drawable.baseline_location_on_24
                                )
                                editLocalDialog.show(
                                    requireParentFragment().parentFragmentManager,
                                    "edit_local"
                                )
                            }
                        }

                        binding.heightLayout.setOnClickListener {
                            if (isEditable) {
                                val editHeightDialog = EditFloatingInputDialog(
                                    object : EditFloatingInputDialog.EditDialogListener {
                                        override fun onDialogPositiveClick(newValue: Float) {
                                            viewModel.setHeight(newValue)
                                        }
                                    },
                                    getString(R.string.new_height_label),
                                    R.drawable.baseline_square_foot_24
                                )
                                editHeightDialog.show(
                                    requireParentFragment().parentFragmentManager,
                                    "edit_height"
                                )
                            }
                        }

                        binding.weightLayout.setOnClickListener {
                            if (isEditable) {
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
                                    "edit_weight"
                                )
                            }
                        }
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
                menuInflater.inflate(R.menu.settings_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.edit_profile -> {
                        enableViews()
                        viewModel.setEditable(true)
                        true
                    }
                    R.id.share_profile -> {
                        true
                    }
                    R.id.logout -> {
                        logout()
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

    private fun logout() {
        viewModel.logout()
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsToHome())
    }

    private fun setDatePicker() {
        datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.sateBirthDate(it)
        }
    }

    private fun setTimePicker() {
        val calendar = Calendar.getInstance()
        timePicker = TimePickerUtil.prepareTimePicker(calendar.timeInMillis)
        timePicker.addOnPositiveButtonClickListener {
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute
            viewModel.setBirthTime(calendar.timeInMillis)
        }
    }

    fun onDoneClick() {
        disableViews()
        viewModel.setEditable(false)
        viewModel.updateUserValues()
    }

    private fun enableViews() {
        binding.settingsDone.visibility = VISIBLE
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr
            .colorOnSecondary, typedValue, true)
        setDividersColor(typedValue.data)
    }

    private fun disableViews() {
        binding.settingsDone.visibility = GONE
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr
            .divider, typedValue, true)
        setDividersColor(typedValue.data)
    }

    private fun setDividersColor(color: Int) {
        binding.materialDivider.setBackgroundColor(color)
        binding.materialDivider2.setBackgroundColor(color)
        binding.materialDivider3.setBackgroundColor(color)
        binding.materialDivider4.setBackgroundColor(color)
        binding.materialDivider5.setBackgroundColor(color)
        binding.materialDivider6.setBackgroundColor(color)
        binding.materialDivider7.setBackgroundColor(color)
    }
}