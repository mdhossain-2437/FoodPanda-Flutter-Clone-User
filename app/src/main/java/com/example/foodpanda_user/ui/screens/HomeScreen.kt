package com.example.foodpanda_user.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_user.R
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.BannerItem
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Order
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.data.model.UserProfile
import com.example.foodpanda_user.ui.theme.FoodpandaAmber
import com.example.foodpanda_user.ui.theme.FoodpandaPink
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: UserProfile,
    selectedAddress: Address,
    addresses: List<Address>,
    shops: List<Shop>,
    banners: List<BannerItem>,
    cartItems: List<CartItem>,
    currentOrder: Order?,
    favoriteShopIds: Set<String>,
    onSelectAddress: (Address) -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToFoodDelivery: (String) -> Unit,
    onNavigateToShop: (Shop) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToVouchers: () -> Unit,
    onNavigateToOrderHistory: () -> Unit,
    onNavigateToActiveOrder: () -> Unit,
    onNavigateToBanner: (BannerItem) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onShowAuthModal: () -> Unit,
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAddressSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val totalCartQty = remember(cartItems) { cartItems.sumOf { it.quantity } }
    val displayedShops = remember(shops, favoriteShopIds, showFavoritesOnly) {
        if (showFavoritesOnly) shops.filter { favoriteShopIds.contains(it.uid) } else shops
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawerContent(
                user = user,
                onSignInClick = {
                    scope.launch { drawerState.close() }
                    onShowAuthModal()
                },
                onVouchersClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToVouchers()
                },
                onFavouritesClick = {
                    scope.launch { drawerState.close() }
                    showFavoritesOnly = !showFavoritesOnly
                    onShowMessage(if (showFavoritesOnly) "Showing Favourite restaurants" else "Showing all restaurants")
                },
                onOrdersClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToOrderHistory()
                },
                onAddressesClick = {
                    scope.launch { drawerState.close() }
                    onNavigateToAddresses()
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    showLogoutDialog = true
                },
                onInfoClick = { label ->
                    scope.launch { drawerState.close() }
                    onShowMessage(label)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HomeAppBar(
                    title = selectedAddress.displayTitle,
                    subtitle = selectedAddress.displaySubtitle,
                    totalCartQuantity = totalCartQty,
                    showFavoritesActive = showFavoritesOnly,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onAddressClick = { showAddressSheet = true },
                    onFavoritesClick = {
                        showFavoritesOnly = !showFavoritesOnly
                        onShowMessage(if (showFavoritesOnly) "Filtering by Favourites" else "Showing all restaurants")
                    },
                    onCartClick = onNavigateToCart,
                    onSearchClick = onNavigateToSearch
                )
            },
            bottomBar = {
                if (currentOrder != null) {
                    ActiveOrderBottomContainer(
                        order = currentOrder,
                        onClick = onNavigateToActiveOrder
                    )
                }
            },
            containerColor = Color.White
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("home_scroll_list"),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEEEEEE))
                            .padding(15.dp)
                    ) {
                        HomeCollage(
                            onFoodDeliveryClick = { onNavigateToFoodDelivery("Food delivery") },
                            onShopsClick = { onNavigateToFoodDelivery("Shops") },
                            onPickUpClick = { onNavigateToFoodDelivery("Pick-up") },
                            onPandaSendClick = { onNavigateToFoodDelivery("pandasend") }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showFavoritesOnly) "Your Favourite Restaurants" else "Popular Restaurants",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "See all",
                            color = FoodpandaPink,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .clickable { onNavigateToFoodDelivery("Food delivery") }
                                .padding(4.dp)
                                .testTag("see_all_popular_button")
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayedShops, key = { it.uid }) { shop ->
                            RestaurantCard(
                                shop = shop,
                                isFavorite = favoriteShopIds.contains(shop.uid),
                                onFavoriteToggle = { onToggleFavorite(shop.uid) },
                                onClick = { onNavigateToShop(shop) },
                                modifier = Modifier
                                    .width(245.dp)
                                    .testTag("restaurant_card_${shop.uid}")
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Campaigns & Daily Deals",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(banners, key = { it.id }) { banner ->
                            CampaignBannerCard(
                                banner = banner,
                                onClick = { onNavigateToBanner(banner) }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "All Restaurants Near You",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(displayedShops, key = { "all_${it.uid}" }) { shop ->
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

    if (showAddressSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false },
            containerColor = Color.White
        ) {
            SelectAddressModalContent(
                addresses = addresses,
                selectedAddress = selectedAddress,
                onSelect = { addr ->
                    onSelectAddress(addr)
                    showAddressSheet = false
                },
                onManageAddresses = {
                    showAddressSheet = false
                    onNavigateToAddresses()
                }
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Logging out?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Thanks for stopping by. See you again soon!",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onSignOut()
                    }
                ) {
                    Text("Log out", color = FoodpandaPink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.DarkGray)
                }
            }
        )
    }
}

