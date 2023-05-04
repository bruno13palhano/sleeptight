package com.bruno13palhano.sleeptight.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.model.BabyStatus
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

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

        }
        binding.list.adapter = adapter

        val status = listOf(
            BabyStatus(
                id = 1L,
                title = "Test 1",
                date = 1L,
                height = 43.3F,
                weight = 2.23F,
            ),
            BabyStatus(
                id = 2L,
                title = "Test 2",
                date = 2L,
                height = 45.2F,
                weight = 2.43F,
            ),
            BabyStatus(
                id = 3L,
                title = "Test 3",
                date = 3L,
                height = 47.1F,
                weight = 2.98F,
            ),
            BabyStatus(
                id = 4L,
                title = "Test 4",
                date = 4L,
                height = 53.3F,
                weight = 3.23F,
            ),
            BabyStatus(
                id = 5L,
                title = "Test 5",
                date = 5L,
                height = 56.3F,
                weight = 3.63F,
            ),
            BabyStatus(
                id = 6L,
                title = "Test 6",
                date = 6L,
                height = 57.3F,
                weight = 3.73F,
            ),
            BabyStatus(
                id = 7L,
                title = "Test 7",
                date = 7L,
                height = 61.5F,
                weight = 4.1F,
            ),
            BabyStatus(
                id = 8L,
                title = "Test 8",
                date = 8L,
                height = 64.1F,
                weight = 4.43F,
            ),
        )

        adapter.submitList(status)

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
}