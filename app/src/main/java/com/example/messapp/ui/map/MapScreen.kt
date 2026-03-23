package com.example.messapp.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messapp.data.model.Mess
import com.example.messapp.data.source.MessDataSource

private val PrimaryGreen       = Color(0xFF8BC34A)
private val ActiveChipBg       = Color(0xFF8BC34A)
private val InactiveChipBg     = Color.White
private val InactiveChipBorder = Color(0xFFDDDDDD)
private val TextPrimary        = Color(0xFF1A1A1A)
private val TextSecondary      = Color(0xFF757575)
private val RatingGold         = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onMessClick: (Int) -> Unit = {}
) {
    val messes = remember { MessDataSource.getAllMesses() }
    var activeFilter by remember { mutableStateOf("Open Now") }
    val filters = listOf("Open Now", "Pure Veg", "Rating 4.0+")

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 280.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContainerColor = Color.White,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 4.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .background(Color(0xFFDDDDDD), RoundedCornerShape(2.dp))
            )
        },
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(4.dp))

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Nearby Messes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "12 places discoverable near you",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Horizontal cards
                LazyRow(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messes) { mess ->
                        MapMessCard(mess = mess, onClick = { onMessClick(mess.id) })
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Search bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null,
                    tint = Color(0xFFE53935), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Search for messes...", fontSize = 14.sp,
                    color = Color(0xFF9E9E9E), modifier = Modifier.weight(1f))
                Icon(Icons.Default.FilterList, "Filter",
                    tint = TextPrimary, modifier = Modifier.size(20.dp))
            }

            // Filter chips
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isActive = filter == activeFilter
                    Row(
                        modifier = Modifier
                            .then(
                                if (isActive)
                                    Modifier.background(ActiveChipBg, RoundedCornerShape(20.dp))
                                else
                                    Modifier
                                        .background(InactiveChipBg, RoundedCornerShape(20.dp))
                                        .border(1.dp, InactiveChipBorder, RoundedCornerShape(20.dp))
                            )
                            .clickable { activeFilter = filter }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (filter.contains("Rating")) {
                            Icon(Icons.Default.Star, null,
                                tint = if (isActive) Color.White else RatingGold,
                                modifier = Modifier.size(13.dp))
                        }
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isActive) Color.White else TextPrimary
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Map placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8EAE6)),
                contentAlignment = Alignment.Center
            ) {
                Text("Map loads here", fontSize = 15.sp, color = Color(0xFF7A9070))
            }
        }
    }
}

@Composable
private fun MapMessCard(mess: Mess, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = mess.imageRes),
                    contentDescription = mess.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(Icons.Default.Star, null,
                        tint = RatingGold, modifier = Modifier.size(13.dp))
                    Text(mess.rating.toString(), fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = mess.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null,
                        tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "${mess.distanceKm}  •  ${mess.cuisine}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}