package com.mgok.conglystore.usecases.user

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetFireBaseUserUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke() = auth.currentUser

}