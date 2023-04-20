package com.bruno13palhano.sleeptight.ui.createaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment() {
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_account, container, false)
        val view = binding.root

        binding.uiEvents = this

        return view
    }

    fun navigateToBabyName() {
        findNavController().navigate(
            CreateAccountFragmentDirections.actionCreateAccountToBabyName())
    }

    fun navigateToLogin() {
        findNavController().navigateUp()
    }
}