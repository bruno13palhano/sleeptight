package com.bruno13palhano.sleeptight.ui.settings

import android.graphics.Bitmap
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
import coil.load
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentSettingsBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import com.bruno13palhano.sleeptight.ui.createaccount.PhotoListener
import com.bruno13palhano.sleeptight.ui.createaccount.ProfilePhotoLifecycleObserver
import com.bruno13palhano.sleeptight.ui.util.TimePickerUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var photoObserver: ProfilePhotoLifecycleObserver
    private var isUserDataNotEmpty = false
    private var isEditable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)
        val view = binding.root

        (activity as MainActivity).supportActionBar?.title = getString(R.string.settings_label)

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        photoObserver = ProfilePhotoLifecycleObserver(
            registry = requireActivity().activityResultRegistry,
            contentResolver = requireContext().contentResolver,
            photoListener = object : PhotoListener {
                override fun onSuccess(bitmap: Bitmap, uri: String) {
                    viewModel.setBabyPhoto(bitmap, uri)
                }

                override fun onFail() {

                }
            }
        )
        lifecycle.addObserver(photoObserver)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isUserdataNotEmpty.collect {
                        isUserDataNotEmpty = it.isUserDataNotEmpty
                        isEditable = it.isEditable
                        setButtonVisibility()
                        setEditProfileVisibility()
                    }
                }
                launch {
                    viewModel.photoUi.collect {
                        binding.photo.load(it)
                    }
                }
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
                        viewModel.activeEditable()
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

    fun onImageClick() {
        photoObserver.selectImage()
    }

    fun onDateClick() {
        if (!datePicker.isAdded)
            datePicker.show(parentFragmentManager.beginTransaction(), "birth_date")
    }

    fun onTimeClick() {
        if (!timePicker.isAdded)
            timePicker.show(parentFragmentManager.beginTransaction(), "birth_time")
    }

    private fun setDatePicker(date: Long) {
        datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setSelection(date)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.sateBirthDate(it)
        }
    }

    private fun setTimePicker(time: Long) {
        timePicker = TimePickerUtil.prepareTimePicker(time)
        timePicker.addOnPositiveButtonClickListener {
            viewModel.setBirthTime(timePicker.hour, timePicker.minute)
        }
    }

    fun onDoneClick() {
        viewModel.updateUserValues()
    }

    private fun setEditProfileVisibility() {
        if (isEditable) {
            enableEditProfile()
        } else {
            disableEditProfile()
        }
    }

    private fun enableEditProfile() {
        binding.babyName.isEnabled = true
        binding.birthplace.isEnabled = true
        binding.date.isEnabled = true
        binding.time.isEnabled= true
        binding.height.isEnabled = true
        binding.weight.isEnabled = true
    }

    private fun disableEditProfile() {
        binding.babyName.isEnabled = false
        binding.birthplace.isEnabled = false
        binding.date.isEnabled = false
        binding.time.isEnabled = false
        binding.height.isEnabled = false
        binding.weight.isEnabled = false
    }

    override fun setButtonVisibility() {
        if (isUserDataNotEmpty && isEditable) {
            enableButton()
        } else {
            disableButton()
        }
    }

    override fun enableButton() {
        binding.settingsDone.visibility = VISIBLE
    }

    override fun disableButton() {
        binding.settingsDone.visibility = GONE
    }
}