@Composable
fun HomeAppBar(
    title: String,
    subtitle: String,
    totalCartQuantity: Int,
    showFavoritesActive: Boolean,
    onMenuClick: () -> Unit,
    onAddressClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FoodpandaPink)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("drawer_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open navigation drawer",
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onAddressClick)
                    .padding(horizontal = 4.dp)
                    .testTag("header_address_selector")
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = onFavoritesClick,
                modifier = Modifier.testTag("favorites_filter_button")
            ) {
                Icon(
                    imageVector = if (showFavoritesActive) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favourites",
                    tint = Color.White
                )
            }
            Box(contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.testTag("header_cart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Shopping cart",
                        tint = Color.White
                    )
                }
                if (totalCartQuantity > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-6).dp, y = (-6).dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = totalCartQuantity.toString(),
                            color = FoodpandaPink,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable(onClick = onSearchClick)
                .testTag("home_search_bar"),
            color = Color.White,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF616161),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Search for shops & restaurants",
                    color = Color(0xFF616161),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun HomeCollage(
    onFoodDeliveryClick: () -> Unit,
    onShopsClick: () -> Unit,
    onPickUpClick: () -> Unit,
    onPandaSendClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Top Wide Card: Food delivery
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFFCFCFC))
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                .clickable(onClick = onFoodDeliveryClick)
                .testTag("collage_food_delivery")
        ) {
            Image(
                painter = painterResource(id = R.drawable.food_delivery),
                contentDescription = "Food delivery",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 6.dp, bottom = 4.dp)
                    .width(140.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(15.dp)
            ) {
                Text(
                    text = "Food delivery",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Order food you love",
                    fontSize = 12.sp,
                    color = Color(0xFF424242)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom 2-Column Row: Shops on left, Pick-up + pandasend on right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Left: Shops
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFCFCFC))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                    .clickable(onClick = onShopsClick)
                    .testTag("collage_shops")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shops),
                    contentDescription = "Shops",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 14.dp, bottom = 6.dp)
                        .width(100.dp),
                    contentScale = ContentScale.Fit
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp)
                ) {
                    Text(
                        text = "Shops",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Groceries and more",
                        fontSize = 12.sp,
                        color = Color(0xFF424242)
                    )
                }
            }

            // Right: Pick-up & pandasend
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.7f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFCFCFC))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                        .clickable(onClick = onPickUpClick)
                        .testTag("collage_pickup")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pick_up),
                        contentDescription = "Pick-up",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 6.dp)
                            .width(96.dp),
                        contentScale = ContentScale.Fit
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(15.dp)
                    ) {
                        Text(
                            text = "Pick-up",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Up to 50% off",
                            fontSize = 12.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFCFCFC))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                        .clickable(onClick = onPandaSendClick)
                        .testTag("collage_pandasend")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pandasend),
                        contentDescription = "pandasend",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 4.dp, end = 4.dp)
                            .width(55.dp),
                        contentScale = ContentScale.Fit
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "pandasend",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Express\nDelivery",
                            fontSize = 11.sp,
                            lineHeight = 13.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantCard(
    shop: Shop,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painterResource(id = shop.shopImageRes),
                contentDescription = shop.shopName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Featured Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(FoodpandaPink)
                    .padding(start = 8.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = "Featured",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            // Favorite IconButton
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
                    .clickable(onClick = onFavoriteToggle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = FoodpandaPink,
                    modifier = Modifier.size(18.dp)
                )
            }
            // Remaining Time Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFFCFF))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${shop.remainingTime} min",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = shop.shopName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = FoodpandaAmber,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = String.format(Locale.US, "%.1f", shop.rating),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "(${shop.totalRating})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "$ • ${shop.shopDescription}",
            fontSize = 12.sp,
            color = Color(0xFF707070),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DirectionsBike,
                contentDescription = null,
                tint = FoodpandaPink,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = if (shop.deliveryPrice == 0.0) "Free delivery" else String.format(Locale.US, "$ %.2f", shop.deliveryPrice),
                fontSize = 12.sp,
                color = if (shop.deliveryPrice == 0.0) FoodpandaPink else Color.Black,
                fontWeight = if (shop.deliveryPrice == 0.0) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun CampaignBannerCard(
    banner: BannerItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(140.dp)
            .clickable(onClick = onClick)
            .testTag("banner_card_${banner.id}"),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = banner.imageRes),
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.42f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Surface(
                    color = FoodpandaPink,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "CODE: ${banner.promoCode}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = banner.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = banner.subtitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun MyDrawerContent(
    user: UserProfile,
    onSignInClick: () -> Unit,
    onVouchersClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onInfoClick: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(300.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .background(FoodpandaPink)
                .padding(18.dp)
        ) {
            if (user.isSignedIn) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(1).uppercase(),
                            color = FoodpandaPink,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = user.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = user.email,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onSignInClick)
                        .testTag("drawer_sign_in_button"),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "Sign up / Log in",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (user.isSignedIn) {
            DrawerMenuItem("Vouchers & offers", Icons.Default.LocalOffer, onVouchersClick, "drawer_vouchers")
            DrawerMenuItem("Favourites", Icons.Default.FavoriteBorder, onFavouritesClick, "drawer_favourites")
            DrawerMenuItem("Orders & reordering", Icons.AutoMirrored.Filled.LibraryBooks, onOrdersClick, "drawer_orders")
            DrawerMenuItem("Addresses", Icons.Default.LocationOn, onAddressesClick, "drawer_addresses")
            DrawerMenuItem("Payment methods", Icons.Default.Payment, { onInfoClick("Cash & Card payment active at Checkout") }, "drawer_payments")
        }
        DrawerMenuItem("Help center", Icons.AutoMirrored.Filled.HelpOutline, { onInfoClick("24/7 Foodpanda Support available in Order screen") }, "drawer_help")
        DrawerMenuItem("Invite friends", Icons.Default.CardGiftcard, { onInfoClick("Use code HELLOPANDA to share 50% OFF with friends!") }, "drawer_invite")
        HorizontalDivider(color = Color(0xFFE0E0E0), modifier = Modifier.padding(vertical = 6.dp))
        DrawerMenuItem("Settings", null, { onInfoClick("Notifications & language preferences up to date") }, "drawer_settings")
        DrawerMenuItem("Terms & Conditions / Privacy", null, { onInfoClick("Foodpanda Terms & Privacy Policy") }, "drawer_terms")
        if (user.isSignedIn) {
            DrawerMenuItem("Log out", null, onLogoutClick, "drawer_logout")
        }
    }
}

