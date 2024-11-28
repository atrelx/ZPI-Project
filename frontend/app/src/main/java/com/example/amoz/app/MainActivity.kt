
package com.example.amoz.app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.amoz.interfaces.SignInDelegate
import com.example.amoz.ui.AppMainScaffold
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.AuthenticationViewModel
import com.example.amoz.view_models.EmployeeViewModel
import com.example.amoz.view_models.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), SignInDelegate {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val employeeViewModel: EmployeeViewModel by viewModels()
    private var completion: (() -> Unit)? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val deeplink = intent.data

        deeplink?.let {
            handleDeepLink(it)
        }

        authenticationViewModel.setSignInDelegate(this)
        userViewModel.isRegistered()

        enableEdgeToEdge()

        setContent {
            AmozApplicationTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppMainScaffold()
                }
            }
        }
    }

    private fun handleDeepLink(deepLink: Uri) {
        val token = deepLink.getQueryParameter("token")

        if (token != null) {
            Log.d("DeepLink", "Received token: $token")
            employeeViewModel.acceptInvitation(token)
        } else {
            Log.e("DeepLink", "Token is missing in the deep link.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            authenticationViewModel.handleSignInResult(task, completion = completion)
        }
    }

    override fun startSignInActivityForResult(completion: (() -> Unit)?) {
        val signInIntent = authenticationViewModel.getSignInIntent(this)
        this.completion = completion
        startActivityForResult(signInIntent, 9001)
    }
}


