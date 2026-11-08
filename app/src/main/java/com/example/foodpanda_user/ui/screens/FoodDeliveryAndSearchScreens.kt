package com.example.foodpanda_user.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.BannerItem
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.ui.theme.FoodpandaPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDeliveryScreen(
    initialCategory: String,
    selectedAddress: Address,
    shops: List<Shop>,
    banners: List<BannerItem>,
    cartItems: List<CartItem>,
    favoriteShopIds: Set<String>,
    onBack: () -> Unit,
    onNavigateToShop: (Shop) -> Unit,
    onNavigateToBanner: (BannerItem) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf(initialCategory) }
    val filters = listOf("All", "Food delivery", "Shops", "Pick-up", "Free delivery")

    val filteredShops = remember(shops, selectedFilter) {
        when (selectedFilter) {
            "All", "Food delivery", "pandasend" -> shops
            "Shops" -> shops.filter { it.categoryTag == "Shops" || it.shopDescription.contains("Bakery", true) || it.shopDescription.contains("Groceries", true) }
            "Pick-up" -> shops.filter { it.categoryTag == "Pick-up" || it.deliveryPrice <= 0.5 }
            "Free delivery" -> shops.filter { it.deliveryPrice == 0.0 }
            else -> shops
        }
    }

    val totalCartQty = remember(cartItems) { cartItems.sumOf { it.quantity } }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = selectedAddress.displayTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = initialCategory,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("food_delivery_back")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToCart) {
                            Box {
                                Icon(Icons.Default.ShoppingBag, contentDescription = "Cart", tint = FoodpandaPink)
                                if (totalCartQty > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(15.dp)
                                            .clip(CircleShape)
                                            .background(FoodpandaPink),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = totalCartQty.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 6.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF5F5F5))
                        .clickable(onClick = onNavigateToSearch)
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF616161), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Search for shops & restaurants", color = Color(0xFF616161), fontSize = 13.sp)
                    }
                }
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FoodpandaPink,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            if (banners.isNotEmpty()) {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(banners, key = { it.id }) { banner ->
                            Box(
                                modifier = Modifier
                                    .height(165.dp)
                                    .aspectRatio(4f / 5f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onNavigateToBanner(banner) }
                            ) {
                                Image(
                                    painter = painterResource(id = banner.imageRes),
                                    contentDescription = banner.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.35f))
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = banner.title,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = banner.promoCode,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Local Hero",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "See all",
                        color = FoodpandaPink,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { selectedFilter = "All" }
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredShops, key = { "hero_${it.uid}" }) { shop ->
                        RestaurantCard(
                            shop = shop,
                            isFavorite = favoriteShopIds.contains(shop.uid),
                            onFavoriteToggle = { onToggleFavorite(shop.uid) },
                            onClick = { onNavigateToShop(shop) },
                            modifier = Modifier.width(240.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
            }

            item {
                Text(
                    text = "All restaurants",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(filteredShops, key = { "list_${it.uid}" }) { shop ->
                Box(modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)) {
                    RestaurantCard(
                        shop = shop,
                        isFavorite = favoriteShopIds.contains(shop.uid),
                        onFavoriteToggle = { onToggleFavorite(shop.uid) },
                        onClick = { onNavigateToShop(shop) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerScreen(
    banner: BannerItem,
    shops: List<Shop>,
    favoriteShopIds: Set<String>,
    onBack: () -> Unit,
    onApplyCode: (String) -> Unit,
    onNavigateToShop: (Shop) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val bannerShops = remember(banner, shops) {
        val matched = shops.filter { banner.shopUids.contains(it.uid) }
        matched.ifEmpty { shops }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(banner.title, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = banner.imageRes),
                        contentDescription = banner.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = banner.title,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = banner.subtitle,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onApplyCode(banner.promoCode) },
                            colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Claim Voucher: ${banner.promoCode}", color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Participating Restaurants",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(bannerShops, key = { it.uid }) { shop ->
                Box(modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)) {
                    RestaurantCard(
                        shop = shop,
                        isFavorite = favoriteShopIds.contains(shop.uid),
                        onFavoriteToggle = { onToggleFavorite(shop.uid) },
                        onClick = { onNavigateToShop(shop) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    shops: List<Shop>,
    recentSearches: List<String>,
    favoriteShopIds: Set<String>,
    onBack: () -> Unit,
    onSubmitSearch: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onNavigateToShop: (Shop) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var submittedQuery by remember { mutableStateOf<String?>(null) }

    val popularTags = listOf("Bubble Tea", "Burger", "Pizza", "Chicken", "Dim Sum", "Noodles", "Cake", "Groceries")

    val matchedShops = remember(submittedQuery, query, shops) {
        val activeTerm = (submittedQuery ?: query).trim()
        if (activeTerm.isBlank()) {
            null
        } else {
            shops.filter {
                it.shopName.contains(activeTerm, ignoreCase = true) ||
                    it.shopDescription.contains(activeTerm, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("search_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                    }
                },
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 15.dp)
                            .height(42.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFF5F5F5))
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF616161),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (query.isEmpty()) {
                                    Text(
                                        text = "Search for shop & restaurants",
                                        color = Color(0xFF616161),
                                        fontSize = 14.sp
                                    )
                                }
                                BasicTextField(
                                    value = query,
                                    onValueChange = {
                                        query = it
                                        submittedQuery = null
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            if (query.isNotBlank()) {
                                                onSubmitSearch(query)
                                                submittedQuery = query
                                            }
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("search_input_field")
                                )
                            }
                            if (query.isNotEmpty()) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF616161),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            query = ""
                                            submittedQuery = null
                                        }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (matchedShops == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (recentSearches.isNotEmpty()) {
                    Text(
                        text = "Recent searches",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    recentSearches.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    query = item
                                    submittedQuery = item
                                    onSubmitSearch(item)
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = item, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            IconButton(
                                onClick = { onRemoveRecentSearch(item) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Text(
                    text = "Popular cuisines",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularTags.forEach { tag ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    query = tag
                                    submittedQuery = tag
                                    onSubmitSearch(tag)
                                },
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFF5F5F5),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                        ) {
                            Text(
                                text = tag,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(15.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "${matchedShops.size} results for \"${submittedQuery ?: query}\"",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                items(matchedShops, key = { it.uid }) { shop ->
                    RestaurantCard(
                        shop = shop,
                        isFavorite = favoriteShopIds.contains(shop.uid),
                        onFavoriteToggle = { onToggleFavorite(shop.uid) },
                        onClick = { onNavigateToShop(shop) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
