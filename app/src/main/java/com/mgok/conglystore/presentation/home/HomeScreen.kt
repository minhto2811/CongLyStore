package com.mgok.conglystore.presentation.home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgok.conglystore.MainActivity
import com.mgok.conglystore.R
import com.mgok.conglystore.presentation.home.tabs.tab_cart.TabCart
import com.mgok.conglystore.presentation.home.tabs.tab_fav.TabFavorite
import com.mgok.conglystore.presentation.home.tabs.tab_history.TabHistory
import com.mgok.conglystore.presentation.home.tabs.tab_home.TabHome
import com.mgok.conglystore.utilities.NoRippleInteractionSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    changePage: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val stateUI by viewModel.state.collectAsStateWithLifecycle()
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { BottomNavigationItem.itemsBottom.size })
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val drawerList = arrayOf(
                    R.drawable.icon_coffee_type to "Loại cà phê",
                    R.drawable.icon_coffee to "Cà phê",
                    R.drawable.icon_bill to "Đơn hàng",
                    R.drawable.icon_revenue to "Doanh thu",
                    R.drawable.icon_best_sale to "Top bán chạy",
                    R.drawable.icon_error_payment to "Báo lỗi thanh toán",
                    R.drawable.icon_refund to "Yêu cầu hoàn tiền",
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Quản lí trực tuyến",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = {
                        coroutine.launch { drawerState.close() }
                    }) {
                        Icon(Icons.Default.Close, "")
                    }
                }
                HorizontalDivider()
                drawerList.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.first),
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = item.second) },
                        selected = false,
                        onClick = {
                            coroutine.launch {
                                drawerState.close()
                                changePage.invoke(
                                    when (index) {
                                        0 -> MainActivity.Route.route_coffee_type
                                        1 -> MainActivity.Route.route_manage_product
                                        2 -> MainActivity.Route.route_bill_management
                                        3 -> MainActivity.Route.route_revenue
                                        4 -> MainActivity.Route.route_best_sale
                                        5 -> MainActivity.Route.route_payment_error
                                        6 -> MainActivity.Route.route_refund
                                        else -> MainActivity.Route.route_coffee
                                    }
                                )
                            }
                        }
                    )
                }
            }
        },
        gesturesEnabled = false
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBar(pagerState, coroutine, stateUI.role)
            },
            containerColor = Color(0x97E4DEDE),
        ) { paddingValue ->
            TabsContent(
                pagerState = pagerState,
                paddingValue = paddingValue,
                changePage = changePage,
                drawerState = drawerState,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomBar(pagerState: PagerState, coroutine: CoroutineScope, role: Int) {
    NavigationBar(
        tonalElevation = 10.dp,
        modifier = Modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .border(
                border = BorderStroke(1.dp, color = Color(0xFFF1F1F1)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ),
        containerColor = Color(0xFFFFFFFF),
    ) {
        BottomNavigationItem.itemsBottom.forEachIndexed { index, item ->
            NavigationBarItem(
                enabled = if (role == 0) index == 0 else true,
                selected = pagerState.currentPage == index, onClick = {
                    coroutine.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, icon = {
                    BadgedBox(badge = {
                        if (item.badgeCount != null) {
                            Badge {
                                Text(text = item.badgeCount.toString())
                            }
                        }
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.icon),
                                contentDescription = item.title,
                                modifier = Modifier.scale(if (pagerState.currentPage == index) 1.8f else 1.4f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(5.dp)
                                    .background(
                                        color = if (pagerState.currentPage == index)
                                            Color(0xFFC67C4E)
                                        else
                                            Color(0x748D8D8D),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                    }
                }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFC67C4E),
                    unselectedIconColor = Color(0x748D8D8D)
                ),
                interactionSource = NoRippleInteractionSource()
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    paddingValue: PaddingValues,
    changePage: (String) -> Unit,
    drawerState: DrawerState,
) {
    HorizontalPager(
        userScrollEnabled = false,
        state = pagerState,
        contentPadding = PaddingValues(bottom = paddingValue.calculateBottomPadding())
    ) { page ->
        when (page) {
            0 -> TabHome(
                drawerState = drawerState,
                changePage = changePage,
            )

            1 -> TabFavorite(
                changePage = changePage,
            )

            2 -> TabCart(
                changePage = changePage,
            )

            3 -> TabHistory(changePage = changePage)
            else -> {}
        }
    }
}



