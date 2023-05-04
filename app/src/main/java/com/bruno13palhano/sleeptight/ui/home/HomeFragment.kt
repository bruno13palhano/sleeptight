package com.bruno13palhano.sleeptight.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root

        binding.uiEvents = this

        if (!viewModel.isUserAuthenticated()) {
            navigateToLogin()
        }

        val adapter = BabyStatusAdapter {
            navigateToBabyStatus(it)
        }
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBabyStatus.collect {
                    adapter.submitList(it)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLogin() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToLogin())
    }

    fun navigateToNewBabyStatus() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToBabyStatusTitleAndDate())
    }

    private fun navigateToBabyStatus(id: Long) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToBabyStatus(id))
    }
}