package com.bruno13palhano.sleeptight.ui.lists.babystatus

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
import com.bruno13palhano.sleeptight.databinding.FragmentBabyStatusListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyStatusListFragment : Fragment() {
    private var _binding: FragmentBabyStatusListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BabyStatusListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_status_list, container, false)
        val view = binding.root

        val adapter = BabyStatusAdapter {

        }
        binding.babyStatusList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.babyStatusList.collect {
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

    fun onAddClick() {
        findNavController().navigate(
            BabyStatusListFragmentDirections.actionStatusListToBabyStatusTitleAndDate())
    }
}