package com.example.messapp.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

private val SheetBg         = Color(0xFF1A2E1A)
private val AccentGreen     = Color(0xFF4CAF50)
private val ChipActiveBg    = Color(0xFF4CAF50)
private val ChipInactiveBg  = Color(0xFF2E3F2E)
private val ChipInactiveTxt = Color(0xFFCCCCCC)
private val TextWhite       = Color(0xFFFFFFFF)
private val TextMuted       = Color(0xFFAAAAAA)

@Composable
fun MapScreen(
    onMessClick: (Int) -> Unit = {}
) {
    val messes = remember { MessDataSource.getAllMesses() }
    var activeFilter by remember { mutableStateOf("Open Now") }
    val filters = listOf("Open Now", "Pure Veg", "Rating 4.0+")

    Box(modifier = Modifier.fillMaxSize()) {

        // Map placeholder
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFD4E6C3)),
            contentAlignment = Alignment.Center
        ) {
            Text("Map loads here", fontSize = 16.sp, color = Color(0xFF4A7C4A))
        }

        // Search bar
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(SheetBg.copy(alpha = 0.92f), RoundedCornerShape(28.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = AccentGreen, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text("Search for messes...", fontSize = 14.sp, color = TextMuted, modifier = Modifier.weight(1f))
            Icon(Icons.Default.FilterList, "Filter", tint = TextWhite, modifier = Modifier.size(20.dp))
        }

        // Filter chips
        LazyRow(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 72.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isActive = filter == activeFilter
                Row(
                    modifier = Modifier
                        .background(if (isActive) ChipActiveBg else ChipInactiveBg, RoundedCornerShape(20.dp))
                        .clickable { activeFilter = filter }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (filter.contains("Rating")) {
                        Icon(Icons.Default.Star, null,
                            tint = if (isActive) Color.White else Color(0xFFFFD700),
                            modifier = Modifier.size(13.dp))
                    }
                    Text(filter, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        color = if (isActive) Color.White else ChipInactiveTxt)
                }
            }
        }

        // Dark bottom sheet
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(SheetBg, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(top = 12.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 36.dp, height = 4.dp)
                    .background(Color(0xFF4A6044), RoundedCornerShape(2.dp))
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text("Nearby Messes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Text("${messes.size} places near you", fontSize = 12.sp, color = TextMuted)
                }
                Text("View All", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AccentGreen)
            }
            Spacer(Modifier.height(14.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messes) { mess ->
                    MapMessCard(mess = mess, onClick = { onMessClick(mess.id) })
                }
            }
        }
    }
}

@Composable
private fun MapMessCard(mess: Mess, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(220.dp)
            .background(Color(0xFF243324), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            Image(
                painter = painterResource(id = mess.imageRes),
                contentDescription = mess.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    .background(Color(0xCC000000), RoundedCornerShape(20.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(11.dp))
                Text(mess.rating.toString(), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }

        Column(modifier = Modifier.padding(10.dp)) {
            Text(mess.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(3.dp))
            Text("▸ ${mess.distanceKm}  •  ${mess.cuisine}", fontSize = 11.sp, color = Color(0xFFAAAAAA),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("STARTING", fontSize = 9.sp, color = Color(0xFFAAAAAA), letterSpacing = 0.5.sp)
                    Text("₹80", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                        .clickable { onClick() }
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Text("View", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}