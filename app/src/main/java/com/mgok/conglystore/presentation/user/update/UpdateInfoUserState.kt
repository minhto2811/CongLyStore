package com.mgok.conglystore.presentation.user.update

import android.net.Uri

data class UpdateInfoUserState(
    val loading: Boolean = false,
    val error: String? = null,
    val url: Uri? = null,
)



