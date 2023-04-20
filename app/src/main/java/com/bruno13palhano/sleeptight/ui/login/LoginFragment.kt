package com.bruno13palhano.sleeptight.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_login, container, false)
        val view = binding.root

        binding.uiEvnets = this

        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(null)

        return view
    }

    fun navigateToHome() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginToHome())
    }

    fun navigateToCreateAccount() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginToCreateAccount())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }
}