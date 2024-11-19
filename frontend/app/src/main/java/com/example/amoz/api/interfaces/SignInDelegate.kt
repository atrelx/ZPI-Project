package com.example.amoz.api.interfaces

interface SignInDelegate {
    fun startSignInActivityForResult(completion: (() -> Unit)? = null)
}