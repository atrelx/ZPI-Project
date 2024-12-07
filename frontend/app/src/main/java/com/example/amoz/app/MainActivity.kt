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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.amoz.R
import com.example.amoz.interfaces.SignInDelegate
import com.example.amoz.ui.AppMainScaffold
import com.example.amoz.ui.components.CustomDialogWindow
import com.example.amoz.ui.screens.Screens
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
        authenticationViewModel.setSignInDelegate(this)

        enableEdgeToEdge()

        setContent {
            AmozApplicationTheme {
                val navController = rememberNavController()
                var showInvitationDialog by remember { mutableStateOf(false) }

                AppMainScaffold(
                    navigationController = navController,
                    onUserAuthorizationCheck = {
                        if (!authenticationViewModel.isSignedIn()) {
                            Log.d("MainActivity", "User is not signed in.")
                            navController.navigate(Screens.Entry.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            Log.d("MainActivity", "User is signed in.")
                            userViewModel.isRegistered(navController)
                        }
                    }
                )

                deeplink?.let {
                    if (showInvitationDialog){
                        CustomDialogWindow(
                            title = stringResource(id = R.string.company_invitation_window_title),
                            text = stringResource(id = R.string.company_invitation_window_text),
                            onDismiss = { showInvitationDialog = false },
                            onAccept = { handleDeepLink(it, true)
                                       showInvitationDialog = false },
                            onReject = { handleDeepLink(it, false)
                                       showInvitationDialog = false },
                        )
                    }
                }
            }
        }
    }

    private fun handleDeepLink(deepLink: Uri, isAccepted: Boolean) {
        val token = deepLink.getQueryParameter("token")

        if (token != null) {
            Log.d("DeepLink", "Received token: $token")
            employeeViewModel.manageInvitation(token, isAccepted)
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


