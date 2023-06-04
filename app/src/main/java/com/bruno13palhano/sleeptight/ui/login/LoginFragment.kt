package com.bruno13palhano.sleeptight.ui.login

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentLoginBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var inputMethodManager: InputMethodManager
    private var isEmailAndPasswordNotEmpty = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_login, container, false)
        val view = binding.root

        binding.uiEvnets = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(null)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStatus.collect {
                    launch {
                        viewModel.isEmailAndPasswordNotEmpty.collect {
                            isEmailAndPasswordNotEmpty = it
                            setButtonVisibility()
                        }
                    }
                    launch {
                        when (it) {
                            LoginViewModel.LoginStatus.Loading -> {
                                onLoginLoading()
                            }

                            LoginViewModel.LoginStatus.Success -> {
                                onLoginSuccess()
                            }

                            LoginViewModel.LoginStatus.Error -> {
                                onLoginError()
                            }

                            LoginViewModel.LoginStatus.Default -> {

                            }
                        }
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            try {
                (activity as MainActivity).hideBottomNavigation()
            } catch (ignored: Exception) {}
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(200)
            binding.email.requestFocus()
            inputMethodManager.showSoftInput(binding.email, InputMethodManager.SHOW_IMPLICIT)
            binding.email.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    binding.password.requestFocus()
                }

                false
            }
        }

        binding.password.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                if (isEmailAndPasswordNotEmpty) {
                    onLoginClick()
                }
            }

            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onLoginSuccess() {
        navigateToHome()
    }

    private fun onLoginError() {
        binding.loginProgress.visibility = GONE
        Toast.makeText(requireContext(), getString(R.string.login_error_label),
            Toast.LENGTH_SHORT).show()
    }

    private fun onLoginLoading() {
        binding.loginProgress.visibility = VISIBLE
    }

    fun onLoginClick() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.login(email, password)
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

    override fun setButtonVisibility() {
        if (isEmailAndPasswordNotEmpty) {
            enableButton()
        } else {
            disableButton()
        }
    }

    override fun enableButton() {
        binding.done.visibility = VISIBLE
    }

    override fun disableButton() {
        binding.done.visibility = GONE
    }
}