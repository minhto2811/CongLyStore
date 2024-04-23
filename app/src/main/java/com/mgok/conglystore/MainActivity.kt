package com.mgok.conglystore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mgok.conglystore.presentation.address.add_address.NewAddressScreen
import com.mgok.conglystore.presentation.address.list_address.AddressScreen
import com.mgok.conglystore.presentation.address.map.MapScreen
import com.mgok.conglystore.presentation.auth.AuthScreen
import com.mgok.conglystore.presentation.best_sale.BestSaleScreen
import com.mgok.conglystore.presentation.bill.detail.BillDetailScreen
import com.mgok.conglystore.presentation.bill.manage.BillManagementScreen
import com.mgok.conglystore.presentation.coffee.detail.DetailProductScreen
import com.mgok.conglystore.presentation.coffee.manager_coffee_type.CoffeeTypeScreen
import com.mgok.conglystore.presentation.coffee.manager_product.ProductManagementScreen
import com.mgok.conglystore.presentation.coffee.search.SearchCoffeeScreen
import com.mgok.conglystore.presentation.coffee.upsert.NewCoffeeScreen
import com.mgok.conglystore.presentation.home.HomeScreen
import com.mgok.conglystore.presentation.order.OrderScreen
import com.mgok.conglystore.presentation.order.ResultScreen
import com.mgok.conglystore.presentation.revenue.RevenueScreen
import com.mgok.conglystore.presentation.splash.SplashScreen
import com.mgok.conglystore.presentation.user.payment_error.PaymentErrorScreen
import com.mgok.conglystore.presentation.user.refund.RefundScreen
import com.mgok.conglystore.presentation.user.settings.SettingsScreen
import com.mgok.conglystore.presentation.user.update.UpdateInfoUserScreen
import com.mgok.conglystore.ui.theme.CongLyStoreTheme
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    object Route {
        const val route_splash = "splash"
        const val route_auth = "auth"
        const val route_home = "home"
        const val route_update_user = "update_user"
        const val route_settings = "settings"
        const val route_coffee_type = "coffee_type"
        const val route_coffee = "coffee"
        const val route_detail_coffee = "detail_coffee"
        const val route_search = "search_coffee"
        const val rout_order = "order"
        const val route_address = "address"
        const val route_new_address = "new_address"
        const val route_map = "map"
        const val route_result = "result"
        const val route_bill_detail = "bill_detail"
        const val route_payment_error = "payment_error"
        const val route_refund = "refund"
        const val route_bill_management = "bill_management"
        const val route_revenue = "revenue"
        const val route_best_sale = "best_sale"
        const val route_manage_product = "manage_product"
    }

    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            CongLyStoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = Route.route_splash
                    ) {


                        composable(Route.route_splash) {
                            SplashScreen { route ->
                                navController.navigate(route)
                            }
                        }

                        composable(Route.route_auth) {
                            AuthScreen { route ->
                                navController.navigate(route) {
                                    popUpTo(Route.route_auth) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                        composable(Route.route_update_user) {
                            UpdateInfoUserScreen(onPopBackStack = {
                                navController.popBackStack()
                            }, onHomeScreen = {
                                navController.navigate(Route.route_home) {
                                    popUpTo(Route.route_splash) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            })
                        }
                        composable(Route.route_home) {
                            HomeScreen(
                                changePage = { route ->
                                    navController.navigate(route)
                                },
                            )
                        }

                        composable(Route.route_settings) {
                            SettingsScreen(onSignOut = {
                                navController.navigate(Route.route_splash)
                            }, changePage = { route ->
                                navController.navigate(route)
                            })
                        }

                        composable(Route.route_coffee_type) {
                            CoffeeTypeScreen(onPop = {
                                navController.popBackStack()
                            })
                        }

                        //admin
                        composable(
                            "${Route.route_coffee}?coffeeId={coffeeId}",
                            listOf(navArgument("coffeeId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val coffeeId = backStackEntry.arguments?.getString("coffeeId")
                            NewCoffeeScreen(
                                coffeeId = coffeeId,
                                onPop = {
                                    navController.popBackStack()
                                })
                        }

                        composable(
                            "${Route.route_detail_coffee}/coffeeId={coffeeId}",
                            arguments = listOf(navArgument("coffeeId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            DetailProductScreen(coffeeId = backStackEntry.arguments?.getString("coffeeId")!!,
                                onPop = {
                                    navController.popBackStack()
                                })
                        }

                        composable(Route.route_search) {
                            SearchCoffeeScreen(changePage = { route ->
                                navController.navigate(route)
                            }, onPop = {
                                navController.popBackStack()
                            })
                        }

                        composable(Route.rout_order) { entry ->
                            val addressId = entry.savedStateHandle.get<String>("addressId")
                            OrderScreen(
                                addressId = addressId,
                                onPop = {
                                    navController.popBackStack()
                                },
                                onSelectAddress = { addressSelectedId ->
                                    val direct =
                                        "${Route.route_address}?addressSelectedId=$addressSelectedId"
                                    navController.navigate(direct)
                                },
                                onOrder = {
                                    navController.navigate(Route.route_result) {
                                        popUpTo(Route.route_splash) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                })
                        }

                        composable(
                            "${Route.route_address}?addressSelectedId={addressSelectedId}",
                            arguments = listOf(navArgument("addressSelectedId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { entry ->
                            val addressSelectedId = entry.arguments?.getString("addressSelectedId")
                            AddressScreen(addressSelectedId = addressSelectedId,
                                changePage = { route, addressId ->
                                    val direct =
                                        if (addressId == null) route else "$route?addressId=$addressId"
                                    navController.navigate(direct)
                                },
                                onPop = {
                                    navController.popBackStack()
                                },
                                onSelectedAddress = { addressId ->
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "addressId",
                                        addressId
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            "${Route.route_new_address}?addressId={addressId}",
                            arguments = listOf(navArgument("addressId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { entry ->
                            val stringAddress = entry.savedStateHandle.get<String>("address")
                            val addressId = entry.arguments?.getString("addressId")
                            NewAddressScreen(stringAddress = stringAddress,
                                addressId = addressId,
                                onPop = {
                                    navController.popBackStack()
                                },
                                changePage = { route, stringAddress ->
                                    val direct =
                                        if (stringAddress == null) route else "$route?stringAddress=$stringAddress"
                                    navController.navigate(direct)
                                })
                        }

                        composable(
                            "${Route.route_map}?stringAddress={stringAddress}",
                            arguments = listOf(navArgument("stringAddress") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val addressDefault =
                                backStackEntry.arguments?.getString("stringAddress")
                            MapScreen(addressDefault = addressDefault, onPop = { stringAddress ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "address",
                                    stringAddress
                                )
                                navController.popBackStack()
                            })
                        }

                        composable(
                            "${Route.route_result}?orderId={orderId}?status={status}",
                            arguments = listOf(navArgument("status") {
                                type = NavType.IntType
                                defaultValue = 0
                            }, navArgument("orderId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val status =
                                backStackEntry.arguments?.getInt("status")
                            val orderId =
                                backStackEntry.arguments?.getString("orderId")
                            ResultScreen(status = status,
                                orderId = orderId,
                                goHome = {
                                    navController.navigate(Route.route_home) {
                                        popUpTo(Route.route_result) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                })
                        }

                        composable(
                            "${Route.route_bill_detail}/billId={billId}?isUpdatePaymentStatus={isUpdatePaymentStatus}&epId={epId}&refundId={refundId}&refundStatus={refundStatus}",
                            listOf(
                                navArgument("billId") {
                                    type = NavType.StringType
                                },
                                navArgument("isUpdatePaymentStatus") {
                                    type = NavType.BoolType
                                    defaultValue = false
                                },
                                navArgument("epId") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("refundId") {
                                    type = NavType.StringType
                                    nullable = true
                                },
                                navArgument("refundStatus") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                            )
                        ) { entry ->
                            val billId = entry.arguments?.getString("billId")
                            val epId = entry.arguments?.getString("epId")
                            val refundId = entry.arguments?.getString("refundId")
                            val refundStatus = entry.arguments?.getInt("refundStatus")
                            Log.e("onCreate: ", refundId + " <><><><> " + refundStatus)
                            val isUpdatePaymentStatus =
                                entry.arguments?.getBoolean("isUpdatePaymentStatus") ?: false
                            BillDetailScreen(
                                isUpdatePaymentStatus = isUpdatePaymentStatus,
                                billId = billId,
                                refundStatus = refundStatus,
                                refundId = refundId,
                                epId = epId,
                                onPop = { navController.popBackStack() }
                            )
                        }
                        composable(Route.route_payment_error) {
                            PaymentErrorScreen(onPop = {
                                navController.popBackStack()
                            },
                                onClick = { epId, billId ->
                                    navController.navigate("${Route.route_bill_detail}/billId=$billId?epId=$epId")
                                })
                        }

                        composable(Route.route_refund) {
                            RefundScreen(
                                onPop = {
                                    navController.popBackStack()
                                },
                                onNavigate = { refundId, billId, refundStatus ->
                                    navController.navigate("${Route.route_bill_detail}/billId=$billId?refundId=$refundId&refundStatus=$refundStatus")
                                }
                            )
                        }

                        composable(Route.route_bill_management) {
                            BillManagementScreen(
                                onPop = { navController.popBackStack() },
                                onClickBill = { billId ->
                                    navController.navigate("${Route.route_bill_detail}/billId=${billId}")
                                }
                            )
                        }

                        composable(Route.route_revenue) {
                            RevenueScreen(
                                onPop = { navController.popBackStack() }
                            )
                        }

                        composable(Route.route_best_sale) {
                            BestSaleScreen(
                                onPop = { navController.popBackStack() },
                                onNavigate = { navController.navigate(it) }
                            )
                        }

                        composable(Route.route_manage_product) {
                            ProductManagementScreen(
                                onPop = { navController.popBackStack() },
                                onNavigate = { coffeeId ->
                                    val route =
                                        if (coffeeId != null) "${Route.route_coffee}?coffeeId=$coffeeId"
                                        else Route.route_coffee
                                    navController.navigate(route)
                                }
                            )
                        }
                    }

                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == Activity.RESULT_OK) {
            data?.let {
                var isUpdatePaymentStatus: Boolean = false
                data.extras?.let { extras ->
                    extras.getString("extra")?.let { data ->
                        isUpdatePaymentStatus =
                            JSONObject(data)["isUpdatePaymentStatus"].toString().toBoolean()
                    }
                }
                val status = it.getIntExtra("status", -1)
                val orderId = it.getStringExtra("orderId")
                val direct = if (status == 0) {
                    if (isUpdatePaymentStatus) {
                        "${Route.route_bill_detail}/billId=${orderId}?isUpdatePaymentStatus=$isUpdatePaymentStatus"
                    } else {
                        "${Route.route_result}?orderId=${orderId}?status=0"
                    }
                } else {
                    "${Route.route_result}?orderId=${orderId}?status=-1"
                }
                navController.navigate(direct) {
                    popUpTo(Route.route_home) {
                        inclusive = !isUpdatePaymentStatus
                    }
                    launchSingleTop = !isUpdatePaymentStatus
                }
            }
        }
    }

}


