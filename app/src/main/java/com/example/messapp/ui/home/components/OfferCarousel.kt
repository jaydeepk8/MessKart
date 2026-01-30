package com.example.messapp.ui.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfferCarousel(
    banners: List<Int>
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { banners.size }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 12.dp
        ) { page ->

            OfferBannerImage(
                imageRes = banners[page]
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                IndicatorDot(
                    isSelected = pagerState.currentPage == index
                )
            }
        }
    }
}
