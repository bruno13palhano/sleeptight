package com.bruno13palhano.sleeptight.ui.lists.babystatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentCommonListBinding
import com.bruno13palhano.sleeptight.ui.util.CommonListView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyStatusListFragment : Fragment(), CommonListView {
    private var _binding: FragmentCommonListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BabyStatusListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_common_list, container, false)
        val view = binding.root

        val adapter = BabyStatusAdapter {
            onListItemClick(it)
        }
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.babyStatusList.collect {
                    adapter.submitList(it)
                }
            }
        }

        binding.addButton.setOnClickListener {
            onAddItemClick()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onListItemClick(itemId: Long) {
        findNavController().navigate(
            BabyStatusListFragmentDirections.actionStatusListToBabyStatus(itemId))
    }

    override fun onAddItemClick() {
        findNavController().navigate(
            BabyStatusListFragmentDirections.actionStatusListToBabyStatusTitleAndDate())
    }
}