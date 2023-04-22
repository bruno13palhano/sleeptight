package com.bruno13palhano.sleeptight.ui.createaccount

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyPhotoAccountBinding
import kotlinx.coroutines.launch

class BabyPhotoAccountFragment : Fragment() {
    private var _binding: FragmentBabyPhotoAccountBinding? = null
    private val binding get() = _binding!!
    private val  viewModel: CreateAccountViewModel by activityViewModels()
    private lateinit var photoObserver: ProfilePhotoLifecycleObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_baby_photo_account, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        photoObserver = ProfilePhotoLifecycleObserver(
            registry = requireActivity().activityResultRegistry,
            contentResolver = requireContext().contentResolver,
            photoListener = object : PhotoListener {
                override fun onSuccess(bitmap: Bitmap) {
                    viewModel.setPhoto(bitmap)
                }

                override fun onFail() {

                }
            }
        )
        lifecycle.addObserver(photoObserver)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collect {
                    binding.photo.setImageBitmap(it)
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToBabyName() {
        findNavController().navigate(
            BabyPhotoAccountFragmentDirections.actionBabyPhotoToBabyName())
    }

    fun onPhotoClick() {
        photoObserver.selectImage()
    }
}