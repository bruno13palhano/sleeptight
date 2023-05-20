package com.bruno13palhano.sleeptight.ui.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentListsBinding

class ListsFragment : Fragment() {
    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_lists, container, false)
        val view = binding.root

        binding.uiEvents = this

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun navigateToNotifications() {
        findNavController().navigate(
            ListsFragmentDirections.actionListsToNotifications())
    }

    fun navigateToNaps() {
        findNavController().navigate(
            ListsFragmentDirections.actionListsToNaps())
    }
}