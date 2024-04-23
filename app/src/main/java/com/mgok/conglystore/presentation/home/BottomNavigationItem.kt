package com.mgok.conglystore.presentation.home

import androidx.annotation.DrawableRes
import com.mgok.conglystore.R

data class BottomNavigationItem(
    val title: String,
    @DrawableRes
    val icon: Int,
    val badgeCount: Int? = null,
) {
    companion object {
        val itemsBottom = listOf(
            BottomNavigationItem(
                title = "Trang chủ",
                icon = R.drawable.icon_home
            ),
            BottomNavigationItem(
                title = "Yêu thích",
                icon = R.drawable.icon_heart
            ),
            BottomNavigationItem(
                title = "Giỏ hàng",
                icon = R.drawable.icon_bag,
            ),
            BottomNavigationItem(
                title = "Đơn hàng",
                icon = R.drawable.icon_history,
            ),
        )
    }
}
