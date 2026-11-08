package com.example.foodpanda_user.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_user.R
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Choice
import com.example.foodpanda_user.data.model.Food
import com.example.foodpanda_user.data.model.Menu
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.ui.theme.FoodpandaAmber
import com.example.foodpanda_user.ui.theme.FoodpandaBackground
import com.example.foodpanda_user.ui.theme.FoodpandaPink
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ShopDetailScreen(
    shop: Shop,
    menus: List<Menu>,
    cartItems: List<CartItem>,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onFoodClick: (String, Food) -> Unit,
    onViewCartClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val selectedTabIndex by remember {
        derivedStateOf {
            val firstVisible = listState.firstVisibleItemIndex
            (firstVisible - 1).coerceIn(0, (menus.size - 1).coerceAtLeast(0))
        }
    }

    val totalCartQuantity = remember(cartItems) { cartItems.sumOf { it.quantity } }
    val totalCartPrice = remember(cartItems) { cartItems.sumOf { it.totalPrice } }

    Scaffold(
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onViewCartClick)
                        .testTag("shop_view_cart_bar"),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(FoodpandaPink)
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = totalCartQuantity.toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = "View your cart",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = String.format(Locale.US, "$ %.2f", totalCartPrice),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        },
        containerColor = FoodpandaBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    ShopHeaderSection(
                        shop = shop,
                        isFavorite = isFavorite,
                        onBack = onBack,
                        onToggleFavorite = onToggleFavorite,
                        onShare = onShare
                    )
                }

                if (menus.isNotEmpty()) {
                    item {
                        ScrollableTabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = Color.White,
                            contentColor = FoodpandaPink,
                            edgePadding = 15.dp,
                            indicator = { tabPositions ->
                                if (selectedTabIndex < tabPositions.size) {
                                    TabRowDefaults.SecondaryIndicator(
                                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                        color = FoodpandaPink,
                                        height = 3.dp
                                    )
                                }
                            }
                        ) {
                            menus.forEachIndexed { index, menu ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = {
                                        scope.launch {
                                            listState.animateScrollToItem(index + 1)
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = menu.category.title,
                                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selectedTabIndex == index) FoodpandaPink else Color.Gray
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                itemsIndexed(menus, key = { _, m -> m.category.id }) { _, menu ->
                    CategoryMenuSection(
                        menu = menu,
                        onFoodClick = { food -> onFoodClick(menu.category.id, food) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShopHeaderSection(
    shop: Shop,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(215.dp)
        ) {
            Image(
                painter = painterResource(id = shop.shopImageRes),
                contentDescription = shop.shopName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.28f))
            )
            // Top controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    onClick = onBack,
                    testTag = "shop_back_button"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircleIconButton(
                        icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        onClick = onToggleFavorite
                    )
                    CircleIconButton(
                        icon = Icons.Default.Share,
                        contentDescription = "Share",
                        onClick = onShare
                    )
                }
            }

            // Delivery time badge & Panda mascot
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp),
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = FoodpandaPink, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Delivery ${shop.remainingTime} mins",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.foodpanda_panda),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
                    .size(56.dp)
            )
        }

        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = shop.shopName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = shop.shopDescription,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = FoodpandaAmber, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${shop.rating} (${shop.totalRating}+ ratings)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = if (shop.deliveryPrice == 0.0) "Free delivery" else String.format(Locale.US, "Delivery $ %.2f", shop.deliveryPrice),
                    color = FoodpandaPink,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Promo Banner Text
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFCE4EC),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.percentage_icon),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = shop.promoBanner,
                        fontSize = 12.sp,
                        color = FoodpandaPink,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Optional Discount Card matching ExampleData
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FoodpandaPink,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = shop.discountTitle,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = shop.discountSubtitle,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    testTag: String? = null
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick)
            .let { if (testTag != null) it.testTag(testTag) else it },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = FoodpandaPink,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun CategoryMenuSection(
    menu: Menu,
    onFoodClick: (Food) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color.White)
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (menu.category.isHotSale) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = FoodpandaPink,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = menu.category.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        if (!menu.category.subtitle.isNullOrBlank()) {
            Text(
                text = menu.category.subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        menu.foods.forEachIndexed { index, food ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFoodClick(food) }
                    .padding(vertical = 12.dp)
                    .testTag("food_item_${food.id}"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = food.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = food.description,
                        fontSize = 12.sp,
                        color = Color(0xFF707070),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = String.format(Locale.US, "from $ %.2f", food.price),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        if (food.comparePrice > food.price) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format(Locale.US, "$ %.2f", food.comparePrice),
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                        if (food.isHotSale) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Popular",
                                    color = Color(0xFFE65100),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(86.dp)) {
                    Image(
                        painter = painterResource(id = food.imageRes),
                        contentDescription = food.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(FoodpandaPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            if (index < menu.foods.lastIndex) {
                HorizontalDivider(color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun CustomizeScreen(
    shop: Shop,
    categoryId: String,
    food: Food,
    editingCartItem: CartItem?,
    hasDifferentShopInCart: Boolean,
    onBack: () -> Unit,
    onConfirmAddOrUpdate: (
        quantity: Int,
        requiredChoices: Map<String, Choice>,
        optionalChoices: List<Choice>,
        specialInstruction: String,
        availabilityOption: String,
        clearExistingFirst: Boolean
    ) -> Unit
) {
    var quantity by remember { mutableIntStateOf(editingCartItem?.quantity ?: 1) }
    var specialInstruction by remember { mutableStateOf(editingCartItem?.specialInstruction ?: "") }
    var availabilityOption by remember {
        mutableStateOf(editingCartItem?.availabilityOption ?: "Remove it from my order")
    }
    var showReplaceCartDialog by remember { mutableStateOf(false) }

    val requiredChoicesMap = remember {
        mutableStateMapOf<String, Choice>().apply {
            if (editingCartItem != null) {
                putAll(editingCartItem.selectedRequiredChoices)
            } else {
                food.customizations.filter { it.isRequired }.forEach { cust ->
                    cust.choices.firstOrNull()?.let { put(cust.id, it) }
                }
            }
        }
    }

    val optionalChoicesList = remember {
        mutableStateListOf<Choice>().apply {
            if (editingCartItem != null) {
                addAll(editingCartItem.selectedOptionalChoices)
            }
        }
    }

    val requiredCount = food.customizations.count { it.isRequired }
    val isReadyToAdd = requiredChoicesMap.size >= requiredCount

    val availabilityOptions = listOf(
        "Remove it from my order",
        "Cancel the entire order",
        "Call me & confirm replacement"
    )

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 15.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (quantity > 1) FoodpandaPink else Color(0xFFE0E0E0))
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                    }
                    Text(
                        text = quantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(FoodpandaPink)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Button(
                        onClick = {
                            if (hasDifferentShopInCart && editingCartItem == null) {
                                showReplaceCartDialog = true
                            } else {
                                onConfirmAddOrUpdate(
                                    quantity,
                                    requiredChoicesMap.toMap(),
                                    optionalChoicesList.toList(),
                                    specialInstruction,
                                    availabilityOption,
                                    false
                                )
                            }
                        },
                        enabled = isReadyToAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("customize_add_to_cart_button")
                    ) {
                        Text(
                            text = if (editingCartItem != null) "Update" else "Add to cart",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
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
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp)
                ) {
                    Image(
                        painter = painterResource(id = food.imageRes),
                        contentDescription = food.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(12.dp)
                    ) {
                        CircleIconButton(
                            icon = Icons.Default.Close,
                            contentDescription = "Close",
                            onClick = onBack,
                            testTag = "customize_close_button"
                        )
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(15.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = food.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = String.format(Locale.US, "from $ %.2f", food.price),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = food.description,
                        color = Color(0xFF616161),
                        fontSize = 13.sp
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFE0E0E0))
                }
            }

            itemsIndexed(food.customizations) { _, customize ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (customize.isRequired) Color(0xFFFFF8FA) else Color(0xFFFAFAFA)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (customize.isRequired) FoodpandaPink.copy(alpha = 0.25f) else Color(0xFFE0E0E0)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = customize.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = if (customize.isRequired) "Select 1" else "Select optional add-ons",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Surface(
                                color = if (customize.isRequired) FoodpandaPink else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (customize.isRequired) "1 Required" else "Optional",
                                    color = if (customize.isRequired) Color.White else Color.DarkGray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        customize.choices.forEach { choice ->
                            if (customize.isRequired) {
                                val selected = requiredChoicesMap[customize.id]?.choice == choice.choice
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { requiredChoicesMap[customize.id] = choice }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selected,
                                        onClick = { requiredChoicesMap[customize.id] = choice },
                                        colors = RadioButtonDefaults.colors(selectedColor = FoodpandaPink)
                                    )
                                    Text(
                                        text = choice.choice,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (choice.price == 0.0) "Free" else String.format(Locale.US, "$ %.2f", choice.price),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                val checked = optionalChoicesList.any { it.choice == choice.choice }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (checked) {
                                                optionalChoicesList.removeAll { it.choice == choice.choice }
                                            } else {
                                                optionalChoicesList.add(choice)
                                            }
                                        }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) {
                                                optionalChoicesList.add(choice)
                                            } else {
                                                optionalChoicesList.removeAll { it.choice == choice.choice }
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = FoodpandaPink)
                                    )
                                    Text(
                                        text = choice.choice,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (choice.price == 0.0) "Free" else String.format(Locale.US, "+ $ %.2f", choice.price),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        text = "Special instructions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Please let us know if you are allergic to anything or if we need to avoid anything",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                    )
                    OutlinedTextField(
                        value = specialInstruction,
                        onValueChange = { specialInstruction = it },
                        placeholder = { Text("e.g. No mayo, less ice") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = "If this product is not available",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    availabilityOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { availabilityOption = option }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = availabilityOption == option,
                                onClick = { availabilityOption = option },
                                colors = RadioButtonDefaults.colors(selectedColor = FoodpandaPink)
                            )
                            Text(text = option, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    if (showReplaceCartDialog) {
        AlertDialog(
            onDismissRequest = { showReplaceCartDialog = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Remove your previous items?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "You still have products from another restaurant. Shall we start over with a fresh cart?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showReplaceCartDialog = false
                        onConfirmAddOrUpdate(
                            quantity,
                            requiredChoicesMap.toMap(),
                            optionalChoicesList.toList(),
                            specialInstruction,
                            availabilityOption,
                            true
                        )
                    }
                ) {
                    Text("Remove", color = FoodpandaPink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReplaceCartDialog = false }) {
                    Text("No", color = Color.DarkGray)
                }
            }
        )
    }
}
