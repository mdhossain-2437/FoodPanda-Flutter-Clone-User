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
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.foodpanda_user.data.model.Order
import com.example.foodpanda_user.data.model.Voucher
import com.example.foodpanda_user.ui.theme.FoodpandaPink
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    order: Order,
    onClose: () -> Unit,
    onAdvanceStage: () -> Unit,
    onCompleteAndDismiss: () -> Unit,
    onContactRider: () -> Unit
) {
    var showDetailBreakdown by remember { mutableStateOf(true) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val timeLabel = when {
        order.isDelivered -> "Delivered"
        order.isNear -> "Anytime now"
        order.isPickup -> "10 mins"
        order.isShopAccept -> "10 - 15 mins"
        else -> "5 - 15 mins"
    }

    val statusDescription = when {
        order.isDelivered -> "Enjoy!"
        order.isNear -> "Anytime now! Look out for your rider!"
        order.isPickup -> "Your rider has picked up your order"
        order.isShopAccept -> "Preparing your order. Your rider will pick it up once it's ready."
        else -> "Got your order ${order.user.name.substringBefore(" ")}!"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Your order", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(order.shop.shopName, fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose, modifier = Modifier.testTag("order_screen_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FoodpandaPink)
                    }
                },
                actions = {
                    TextButton(onClick = { showHelpDialog = true }) {
                        Text("Help", color = FoodpandaPink, fontWeight = FontWeight.SemiBold)
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
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(FoodpandaPink)
                )
            }

            if (order.isPickup && !order.isDelivered) {
                item {
                    // Rider live route map visualization when picked up
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(28.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.location_shop_marker),
                                    contentDescription = "Shop",
                                    modifier = Modifier.size(38.dp)
                                )
                                Text("Shop", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.location_rider_marker),
                                    contentDescription = "Rider",
                                    modifier = Modifier.size(44.dp)
                                )
                                Text("Rider (${order.riderName})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FoodpandaPink)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.location_user_marker),
                                    contentDescription = "You",
                                    modifier = Modifier.size(38.dp)
                                )
                                Text("You", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!order.isPickup || order.isDelivered) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.foodpanda_location),
                                contentDescription = "Order status",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Text("Estimated delivery time", fontSize = 15.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = timeLabel,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    if (!order.isDelivered) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CustomLinearProgress(
                                isProgress = !order.isShopAccept,
                                isDone = order.isShopAccept,
                                modifier = Modifier.width(50.dp)
                            )
                            CustomLinearProgress(
                                isProgress = order.isShopAccept && !order.isPickup,
                                isDone = order.isPickup,
                                modifier = Modifier.weight(1f)
                            )
                            CustomLinearProgress(
                                isProgress = order.isPickup && !order.isNear,
                                isDone = order.isNear,
                                modifier = Modifier.weight(1f)
                            )
                            CustomLinearProgress(
                                isProgress = order.isNear && !order.isDelivered,
                                isDone = order.isDelivered,
                                modifier = Modifier.width(70.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    Text(
                        text = statusDescription,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color(0xFF424242)
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    if (!order.isDelivered) {
                        OutlinedButton(
                            onClick = onAdvanceStage,
                            border = BorderStroke(1.dp, FoodpandaPink),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("advance_order_stage_button")
                        ) {
                            Text("Simulate Next Delivery Step", color = FoodpandaPink, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Button(
                            onClick = onCompleteAndDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Back to Home", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                HorizontalDivider(thickness = 10.dp, color = Color(0xFFF5F5F5))
            }

            if (order.isPickup && !order.isDelivered) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .clickable(onClick = onContactRider),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Row(
                            modifier = Modifier.padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.foodpanda_panda),
                                contentDescription = "Rider",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Contact your rider (${order.riderName})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Ask for contactless delivery", color = Color.Gray, fontSize = 12.sp)
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = "Message Rider",
                                tint = FoodpandaPink,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(15.dp)) {
                    Text("Order Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    OrderDetailRow(
                        label = "Order number",
                        value = order.id,
                        isTag = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderDetailRow(
                        label = "Order from",
                        value = order.shop.shopName,
                        highlight = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderDetailRow(
                        label = "Delivery address",
                        value = "${order.address.houseNumber} ${order.address.street}, ${order.address.province}"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderDetailRow(
                        label = "Total (incl. VAT)",
                        value = String.format(Locale.US, "$ %.2f", order.finalTotal)
                    )
                }
                HorizontalDivider(color = Color(0xFFEEEEEE))
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDetailBreakdown = !showDetailBreakdown }
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Details (${order.foodOrders.sumOf { it.quantity }} items)",
                            fontWeight = FontWeight.SemiBold,
                            color = FoodpandaPink
                        )
                        Icon(
                            imageVector = if (showDetailBreakdown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = FoodpandaPink
                        )
                    }
                    if (showDetailBreakdown) {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp)) {
                            order.foodOrders.forEach { fo ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${fo.quantity}x ${fo.foodName}", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                        if (fo.customize.isNotBlank()) {
                                            Text(fo.customize, fontSize = 12.sp, color = Color.Gray)
                                        }
                                    }
                                    Text(String.format(Locale.US, "$ %.2f", fo.foodPrice * fo.quantity), fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                }
            }
        }
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            containerColor = Color.White,
            title = { Text("Order Support (${order.id})", style = MaterialTheme.typography.titleLarge) },
            text = {
                Text(
                    "Need help with your order from ${order.shop.shopName}? Our support team and rider ${order.riderName} are actively monitoring your delivery.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Got it", color = FoodpandaPink, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun OrderDetailRow(
    label: String,
    value: String,
    isTag: Boolean = false,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(label, fontSize = 14.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.width(16.dp))
        if (isTag) {
            Surface(
                color = Color(0xFFEEEEEE),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        } else {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (highlight) FoodpandaPink else Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    orders: List<Order>,
    onBack: () -> Unit,
    onReorder: (Order) -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders & reordering", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("order_history_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FoodpandaPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.browse_restaurant_icon),
                    contentDescription = "Browse Restaurants",
                    modifier = Modifier.width(90.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text("Browse Restaurants", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You don't have any orders yet. Try one of our awesome restaurants and place your first order!",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Browse restaurants in your area", color = Color.White)
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
                        text = "Past orders",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                items(orders, key = { it.id }) { order ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = order.shop.shopImageRes),
                                    contentDescription = order.shop.shopName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = order.shop.shopName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = String.format(Locale.US, "$ %.2f", order.finalTotal),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = dateFormatter.format(Date(if (order.deliveredTime != 0L) order.deliveredTime else order.time)),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = order.foodOrders.joinToString(", ") { "${it.quantity}x ${it.foodName}" },
                                        fontSize = 13.sp,
                                        color = Color(0xFF616161),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { onReorder(order) },
                                colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .testTag("reorder_button_${order.id}")
                            ) {
                                Text("Select items to reorder", color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoucherScreen(
    vouchers: List<Voucher>,
    usedVoucherIds: Set<String>,
    appliedVoucher: Voucher?,
    onBack: () -> Unit,
    onAddVoucherCode: (String) -> Unit,
    onSelectVoucher: (Voucher) -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("latest") }
    var showSortSheet by remember { mutableStateOf(false) }

    val sortedVouchers = remember(vouchers, sortBy) {
        when (sortBy) {
            "expiring" -> vouchers.sortedBy { it.expiredDate }
            "minimum" -> vouchers.sortedBy { it.minPrice }
            else -> vouchers.sortedByDescending { it.createdDate }
        }
    }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.US) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vouchers & Offers", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("voucher_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Add a voucher code box matching VoucherSaving widget
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text("Add a voucher code", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = codeInput,
                                onValueChange = { codeInput = it },
                                placeholder = { Text("Enter code (e.g. HELLOPANDA)") },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("voucher_code_input")
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    if (codeInput.isNotBlank()) {
                                        onAddVoucherCode(codeInput)
                                        codeInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(54.dp)
                            ) {
                                Text("Add", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (sortedVouchers.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.voucher_panda),
                            contentDescription = "No Vouchers",
                            modifier = Modifier.width(90.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("No Vouchers Yet", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("It seems you have no vouchers yet.", color = Color.Gray)
                    }
                }
            } else {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { showSortSheet = true },
                            shape = RoundedCornerShape(20.dp),
                            color = if (sortBy == "latest") Color.White else FoodpandaPink,
                            border = BorderStroke(1.dp, if (sortBy == "latest") Color.Gray else FoodpandaPink)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (sortBy) {
                                        "expiring" -> "Expiring first"
                                        "minimum" -> "Lowest minimum order value"
                                        else -> "Sort"
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (sortBy == "latest") Color.Black else Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = if (sortBy == "latest") Color.Black else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (sortBy != "latest") {
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Clear All",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { sortBy = "latest" }
                            )
                        }
                    }
                }

                items(sortedVouchers, key = { it.id }) { voucher ->
                    val isUsed = usedVoucherIds.contains(voucher.id)
                    val isApplied = appliedVoucher?.id == voucher.id
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(
                            1.dp,
                            if (isApplied) FoodpandaPink else Color(0xFFE0E0E0)
                        )
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.voucher_panda),
                                    contentDescription = null,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(voucher.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        text = buildString {
                                            append("Code: ${voucher.code}")
                                            if (voucher.minPrice > 0) append(" • Min. order $ ${String.format(Locale.US, "%.2f", voucher.minPrice)}")
                                            if (voucher.freeDelivery) append(" • Free Delivery")
                                        },
                                        fontSize = 12.sp,
                                        color = FoodpandaPink,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Button(
                                    onClick = { onSelectVoucher(voucher) },
                                    enabled = !isUsed,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isApplied) Color(0xFF2E7D32) else FoodpandaPink
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = when {
                                            isUsed -> "Used"
                                            isApplied -> "Applied"
                                            else -> "Use"
                                        },
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEEEE))
                            Text(
                                text = "Valid until ${dateFormatter.format(Date(voucher.expiredDate))}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSortSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSortSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Text("Sort vouchers by", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                listOf(
                    "latest" to "Latest added",
                    "expiring" to "Expiring first",
                    "minimum" to "Lowest minimum order value"
                ).forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                sortBy = key
                                showSortSheet = false
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = sortBy == key,
                            onClick = {
                                sortBy = key
                                showSortSheet = false
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = FoodpandaPink)
                        )
                        Text(label, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    addresses: List<Address>,
    selectedAddress: Address,
    onBack: () -> Unit,
    onSelectAddress: (Address) -> Unit,
    onAddNewAddress: () -> Unit,
    onEditAddress: (Address) -> Unit,
    onDeleteAddress: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Addresses", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("addresses_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(15.dp)
                ) {
                    Button(
                        onClick = onAddNewAddress,
                        colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("add_new_address_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add new address", color = Color.White, fontWeight = FontWeight.Bold)
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
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(addresses, key = { it.id }) { addr ->
                val isSelected = addr.id == selectedAddress.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAddress(addr) },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFFF8FA) else Color.White
                    ),
                    border = BorderStroke(1.dp, if (isSelected) FoodpandaPink else Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = FoodpandaPink,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(addr.displayTitle, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(addr.displaySubtitle, fontSize = 13.sp, color = Color.DarkGray)
                            if (addr.floor.isNotBlank()) {
                                Text("Floor/Unit: ${addr.floor}", fontSize = 12.sp, color = Color.Gray)
                            }
                            if (addr.deliveryInstruction.isNotBlank()) {
                                Text("Note to rider: ${addr.deliveryInstruction}", fontSize = 12.sp, color = FoodpandaPink)
                            }
                        }
                        IconButton(onClick = { onEditAddress(addr) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = FoodpandaPink, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { onDeleteAddress(addr.id) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    initialAddress: Address?,
    onBack: () -> Unit,
    onSave: (Address) -> Unit
) {
    var houseNumber by remember { mutableStateOf(initialAddress?.houseNumber ?: "") }
    var street by remember { mutableStateOf(initialAddress?.street ?: "") }
    var area by remember { mutableStateOf(initialAddress?.area ?: "Boeung Keng Kang") }
    var province by remember { mutableStateOf(initialAddress?.province ?: "Phnom Penh") }
    var floor by remember { mutableStateOf(initialAddress?.floor ?: "") }
    var instruction by remember { mutableStateOf(initialAddress?.deliveryInstruction ?: "") }
    var label by remember { mutableStateOf(initialAddress?.label ?: "Home") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialAddress == null) "Add New Address" else "Edit Address",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FoodpandaPink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(color = Color.White, shadowElevation = 8.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(15.dp)
                ) {
                    Button(
                        onClick = {
                            if (street.isNotBlank()) {
                                onSave(
                                    Address(
                                        id = initialAddress?.id ?: "addr_${UUID.randomUUID()}",
                                        houseNumber = houseNumber.trim(),
                                        street = street.trim(),
                                        area = area.trim(),
                                        province = province.trim().ifBlank { "Phnom Penh" },
                                        floor = floor.trim(),
                                        deliveryInstruction = instruction.trim(),
                                        label = label
                                    )
                                )
                            }
                        },
                        enabled = street.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_address_button")
                    ) {
                        Text("Save and continue", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location_user_marker),
                        contentDescription = "Pin",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = houseNumber,
                        onValueChange = { houseNumber = it },
                        label = { Text("House / Bldg #") },
                        singleLine = true,
                        modifier = Modifier.weight(0.4f)
                    )
                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Street *") },
                        singleLine = true,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Area / District") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = floor,
                    onValueChange = { floor = it },
                    label = { Text("Floor / Unit (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = instruction,
                    onValueChange = { instruction = it },
                    label = { Text("Delivery instructions for rider") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text("Add a label", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf("Home", "Work", "Partner", "Other").forEach { item ->
                        FilterChip(
                            selected = label == item,
                            onClick = { label = item },
                            label = { Text(item) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FoodpandaPink,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthModalSheet(
    onDismiss: () -> Unit,
    onSignInSuccess: (name: String, email: String, phone: String) -> Unit
) {
    var showEmailForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Sokha Chan") }
    var email by remember { mutableStateOf("sokha.chan@foodpanda.kh") }
    var phone by remember { mutableStateOf("+855 12 889 900") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = if (showEmailForm) "Continue with Email" else "Sign up or Log in",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (!showEmailForm) {
                // Google Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            onSignInSuccess("Sokha Chan", "sokha.chan@gmail.com", "+855 12 889 900")
                        },
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = "Google",
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.CenterStart)
                        )
                        Text(
                            text = "Continue with Google",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF616161),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Facebook Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            onSignInSuccess("Sokha Chan", "sokha.fb@foodpanda.kh", "+855 12 889 900")
                        },
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF3B5998)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook_icon),
                            contentDescription = "Facebook",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterStart)
                        )
                        Text(
                            text = "Continue with Facebook",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Email Button
                OutlinedButton(
                    onClick = { showEmailForm = true },
                    border = BorderStroke(1.dp, FoodpandaPink),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Continue with email", color = FoodpandaPink, fontWeight = FontWeight.SemiBold)
                }
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { onSignInSuccess(name, email, phone) },
                    colors = ButtonDefaults.buttonColors(containerColor = FoodpandaPink),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Log in / Sign up", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
