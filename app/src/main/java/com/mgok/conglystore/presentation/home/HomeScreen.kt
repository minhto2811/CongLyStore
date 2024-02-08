package com.mgok.conglystore.presentation.home


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mgok.conglystore.presentation.home.tab.TabHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(onClickSettings: () -> Unit) {
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(pagerState, coroutine)
        }
    ) { paddingValue ->
        TabsContent(
            pagerState = pagerState,
            paddingValue = paddingValue,
            onClickSettings = onClickSettings
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(pagerState: PagerState, coroutine: CoroutineScope) {
    NavigationBar(
    ) {
        BottomNavigationItem.itemsBottom.forEachIndexed { index, item ->
            NavigationBarItem(selected = pagerState.currentPage == index,
                onClick = {
                    coroutine.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = index == pagerState.currentPage,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == pagerState.currentPage) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(pagerState: PagerState, paddingValue: PaddingValues, onClickSettings: () -> Unit) {
    HorizontalPager(
        state = pagerState,
        contentPadding = paddingValue
    ) { page ->
        when (page) {
            0 -> TabHome()

            1 -> Text(
                text = "1",
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary)
            )

            2 ->Text(
                text = "1",
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}