@Composable
private fun DrawerMenuItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = FoodpandaPink,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
        }
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SelectAddressModalContent(
    addresses: List<Address>,
    selectedAddress: Address,
    onSelect: (Address) -> Unit,
    onManageAddresses: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = "Choose a delivery address",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        addresses.forEach { address ->
            val isSelected = address.id == selectedAddress.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSelect(address) }
                    .padding(vertical = 10.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onSelect(address) },
                    colors = RadioButtonDefaults.colors(selectedColor = FoodpandaPink)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = address.displayTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = address.displaySubtitle,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    if (address.deliveryInstruction.isNotBlank()) {
                        Text(
                            text = "Note: ${address.deliveryInstruction}",
                            color = FoodpandaPink,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onManageAddresses)
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = FoodpandaPink)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Add or edit delivery addresses",
                color = FoodpandaPink,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun ActiveOrderBottomContainer(
    order: Order,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val statusText = when {
        order.isDelivered -> "Enjoy!"
        order.isNear -> "Anytime now! Look out for your rider!"
        order.isPickup -> "Your rider has picked up your order"
        order.isShopAccept -> "Preparing your order. Your rider will pick it up once it's ready."
        else -> "Got your order ${order.user.name.substringBefore(" ")}!"
    }
    val timeText = when {
        order.isDelivered -> "Delivered"
        order.isNear -> "Anytime now"
        order.isPickup -> "10 mins"
        order.isShopAccept -> "10 - 15 mins"
        else -> "5 - 15 mins"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("active_order_bottom_bar"),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 15.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(FoodpandaPink)
                        .border(2.5.dp, Color(0xFFF8BBD0), CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = order.shop.shopName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand order status",
                        tint = FoodpandaPink
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomLinearProgress(
                            isProgress = !order.isShopAccept && !order.isDelivered,
                            isDone = order.isShopAccept || order.isDelivered,
                            modifier = Modifier.width(34.dp)
                        )
                        CustomLinearProgress(
                            isProgress = order.isShopAccept && !order.isPickup,
                            isDone = order.isPickup || order.isDelivered,
                            modifier = Modifier.weight(1f)
                        )
                        CustomLinearProgress(
                            isProgress = order.isPickup && !order.isNear,
                            isDone = order.isNear || order.isDelivered,
                            modifier = Modifier.weight(1f)
                        )
                        CustomLinearProgress(
                            isProgress = order.isNear && !order.isDelivered,
                            isDone = order.isDelivered,
                            modifier = Modifier.width(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        color = Color(0xFF616161),
                        maxLines = if (expanded) 3 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = timeText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CustomLinearProgress(
    isProgress: Boolean,
    isDone: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "order_step_progress")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LinearProgressIndicator(
        progress = {
            when {
                isDone -> 1f
                isProgress -> animatedProgress
                else -> 0f
            }
        },
        modifier = modifier
            .height(6.dp)
            .clip(RoundedCornerShape(50)),
        color = FoodpandaPink,
        trackColor = Color(0xFFE0E0E0)
    )
}
