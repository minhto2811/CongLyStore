package com.mgok.conglystore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mgok.conglystore.presentation.auth.AuthScreen
import com.mgok.conglystore.presentation.coffee.CoffeeViewModel
import com.mgok.conglystore.presentation.coffee.detail.DetailProductScreen
import com.mgok.conglystore.presentation.coffee.product.CoffeeScreen
import com.mgok.conglystore.presentation.coffee.search.SearchCoffeeScreen
import com.mgok.conglystore.presentation.coffee.type.CoffeeTypeScreen
import com.mgok.conglystore.presentation.home.HomeScreen
import com.mgok.conglystore.presentation.map.MapViewModel
import com.mgok.conglystore.presentation.splash.SplashScreen
import com.mgok.conglystore.presentation.user.SettingsScreen
import com.mgok.conglystore.presentation.user.UpdateInfoUserScreen
import com.mgok.conglystore.ui.theme.CongLyStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val coffeeViewModel: CoffeeViewModel by viewModels()
    private val mapViewModel: MapViewModel by viewModels()


    object Route {
        const val route_splash = "splash"
        const val route_auth = "auth"
        const val route_home = "home"
        const val route_update_user = "update_user"
        const val route_settings = "settings"
        const val route_coffee_type = "coffee_type"
        const val route_coffee = "coffee"
        const val route_detail_coffe = "detail_coffe"
        const val route_search = "search_coffee"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
                        startDestination = Route.route_splash
                    ) {


                        composable(Route.route_splash) {
                            SplashScreen { route ->
                                navController.navigate(route)
                            }
                        }

                        composable(Route.route_auth) {
                            AuthScreen { route ->
                                navController.navigate(route)
                            }
                        }
                        composable(Route.route_update_user) {
                            UpdateInfoUserScreen(
                                onPopBackStack = {
                                    navController.popBackStack()
                                },
                                onHomeScreen = {
                                    navController.navigate(Route.route_home) {
                                        popUpTo(Route.route_splash) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(Route.route_home) {
                            HomeScreen(
                                coffeeViewModel = coffeeViewModel,
                                mapViewModel = mapViewModel,
                                changePage = { route ->
                                    navController.navigate(route)
                                }
                            )
                        }

                        composable(Route.route_settings) {
                            SettingsScreen(
                                onSignOut = {
                                    navController.navigate(Route.route_splash)
                                }
                            )
                        }

                        composable(Route.route_coffee_type) {
                            CoffeeTypeScreen(
                                coffeeViewModel = coffeeViewModel
                            )
                        }

                        composable(Route.route_coffee) {
                            CoffeeScreen(
                                coffeeViewModel = coffeeViewModel
                            )
                        }

                        composable(Route.route_detail_coffe) {
                            DetailProductScreen(
                                coffeeViewModel = coffeeViewModel,
                                onPop = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Route.route_search) {
                            SearchCoffeeScreen(
                                coffeeViewModel = coffeeViewModel,
                                changePage = { route ->
                                    navController.navigate(route)
                                },
                                onPop = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}


