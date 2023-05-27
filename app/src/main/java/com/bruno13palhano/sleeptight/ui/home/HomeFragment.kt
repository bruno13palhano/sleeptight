package com.bruno13palhano.sleeptight.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        if (!viewModel.isUserAuthenticated()) {
            navigateToLogin()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profileImage.collect {
                        binding.profileImage.load(it)
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.share_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
                        shareLastInformation()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLogin() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToLogin())
    }
    
    private fun shareLastInformation() {
        val text = getStatusText()

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/*"
            putExtra(Intent.EXTRA_TITLE, getString(R.string.home_last_status))
            putExtra(Intent.EXTRA_TEXT, text)
        }, null)
        this@HomeFragment.requireContext().startActivity(share)
    }

    private fun getStatusText(): String {
        return if (binding.napTitle.text != "" && binding.notificationTitle.text != "") {
            "${getString(R.string.baby_name_label)}: ${binding.babyName.text}\n" +
                    "${getString(R.string.birth_height_label)}: ${binding.height.text}\n" +
                    "${getString(R.string.birth_weight_label)}: ${binding.weight.text}\n" +
                    "${getString(R.string.last_nap_date_label)}: ${binding.napDate.text}\n" +
                    "${getString(R.string.last_nap_sleeping_time_label)}: ${binding.napSleepingTime.text}\n" +
                    "${getString(R.string.last_notification_label)}: ${binding.notificationTitle.text}\n" +
                    "${getString(R.string.last_notification_date_label)}: ${binding.notificationDate.text}"
        } else if (binding.napTitle.text != "") {
            "${getString(R.string.baby_name_label)}: ${binding.babyName.text}\n" +
                    "${getString(R.string.birth_height_label)}: ${binding.height.text}\n" +
                    "${getString(R.string.birth_weight_label)}: ${binding.weight.text}\n" +
                    "${getString(R.string.last_nap_date_label)}: ${binding.napDate.text}\n" +
                    "${getString(R.string.last_nap_sleeping_time_label)}: ${binding.napSleepingTime.text}\n"
        } else if (binding.notificationTitle.text != "") {
            "${getString(R.string.baby_name_label)}: ${binding.babyName.text}\n" +
                    "${getString(R.string.birth_height_label)}: ${binding.height.text}\n" +
                    "${getString(R.string.birth_weight_label)}: ${binding.weight.text}\n" +
                    "${getString(R.string.last_notification_label)}: ${binding.notificationTitle.text}\n" +
                    "${getString(R.string.last_notification_date_label)}: ${binding.notificationDate.text}"
        } else {
            "${getString(R.string.baby_name_label)}: ${binding.babyName.text}\n" +
            "${getString(R.string.birth_height_label)}: ${binding.height.text}\n" +
            "${getString(R.string.birth_weight_label)}: ${binding.weight.text}"
        }
    }
}