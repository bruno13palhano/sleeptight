package com.bruno13palhano.sleeptight.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onLoginClick() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.login(
            email = email,
            password = password,
            onSuccess = {
                navigateToHome()
            },
            onFail = {
                Toast.makeText(requireContext(), getString(R.string.login_error_label),
                    Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToHome() {
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