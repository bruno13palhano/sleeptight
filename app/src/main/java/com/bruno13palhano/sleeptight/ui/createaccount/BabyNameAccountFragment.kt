package com.bruno13palhano.sleeptight.ui.createaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyNameAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BabyNameAccountFragment : Fragment() {
    private var _binding: FragmentBabyNameAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateAccountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_name_account, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return view
    }

    fun navigateToBirthplace() {
        findNavController().navigate(
            BabyNameAccountFragmentDirections.actionBabyNameToBayBirthplace())
    }
}