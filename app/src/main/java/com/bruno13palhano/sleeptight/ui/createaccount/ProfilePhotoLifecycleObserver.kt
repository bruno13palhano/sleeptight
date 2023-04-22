package com.bruno13palhano.sleeptight.ui.createaccount

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ProfilePhotoLifecycleObserver(
    private val registry: ActivityResultRegistry,
    private val contentResolver: ContentResolver,
    private val photoListener: PhotoListener
) : DefaultLifecycleObserver {
    lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("photo_listener_key", owner,
            ActivityResultContracts.GetContent()) { uri ->
            try {
                uri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmapPhoto: Bitmap = MediaStore.Images.Media
                            .getBitmap(contentResolver, uri)
                        photoListener.onSuccess(bitmapPhoto)
                    } else {
                        val source = ImageDecoder.createSource(contentResolver, uri)
                        val bitmapPhoto = ImageDecoder.decodeBitmap(source)
                        photoListener.onSuccess(bitmapPhoto)
                    }
                }
            } catch (ignored: Exception) {
                photoListener.onFail()
            }
        }
    }

    fun selectImage() {
        getContent.launch("image/*")
    }
}

interface PhotoListener {
    fun onSuccess(bitmap: Bitmap)
    fun onFail()
}