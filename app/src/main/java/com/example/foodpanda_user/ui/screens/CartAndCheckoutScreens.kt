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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodpanda_user.R
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.data.model.Voucher
import com.example.foodpanda_user.ui.theme.FoodpandaPink
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    isCutlery: Boolean,
    appliedVoucher: Voucher?,
    onBack: () -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onEditCartItem: (CartItem) -> Unit,
    onAddMoreItems: (Shop) -> Unit,
    onCutleryChange: (Boolean) -> Unit,
    onApplyVoucherClick: () -> Unit,
    onRemoveVoucher: () -> Unit,
    onProceedToCheckout: () -> Unit
) {
    if (cartItems.isEmpty()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("empty_cart_close")) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = FoodpandaPink)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foodpanda_cart),
                    contentDescription = "Empty Cart",
                    modifier = Modifier.width(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hungry?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You haven't added anything to your cart!",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("empty_cart_browse_button")
                ) {
                    Text("Browse", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        return
    }

    val shop = cartItems.first().shop
    val subtotalPrice = remember(cartItems) { cartItems.sumOf { it.totalPrice } }
    val discountPrice = remember(subtotalPrice, appliedVoucher) {
        appliedVoucher?.calculateDiscount(subtotalPrice) ?: 0.0
    }
    val deliveryPrice = remember(shop, appliedVoucher) {
        if (appliedVoucher?.freeDelivery == true) 0.0 else shop.deliveryPrice
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(
                                text = shop.shopName,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("cart_close_button")) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = FoodpandaPink)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = FoodpandaPink)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                ProgressStepper(activeStep = 2)
            }
        },
        bottomBar = {
            CartCheckoutBottomBar(
                title = "Review payment and address",
                subtotalPrice = subtotalPrice,
                deliveryPrice = deliveryPrice,
                discountPrice = discountPrice,
                onClick = onProceedToCheckout,
                testTag = "cart_proceed_checkout_button"
            )
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
                // Estimate Card matching EstimateCard widget
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.delivery),
                            contentDescription = "Delivery",
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text("Estimated delivery", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "Standard (${shop.remainingTime} mins)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            items(cartItems, key = { it.id }) { item ->
                Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quantity Stepper Box
                        Row(
                            modifier = Modifier
                                .border(1.5.dp, Color(0xFFE0E0E0), RoundedCornerShape(6.dp))
                                .padding(horizontal = 4.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onUpdateQuantity(item.id, item.quantity - 1) },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.quantity == 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = FoodpandaPink,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = item.quantity.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = { onUpdateQuantity(item.id, item.quantity + 1) },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = FoodpandaPink,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(id = item.food.imageRes),
                            contentDescription = item.food.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onEditCartItem(item) }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onEditCartItem(item) }
                        ) {
                            Text(
                                text = item.food.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = FoodpandaPink,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (item.customizeText.isNotBlank()) {
                                Text(
                                    text = item.customizeText,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format(Locale.US, "$ %.2f", item.totalPrice),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                }
            }

            item {
                Text(
                    text = "Add more items",
                    color = FoodpandaPink,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 14.dp)
                        .clickable { onAddMoreItems(shop) }
                )
                HorizontalDivider(thickness = 8.dp, color = Color(0xFFF5F5F5))
            }

            item {
                // Subtotal & Voucher Card
                Column(modifier = Modifier.padding(15.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(String.format(Locale.US, "$ %.2f", subtotalPrice), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery fee", color = Color.Gray, fontSize = 14.sp)
                        Text(
                            text = if (deliveryPrice == 0.0) "Free" else String.format(Locale.US, "$ %.2f", deliveryPrice),
                            color = if (deliveryPrice == 0.0) FoodpandaPink else Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    if (discountPrice > 0.0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Voucher discount (${appliedVoucher?.code})", color = FoodpandaPink, fontSize = 14.sp)
                            Text(
                                text = String.format(Locale.US, "- $ %.2f", discountPrice),
                                color = FoodpandaPink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    if (appliedVoucher == null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onApplyVoucherClick)
                                .padding(vertical = 8.dp)
                                .testTag("apply_voucher_row"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocalOffer, contentDescription = null, tint = FoodpandaPink)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Apply a voucher",
                                color = FoodpandaPink,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Surface(
                            color = Color(0xFFFCE4EC),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalOffer, contentDescription = null, tint = FoodpandaPink, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(appliedVoucher.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FoodpandaPink)
                                        Text("Code: ${appliedVoucher.code}", fontSize = 11.sp, color = Color.DarkGray)
                                    }
                                }
                                Text(
                                    text = "Remove",
                                    color = FoodpandaPink,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable(onClick = onRemoveVoucher)
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(thickness = 8.dp, color = Color(0xFFF5F5F5))
            }

            item {
                // Cutlery Switch matching CutlerySwitch widget
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = FoodpandaPink)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cutlery",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (isCutlery) "The restaurant will provide cutlery if available"
                            else "Thank you for reducing plastic waste!",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = isCutlery,
                        onCheckedChange = onCutleryChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = FoodpandaPink
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    selectedAddress: Address,
    appliedVoucher: Voucher?,
    onBack: () -> Unit,
    onChangeAddressClick: () -> Unit,
    onEditInstructionClick: () -> Unit,
    onPlaceOrder: (paymentMethod: String, isContactless: Boolean) -> Unit
) {
    if (cartItems.isEmpty()) {
        onBack()
        return
    }

    var paymentMethod by remember { mutableStateOf("Cash") }
    var isContactless by remember { mutableStateOf(false) }

    val shop = cartItems.first().shop
    val subtotalPrice = remember(cartItems) { cartItems.sumOf { it.totalPrice } }
    val discountPrice = remember(subtotalPrice, appliedVoucher) {
        appliedVoucher?.calculateDiscount(subtotalPrice) ?: 0.0
    }
    val deliveryPrice = remember(shop, appliedVoucher) {
        if (appliedVoucher?.freeDelivery == true) 0.0 else shop.deliveryPrice
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(shop.shopName, fontSize = 12.sp, color = Color.Gray)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("checkout_back_button")) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                ProgressStepper(activeStep = 3)
            }
        },
        bottomBar = {
            CartCheckoutBottomBar(
                title = "Place order",
                subtotalPrice = subtotalPrice,
                deliveryPrice = deliveryPrice,
                discountPrice = discountPrice,
                onClick = { onPlaceOrder(paymentMethod, isContactless) },
                testTag = "place_order_button"
            )
        },
        containerColor = Color(0xFFF8F8F8)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Delivery Address Card
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = FoodpandaPink)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Delivery address",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = onChangeAddressClick,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Address", tint = FoodpandaPink)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            // Map Preview visual card with marker
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.pattern),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    alpha = 0.25f
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(
                                        painter = painterResource(id = R.drawable.location_user_marker),
                                        contentDescription = "Pin",
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Surface(
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp),
                                        shadowElevation = 2.dp
                                    ) {
                                        Text(
                                            text = "${selectedAddress.area}, ${selectedAddress.province}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "${selectedAddress.houseNumber} ${selectedAddress.street}".trim(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(selectedAddress.province, fontSize = 13.sp, color = Color.Gray)
                        }

                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onEditInstructionClick)
                                .padding(horizontal = 15.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = FoodpandaPink, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedAddress.deliveryInstruction.ifBlank { "Add delivery instructions" },
                                color = if (selectedAddress.deliveryInstruction.isBlank()) FoodpandaPink else Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Contactless delivery: rider leaves order at your door",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Switch(
                                checked = isContactless,
                                onCheckedChange = { isContactless = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = FoodpandaPink
                                )
                            )
                        }
                    }
                }
            }

            item {
                // Payment Method Card
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = FoodpandaPink)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Payment method",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        listOf("Cash", "Credit / Debit Card (Visa •••• 4242)").forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = method }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = paymentMethod == method,
                                    onClick = { paymentMethod = method },
                                    colors = RadioButtonDefaults.colors(selectedColor = FoodpandaPink)
                                )
                                Icon(
                                    imageVector = if (method == "Cash") Icons.Default.Money else Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = method,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Order Summary Card
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = FoodpandaPink)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Order summary",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        cartItems.forEach { cart ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${cart.quantity}x ${cart.food.name}", fontSize = 14.sp)
                                    if (cart.customizeText.isNotBlank()) {
                                        Text(cart.customizeText, fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(String.format(Locale.US, "$ %.2f", cart.totalPrice), fontSize = 14.sp)
                            }
                        }
                        HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 8.dp))
                        SummaryRow("Subtotal", String.format(Locale.US, "$ %.2f", subtotalPrice))
                        Spacer(modifier = Modifier.height(6.dp))
                        SummaryRow("Delivery fee", if (deliveryPrice == 0.0) "Free" else String.format(Locale.US, "$ %.2f", deliveryPrice))
                        if (discountPrice > 0.0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            SummaryRow("Discount", String.format(Locale.US, "- $ %.2f", discountPrice), isHighlight = true)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "By placing your order you agree to our Terms & Conditions and Privacy Policy.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isHighlight) FoodpandaPink else Color(0xFF616161)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) FoodpandaPink else Color(0xFF616161)
        )
    }
}

@Composable
fun ProgressStepper(activeStep: Int) {
    val steps = listOf("Menu", "Cart", "Checkout")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, label ->
                val stepNum = index + 1
                val isCompletedOrActive = stepNum <= activeStep
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = if (index < steps.lastIndex) Modifier.weight(1f) else Modifier
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isCompletedOrActive) FoodpandaPink else Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stepNum.toString(),
                                color = if (isCompletedOrActive) Color.White else Color.DarkGray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (stepNum == activeStep) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCompletedOrActive) FoodpandaPink else Color.Gray
                        )
                    }
                    if (index < steps.lastIndex) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 6.dp)
                                .height(2.dp)
                                .background(if (stepNum < activeStep) FoodpandaPink else Color(0xFFE0E0E0))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartCheckoutBottomBar(
    title: String,
    subtotalPrice: Double,
    deliveryPrice: Double,
    discountPrice: Double,
    onClick: () -> Unit,
    testTag: String
) {
    val total = (subtotalPrice + deliveryPrice - discountPrice).coerceAtLeast(0.0)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 15.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("(incl. VAT)", fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = String.format(Locale.US, "$ %.2f", total),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = FoodpandaPink
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag(testTag)
            ) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
