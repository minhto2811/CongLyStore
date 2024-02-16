package com.mgok.conglystore.presentation.user

import android.net.Uri
import com.mgok.conglystore.presentation.auth.ResultStatusState

data class UpdateInfoUserState(
    val status: ResultStatusState = ResultStatusState.Default,
    val error: String? = null,
    val uploadState: UploadState = UploadState()
)


data class UploadState(
    val status: ResultStatusState = ResultStatusState.Default,
    val url: Uri? = null,
    val error: String? = null
)


