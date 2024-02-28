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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mgok.conglystore.presentation.address.add_address.NewAddressScreen
import com.mgok.conglystore.presentation.address.list_address.AddressScreen
import com.mgok.conglystore.presentation.address.map.MapScreen
import com.mgok.conglystore.presentation.auth.AuthScreen
import com.mgok.conglystore.presentation.coffee.detail.DetailProductScreen
import com.mgok.conglystore.presentation.coffee.manager_coffee_type.CoffeeTypeScreen
import com.mgok.conglystore.presentation.coffee.manager_product.NewCoffeeScreen
import com.mgok.conglystore.presentation.coffee.search.SearchCoffeeScreen
import com.mgok.conglystore.presentation.home.HomeScreen
import com.mgok.conglystore.presentation.order.OrderScreen
import com.mgok.conglystore.presentation.order.OrderViewModel
import com.mgok.conglystore.presentation.splash.SplashScreen
import com.mgok.conglystore.presentation.user.settings.SettingsScreen
import com.mgok.conglystore.presentation.user.update.UpdateInfoUserScreen
import com.mgok.conglystore.ui.theme.CongLyStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val orderViewModel: OrderViewModel by viewModels()


    object Route {
        const val route_splash = "splash"
        const val route_auth = "auth"
        const val route_home = "home"
        const val route_update_user = "update_user"
        const val route_settings = "settings"
        const val route_coffee_type = "coffee_type"
        const val route_coffee = "coffee"
        const val route_detail_coffee = "detail_coffee/{coffeeId}"
        const val route_search = "search_coffee"
        const val rout_order = "order"
        const val route_address = "address"
        const val route_new_address = "new_address"
        const val route_map = "map"
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
                                navController.navigate(route) {
                                    popUpTo(route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
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
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable(Route.route_home) {
                            HomeScreen(
                                changePage = { route ->
                                    navController.navigate(route)
                                },
                                orderViewModel = orderViewModel
                            )
                        }

                        composable(Route.route_settings) {
                            SettingsScreen(
                                onSignOut = {
                                    navController.navigate(Route.route_splash)
                                },
                                changePage = { route ->
                                    navController.navigate(route)

                                }
                            )
                        }

                        composable(Route.route_coffee_type) {
                            CoffeeTypeScreen()
                        }

                        //admin
                        composable(Route.route_coffee) {
                            NewCoffeeScreen()
                        }

                        composable(
                            Route.route_detail_coffee,
                            arguments = listOf(navArgument("coffeeId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            DetailProductScreen(
                                coffeeId = backStackEntry.arguments?.getString("coffeeId")!!,
                                onPop = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Route.route_search) {
                            SearchCoffeeScreen(
                                changePage = { route ->
                                    navController.navigate(route)
                                },
                                onPop = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Route.rout_order) {
                            OrderScreen(
                                orderViewModel = orderViewModel
                            )
                        }

                        composable(Route.route_address) {
                            AddressScreen(
                                changePage = {
                                    navController.navigate(Route.route_new_address)
                                },
                                onPop = {
                                    navController.popBackStack()
                                })
                        }

                        composable(Route.route_new_address) {
                            NewAddressScreen(
                                onPop = {
                                    navController.popBackStack()
                                },
                                changePage = { route ->
                                    navController.navigate(route)
                                }
                            )
                        }

                        composable(Route.route_map) {
                            MapScreen(onPop = { /*TODO*/ })
                        }
                    }

                }
            }
        }
    }

}


