package com.bruno13palhano.sleeptight.ui.babystatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bruno13palhano.sleeptight.R

class BabyStatusHeightAndWeightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_baby_status_height_and_weight, container, false)
    }
}