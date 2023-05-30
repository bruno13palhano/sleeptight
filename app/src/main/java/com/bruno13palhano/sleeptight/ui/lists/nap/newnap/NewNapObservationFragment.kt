package com.bruno13palhano.sleeptight.ui.lists.nap.newnap

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
import com.bruno13palhano.sleeptight.databinding.FragmentNewNapObservationBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import kotlinx.coroutines.launch

class NewNapObservationFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentNewNapObservationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNapViewModel by activityViewModels()
    private lateinit var inputMethodManager: InputMethodManager
    private var title = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_nap_observation, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.title.collect {
                    setButtonVisibility(it)
                    title = it
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newNapTitle.requestFocus()
        inputMethodManager.showSoftInput(binding.newNapTitle, InputMethodManager.SHOW_IMPLICIT)
        binding.newNapTitle.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.newNapObservation.requestFocus()
            }

            false
        }

        binding.newNapObservation.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                if (isTitleNotEmpty()) {
                    navigateToDate()
                }
            }

            false
        }
    }

    private fun isTitleNotEmpty()
        = title != ""

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToDate() {
        findNavController().navigate(
            NewNapObservationFragmentDirections.actionObservationToDate())
    }

    private fun setButtonVisibility(title: String) {
        if (title.trim() != "") {
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