package com.bruno13palhano.sleeptight.ui.babystatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyStatusBinding

class BabyStatusFragment : Fragment() {
    private var _binding: FragmentBabyStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_status, container, false)
        val view = binding.root

        binding.uiEvents = this

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}