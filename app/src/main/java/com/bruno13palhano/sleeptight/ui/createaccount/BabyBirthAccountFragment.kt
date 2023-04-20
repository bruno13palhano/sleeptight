package com.bruno13palhano.sleeptight.ui.createaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyBirthAccountBinding

class BabyBirthAccountFragment : Fragment() {
    private var _binding: FragmentBabyBirthAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_birth_account, container, false)
        val view = binding.root

        binding.uiEvents = this

        return view
    }

    fun navigateToHome() {
        findNavController().navigate(
            BabyBirthAccountFragmentDirections.actionBabyBirthToHome())
    }
}