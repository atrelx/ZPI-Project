
package com.example.amoz.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.amoz.interfaces.SignInDelegate
import com.example.amoz.ui.theme.AmozApplicationTheme
import com.example.amoz.view_models.AuthenticationViewModel
import com.example.amoz.view_models.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer

@AndroidEntryPoint
class MainActivity : ComponentActivity(), SignInDelegate {
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var completion: (() -> Unit)? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        authenticationViewModel.setSignInDelegate(this)

        enableEdgeToEdge()

        setContent {
            AmozApplicationTheme {
//                Box(modifier = Modifier.fillMaxSize()) {
//                    AppMainScaffold()
//                }
                Box(modifier = Modifier.fillMaxSize()) {
                    AppMainScaffold()

                    Button(
                        onClick = { startSignInActivityForResult() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(48.dp, 64.dp)
                    ) {
                        Text(text = "Google")
                    }
                    Button(
                        onClick = { userViewModel.registerUser() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(48.dp, 64.dp)
                    ) {
                        Text(text = "Test")
                    }
                }
            }
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


