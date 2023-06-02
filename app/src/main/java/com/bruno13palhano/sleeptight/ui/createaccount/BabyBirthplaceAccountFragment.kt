package com.bruno13palhano.sleeptight.ui.createaccount

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
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyBirthplaceAccountBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyBirthplaceAccountFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentBabyBirthplaceAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateAccountViewModel by activityViewModels()
    private lateinit var inputMethodManager: InputMethodManager
    private var isBirthplaceNotEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_birthplace_account, container, false)
        val view = binding.root

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isBirthplaceNotEmpty.collect {
                    isBirthplaceNotEmpty = it
                    setButtonVisibility()
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.birthplace.requestFocus()
        inputMethodManager.showSoftInput(binding.birthplace, InputMethodManager.SHOW_IMPLICIT)
        binding.birthplace.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                if (isBirthplaceNotEmpty) {
                    navigateToBirth()
                }
            }

            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToBirth() {
        findNavController().navigate(
            BabyBirthplaceAccountFragmentDirections.actionBabyBirthplaceToBabyBirth())
    }

    override fun setButtonVisibility() {
        if (isBirthplaceNotEmpty) {
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