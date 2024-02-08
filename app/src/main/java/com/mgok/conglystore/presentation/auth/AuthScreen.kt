package com.mgok.conglystore.presentation.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mgok.conglystore.presentation.auth.sign_in.TabSignIn
import com.mgok.conglystore.presentation.auth.sign_up.TabSignUp
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    onNavigate: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = { 2 })
        val coroutineScope = rememberCoroutineScope()
        Tabs(pagerState) { index ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }

        }
        ContentPager(
            pagerState = pagerState,
            chagePageIndex = {index->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            onNavigate = onNavigate,
        )
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState, onClick: (Int) -> Unit) {
    val list = listOf("Đăng nhập", "Đăng ký")
    TabRow(selectedTabIndex = pagerState.currentPage,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = Color(0xFF648DDB)
            )
        }) {
        list.forEachIndexed { index, title ->
            val isSelected = index == pagerState.currentPage
            Tab(
                selected = isSelected,
                onClick = { onClick.invoke(index) }
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) Color(0xFF648DDB) else Color(0xFFD9D9D9),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentPager(
    pagerState: PagerState,
    chagePageIndex: (Int) -> Unit,
    onNavigate: (String) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        pageSpacing = 20.dp,
    ) { page ->
        when (page) {
            0 -> TabSignIn(
                chagePageIndex = chagePageIndex,
                onNavigate = onNavigate
            )

            1 -> TabSignUp(
                chagePageIndex = chagePageIndex,
                onNavigate = onNavigate
            )
        }
    }
}

