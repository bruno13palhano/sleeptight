package com.bruno13palhano.sleeptight.ui.newnap

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
import com.bruno13palhano.sleeptight.databinding.FragmentNewNapObservationBinding
import kotlinx.coroutines.launch

class NewNapObservationFragment : Fragment() {
    private var _binding: FragmentNewNapObservationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewNapViewModel by activityViewModels()

    private var date = 0L
    private var startTime = 0L
    private var endTime = 0L
    private var observation = ""

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
                launch {
                    viewModel.date.collect {
                        date = it
                    }
                }
                launch {
                    viewModel.startTime.collect {
                        startTime = it
                    }
                }
                launch {
                    viewModel.endTime.collect {
                        endTime = it
                    }
                }
                launch {
                    viewModel.observation.collect {
                        observation = it
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
            NewNapObservationFragmentDirections.actionObservationToDate())
    }
}