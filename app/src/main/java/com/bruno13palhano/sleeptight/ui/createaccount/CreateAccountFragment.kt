package com.bruno13palhano.sleeptight.ui.createaccount

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentCreateAccountBinding
import com.bruno13palhano.sleeptight.ui.ButtonItemVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateAccountFragment : Fragment(), ButtonItemVisibility {
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateAccountViewModel by activityViewModels()
    private lateinit var inputMethodManager: InputMethodManager
    private var isUserDataNotEmpty = false

    private val listener = ViewTreeObserver.OnGlobalLayoutListener {
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_create_account, container, false)
        val view = binding.root

        (activity as MainActivity).supportActionBar?.title = getString(R.string.create_account_label)

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        inputMethodManager = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isUserDataNotEmpty.collect {
                        isUserDataNotEmpty = it
                        setButtonVisibility()
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.username.requestFocus()
        inputMethodManager.showSoftInput(binding.username, InputMethodManager.SHOW_IMPLICIT)
        binding.username.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.email.requestFocus()
            }

            false
        }

        binding.email.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                binding.password.requestFocus()
            }

            false
        }

        binding.password.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                if (isUserDataNotEmpty) {
                    navigateToBabyName()
                }
            }

            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToBabyName() {
        findNavController().navigate(
            CreateAccountFragmentDirections.actionCreateAccountToBabyPhoto()
        )
    }

    fun navigateToLogin() {
        findNavController().navigateUp()
    }

    override fun setButtonVisibility() {
        if (isUserDataNotEmpty) {
            enableButton()
        } else {
            disableButton()
        }
    }

    override fun enableButton() {
        binding.next.visibility = VISIBLE
    }

    override fun disableButton() {
        binding.next.visibility = GONE
    }
}