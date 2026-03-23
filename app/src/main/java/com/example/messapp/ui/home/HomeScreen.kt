package com.example.messapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messapp.navigation.Routes
import com.example.messapp.ui.home.components.LocationAndSearchSection
import com.example.messapp.ui.home.components.MessItemCard
import com.example.messapp.ui.home.components.MessNearMeHeader
import com.example.messapp.ui.home.components.OfferCarousel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messes = viewModel.messes
    val listState = rememberLazyListState()

    val showStickyHeader by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 1 }
    }

    Box {
        LazyColumn(state = listState) {

            item {
                LocationAndSearchSection(
                    uiState = uiState,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onFilterClick = {}
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfferCarousel(banners = uiState.offerBanners)

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MessNearMeHeader(
                    onViewMapClick = {
                        navController.navigate(Routes.MAP)
                    }
                )
            }

            items(messes) { mess ->
                MessItemCard(
                    mess = mess,
                    onClick = {
                        navController.navigate("${Routes.MESS_DETAILS}/${mess.id}")
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = showStickyHeader,
            enter = slideInVertically { -it / 2 } + fadeIn(),
            exit = slideOutVertically { -it / 2 } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                tonalElevation = 6.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    MessNearMeHeader(
                        onViewMapClick = {
                            navController.navigate(Routes.MAP)
                        }
                    )
                }
            }
        }
    }
}