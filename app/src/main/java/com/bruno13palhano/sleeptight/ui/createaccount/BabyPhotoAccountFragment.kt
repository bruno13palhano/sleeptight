package com.bruno13palhano.sleeptight.ui.createaccount

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.bruno13palhano.sleeptight.MainActivity
import com.bruno13palhano.sleeptight.R
import com.bruno13palhano.sleeptight.databinding.FragmentBabyPhotoAccountBinding
import kotlinx.coroutines.launch

class BabyPhotoAccountFragment : Fragment() {
    private var _binding: FragmentBabyPhotoAccountBinding? = null
    private val binding get() = _binding!!
    private val  viewModel: CreateAccountViewModel by activityViewModels()
    private lateinit var photoObserver: ProfilePhotoLifecycleObserver

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
            .inflate(inflater, R.layout.fragment_baby_photo_account, container, false)
        val view = binding.root

        binding.uiEvents = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        photoObserver = ProfilePhotoLifecycleObserver(
            registry = requireActivity().activityResultRegistry,
            contentResolver = requireContext().contentResolver,
            photoListener = object : PhotoListener {
                override fun onSuccess(bitmap: Bitmap, uri: String) {
                    viewModel.setPhoto(bitmap, uri)
                }

                override fun onFail() {

                }
            }
        )
        lifecycle.addObserver(photoObserver)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photoUi.collect {
                    binding.photo.load(it)
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