package com.bruno13palhano.sleeptight.ui.lists.nap.newnap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentNewNapObservationBinding
import com.bruno13palhano.sleeptight.ui.lists.newnap.NewNapObservationFragmentDirections
import kotlinx.coroutines.launch

class NewNapObservationFragment : Fragment() {
    private var _binding: FragmentNewNapObservationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNapViewModel by activityViewModels()

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.title.collect {
                    if (it != "") {
                        enableNextButton()
                    } else {
                        disableNextButton()
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

    fun navigateToDate() {
        findNavController().navigate(
            NewNapObservationFragmentDirections.actionObservationToDate()
        )
    }

    private fun enableNextButton() {
        binding.next.visibility = VISIBLE
    }

    private fun disableNextButton() {
        binding.next.visibility = GONE
    }
}