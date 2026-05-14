package com.example.messapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.messapp.navigation.Routes
import com.example.messapp.ui.home.components.LocationAndSearchSection
import com.example.messapp.ui.home.components.MessItemCard
import com.example.messapp.ui.home.components.MessNearMeHeader
import com.example.messapp.ui.home.components.OfferCarousel
import com.example.messapp.ui.theme.AppBackground
import com.example.messapp.ui.theme.AppPrimaryGreen
import com.example.messapp.ui.theme.AppTextPrimary
import com.example.messapp.ui.theme.AppTextSecondary

@OptIn(ExperimentalFoundationApi::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val messes by viewModel.filteredMesses.collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilters by remember { mutableStateOf(false) }

    val showStickyHeader by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 1 }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        LazyColumn(state = listState) {

            item {
                LocationAndSearchSection(
                    uiState = uiState,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onFilterClick = { showFilters = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OfferCarousel(banners = uiState.offerBanners)

                Spacer(modifier = Modifier.height(16.dp))

                ActiveFilterRow(
                    activeFilters = uiState.activeFilters,
                    sortOption = uiState.sortOption,
                    onFilterClick = viewModel::toggleFilter,
                    onClearClick = viewModel::clearFilters
                )
            }

            item {
                MessNearMeHeader(
                    onViewMapClick = {
                        navController.navigate(Routes.MAP)
                    }
                )
            }

            if (messes.isEmpty()) {
                item {
                    EmptyMessResult(
                        hasSearch = uiState.searchQuery.isNotBlank() || uiState.activeFilters.isNotEmpty(),
                        onClearClick = viewModel::clearFilters
                    )
                }
            } else {
                items(messes) { mess ->
                    MessItemCard(
                        mess = mess,
                        onClick = {
                            navController.navigate("${Routes.MESS_DETAILS}/${mess.id}")
                        }
                    )
                }
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showFilters) {
            HomeFilterSheet(
                activeFilters = uiState.activeFilters,
                sortOption = uiState.sortOption,
                onFilterClick = viewModel::toggleFilter,
                onSortClick = viewModel::setSortOption,
                onClearClick = viewModel::clearFilters,
                onDismiss = { showFilters = false }
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ActiveFilterRow(
    activeFilters: Set<String>,
    sortOption: String,
    onFilterClick: (String) -> Unit,
    onClearClick: () -> Unit
) {
    if (activeFilters.isEmpty() && sortOption == "Nearest") return

    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(onClick = {}, label = { Text("Sort: $sortOption") })
        activeFilters.forEach { filter ->
            AssistChip(
                onClick = { onFilterClick(filter) },
                label = { Text("$filter ×") }
            )
        }
        AssistChip(onClick = onClearClick, label = { Text("Clear all") })
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun EmptyMessResult(
    hasSearch: Boolean,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (hasSearch) "No mess found" else "No messes available",
            color = AppTextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Try removing filters or searching for another food type.",
            color = AppTextSecondary
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onClearClick, shape = RoundedCornerShape(14.dp)) {
            Text("Clear filters")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun HomeFilterSheet(
    activeFilters: Set<String>,
    sortOption: String,
    onFilterClick: (String) -> Unit,
    onSortClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val filterGroups = listOf(
        "Food preference" to listOf("Pure Veg", "Veg / Non-Veg"),
        "Service mode" to listOf("Home Delivery", "Dine-in"),
        "Quick picks" to listOf("Rating 4.5+", "Under 1 km")
    )
    val sortOptions = listOf("Nearest", "Top Rated", "Fastest")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Filter messes", fontWeight = FontWeight.Bold, color = AppTextPrimary)
            Text("Find a tiffin service that matches your daily routine.", color = AppTextSecondary)

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Sort by", fontWeight = FontWeight.SemiBold, color = AppTextPrimary)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortOptions.forEach { option ->
                        FilterChip(
                            selected = sortOption == option,
                            onClick = { onSortClick(option) },
                            label = { Text(option) }
                        )
                    }
                }
            }

            filterGroups.forEach { (title, filters) ->
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(title, fontWeight = FontWeight.SemiBold, color = AppTextPrimary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filters.forEach { filter ->
                            FilterChip(
                                selected = filter in activeFilters,
                                onClick = { onFilterClick(filter) },
                                label = { Text(filter) }
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onClearClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimaryGreen)
                ) {
                    Text("Apply")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
