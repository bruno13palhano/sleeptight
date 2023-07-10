package com.bruno13palhano.sleeptight.ui.screens.analytics

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class to get String resource
 *
 * User this class when you can't get a context reference
 * e.g. ViewModel classes.
 *
 * @property context the ApplicationContext
 */
@Singleton
class StringResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Gets a string from StringResource
     * @param stringId the id from this String Resource
     * @return a String related to this [stringId]
     */
    fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }
}