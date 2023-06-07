package com.bruno13palhano.sleeptight.ui.lists.babystatus.newbabystatus

import android.content.Context
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
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNewBabyStatusHeightAndWeightBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewBabyStatusHeightAndWeightFragment : Fragment() {
    private var _binding: FragmentNewBabyStatusHeightAndWeightBinding? = null
    private val binding get() = _binding!!
    private lateinit var inputMethodManager: InputMethodManager
    private val viewModel: NewBabyStatusViewModel by activityViewModels()
    private var isHeightAndTitleValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_baby_status_height_and_weight, container, false)
        val view = binding.root

        (activity as MainActivity).supportActionBar?.title = getString(R.string.baby_status_height_and_weight)

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.heightAndWeightValue.collect {
                        setButtonVisibility(it)
                        isHeightAndTitleValid = it
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.height.requestFocus()
        inputMethodManager.showSoftInput(binding.height, InputMethodManager.SHOW_IMPLICIT)
        binding.height.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.weight.requestFocus()
            }

            false
        }

        binding.weight.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                if (isHeightAndTitleValid) {
                    navigateToHome()
                }
            }

            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToHome() {
        findNavController().navigate(
            NewBabyStatusHeightAndWeightFragmentDirections.actionHeightAndWeightToStatusList())
        viewModel.insertBabyStatus()
    }

    private fun setButtonVisibility(isHeightAndWeightValid: Boolean) {
        if (isHeightAndWeightValid) {
            enableDoneButton()
        } else {
            disableDoneButton()
        }
    }

    private fun enableDoneButton() {
        binding.done.visibility = VISIBLE
    }

    private fun disableDoneButton() {
        binding.done.visibility = GONE
    }
}