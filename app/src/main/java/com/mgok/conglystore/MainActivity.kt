package com.mgok.conglystore

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.mgok.conglystore.presentation.auth.AuthScreen
import com.mgok.conglystore.presentation.auth.GoogleAuthUiClient
import com.mgok.conglystore.presentation.auth.user.UpdateInfoUserScreen
import com.mgok.conglystore.presentation.home.HomeScreen
import com.mgok.conglystore.presentation.home.SettingsScreen
import com.mgok.conglystore.presentation.splash.SplashScreen
import com.mgok.conglystore.ui.theme.CongLyStoreTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CongLyStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = getString(R.string.route_splash)
                    ) {
                        composable(getString(R.string.route_splash)) {
                            SplashScreen() { route ->
                                navController.navigate(route)
                            }
                        }

                        composable(getString(R.string.route_auth)) {
                            AuthScreen { route ->
                               lifecycleScope.launch(Dispatchers.Main){
                                   navController.navigate(route)
                               }
                            }
                        }
                        composable(getString(R.string.route_update_user)) {
                            UpdateInfoUserScreen {
                                navController.popBackStack()
                            }
                        }
                        composable(getString(R.string.route_home)) {
                            HomeScreen(onClickSettings = { navController.navigate(getString(R.string.route_settings)) })
                        }

                        composable(getString(R.string.route_settings)) {
                            SettingsScreen(
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

