package com.example.foodpanda_user

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.BannerItem
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Food
import com.example.foodpanda_user.ui.screens.AddressScreen
import com.example.foodpanda_user.ui.screens.AuthModalSheet
import com.example.foodpanda_user.ui.screens.BannerScreen
import com.example.foodpanda_user.ui.screens.CartScreen
import com.example.foodpanda_user.ui.screens.CheckoutScreen
import com.example.foodpanda_user.ui.screens.CustomizeScreen
import com.example.foodpanda_user.ui.screens.EditAddressScreen
import com.example.foodpanda_user.ui.screens.FoodDeliveryScreen
import com.example.foodpanda_user.ui.screens.HomeScreen
import com.example.foodpanda_user.ui.screens.OrderHistoryScreen
import com.example.foodpanda_user.ui.screens.OrderScreen
import com.example.foodpanda_user.ui.screens.SearchScreen
import com.example.foodpanda_user.ui.screens.ShopDetailScreen
import com.example.foodpanda_user.ui.screens.VoucherScreen
import com.example.foodpanda_user.ui.theme.FoodpandaPink
import com.example.foodpanda_user.ui.theme.FoodpandaTheme
import com.example.foodpanda_user.viewmodel.FoodpandaViewModel
import kotlinx.coroutines.delay

sealed interface AppDestination {
    data object Home : AppDestination
    data class FoodDelivery(val category: String) : AppDestination
    data class BannerDetail(val banner: BannerItem) : AppDestination
    data object Search : AppDestination
    data class ShopDetail(val shopUid: String) : AppDestination
    data class CustomizeFood(
        val shopUid: String,
        val categoryId: String,
        val food: Food,
        val editingCartItem: CartItem? = null
    ) : AppDestination
    data object Cart : AppDestination
    data object Checkout : AppDestination
    data object ActiveOrder : AppDestination
    data object OrderHistory : AppDestination
    data object Vouchers : AppDestination
    data object Addresses : AppDestination
    data class EditAddress(val address: Address?) : AppDestination
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodpandaTheme {
                FoodpandaApp()
            }
        }
    }
}

@Composable
fun FoodpandaApp(viewModel: FoodpandaViewModel = viewModel()) {
    var showSplash by remember { mutableStateOf(true) }
    val backStack = remember { mutableStateListOf<AppDestination>(AppDestination.Home) }
    var showAuthSheet by remember { mutableStateOf(false) }

    val user by viewModel.user.collectAsStateWithLifecycle()
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle()
    val shops by viewModel.shops.collectAsStateWithLifecycle()
    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val isCutlery by viewModel.isCutlery.collectAsStateWithLifecycle()
    val appliedVoucher by viewModel.appliedVoucher.collectAsStateWithLifecycle()
    val vouchers by viewModel.vouchers.collectAsStateWithLifecycle()
    val usedVoucherIds by viewModel.usedVoucherIds.collectAsStateWithLifecycle()
    val currentOrder by viewModel.currentOrder.collectAsStateWithLifecycle()
    val orderHistory by viewModel.orderHistory.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()
    val favoriteShopIds by viewModel.favoriteShopIds.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        delay(900)
        showSplash = false
    }

    LaunchedEffect(snackbarMessage) {
        val msg = snackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    fun navigateTo(dest: AppDestination) {
        backStack.add(dest)
    }

    fun popBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    if (showSplash) {
        SplashScreenContent()
        return
    }

    val currentDestination = backStack.lastOrNull() ?: AppDestination.Home

    BackHandler(enabled = backStack.size > 1) {
        popBack()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = FoodpandaPink,
                    contentColor = Color.White
                )
            }
        }
    ) { _ ->
        when (val screen = currentDestination) {
            is AppDestination.Home -> {
                HomeScreen(
                    user = user,
                    selectedAddress = selectedAddress,
                    addresses = addresses,
                    shops = shops,
                    banners = viewModel.banners,
                    cartItems = cart,
                    currentOrder = currentOrder,
                    favoriteShopIds = favoriteShopIds,
                    onSelectAddress = { viewModel.selectAddress(it) },
                    onNavigateToAddresses = { navigateTo(AppDestination.Addresses) },
                    onNavigateToFoodDelivery = { cat -> navigateTo(AppDestination.FoodDelivery(cat)) },
                    onNavigateToShop = { shop -> navigateTo(AppDestination.ShopDetail(shop.uid)) },
                    onNavigateToCart = { navigateTo(AppDestination.Cart) },
                    onNavigateToSearch = { navigateTo(AppDestination.Search) },
                    onNavigateToVouchers = { navigateTo(AppDestination.Vouchers) },
                    onNavigateToOrderHistory = { navigateTo(AppDestination.OrderHistory) },
                    onNavigateToActiveOrder = { navigateTo(AppDestination.ActiveOrder) },
                    onNavigateToBanner = { banner -> navigateTo(AppDestination.BannerDetail(banner)) },
                    onToggleFavorite = { viewModel.toggleFavoriteShop(it) },
                    onShowAuthModal = { showAuthSheet = true },
                    onSignOut = { viewModel.signOut() },
                    onShowMessage = { viewModel.showMessage(it) }
                )
            }

            is AppDestination.FoodDelivery -> {
                FoodDeliveryScreen(
                    initialCategory = screen.category,
                    selectedAddress = selectedAddress,
                    shops = shops,
                    banners = viewModel.banners,
                    cartItems = cart,
                    favoriteShopIds = favoriteShopIds,
                    onBack = { popBack() },
                    onNavigateToShop = { shop -> navigateTo(AppDestination.ShopDetail(shop.uid)) },
                    onNavigateToBanner = { banner -> navigateTo(AppDestination.BannerDetail(banner)) },
                    onNavigateToCart = { navigateTo(AppDestination.Cart) },
                    onNavigateToSearch = { navigateTo(AppDestination.Search) },
                    onToggleFavorite = { viewModel.toggleFavoriteShop(it) }
                )
            }

            is AppDestination.BannerDetail -> {
                BannerScreen(
                    banner = screen.banner,
                    shops = shops,
                    favoriteShopIds = favoriteShopIds,
                    onBack = { popBack() },
                    onApplyCode = { code -> viewModel.addVoucherByCode(code) },
                    onNavigateToShop = { shop -> navigateTo(AppDestination.ShopDetail(shop.uid)) },
                    onToggleFavorite = { viewModel.toggleFavoriteShop(it) }
                )
            }

            is AppDestination.Search -> {
                SearchScreen(
                    shops = shops,
                    recentSearches = recentSearches,
                    favoriteShopIds = favoriteShopIds,
                    onBack = { popBack() },
                    onSubmitSearch = { viewModel.addSearchQuery(it) },
                    onRemoveRecentSearch = { viewModel.removeRecentSearch(it) },
                    onNavigateToShop = { shop -> navigateTo(AppDestination.ShopDetail(shop.uid)) },
                    onToggleFavorite = { viewModel.toggleFavoriteShop(it) }
                )
            }

            is AppDestination.ShopDetail -> {
                val shop = viewModel.getShopById(screen.shopUid)
                val menus = viewModel.getMenusForShop(screen.shopUid)
                ShopDetailScreen(
                    shop = shop,
                    menus = menus,
                    cartItems = cart,
                    isFavorite = favoriteShopIds.contains(shop.uid),
                    onBack = { popBack() },
                    onToggleFavorite = { viewModel.toggleFavoriteShop(shop.uid) },
                    onShare = { viewModel.showMessage("Shared ${shop.shopName} link!") },
                    onFoodClick = { categoryId, food ->
                        navigateTo(
                            AppDestination.CustomizeFood(
                                shopUid = shop.uid,
                                categoryId = categoryId,
                                food = food
                            )
                        )
                    },
                    onViewCartClick = { navigateTo(AppDestination.Cart) }
                )
            }

            is AppDestination.CustomizeFood -> {
                val shop = viewModel.getShopById(screen.shopUid)
                CustomizeScreen(
                    shop = shop,
                    categoryId = screen.categoryId,
                    food = screen.food,
                    editingCartItem = screen.editingCartItem,
                    hasDifferentShopInCart = viewModel.hasDifferentShopInCart(shop.uid),
                    onBack = { popBack() },
                    onConfirmAddOrUpdate = { qty, req, opt, note, avail, clearFirst ->
                        viewModel.addOrUpdateCartItem(
                            shop = shop,
                            food = screen.food,
                            categoryId = screen.categoryId,
                            quantity = qty,
                            requiredChoices = req,
                            optionalChoices = opt,
                            specialInstruction = note,
                            availabilityOption = avail,
                            editingCartItemId = screen.editingCartItem?.id,
                            clearExistingFirst = clearFirst
                        )
                        popBack()
                    }
                )
            }

            is AppDestination.Cart -> {
                CartScreen(
                    cartItems = cart,
                    isCutlery = isCutlery,
                    appliedVoucher = appliedVoucher,
                    onBack = { popBack() },
                    onUpdateQuantity = { id, qty -> viewModel.updateCartItemQuantity(id, qty) },
                    onEditCartItem = { item ->
                        navigateTo(
                            AppDestination.CustomizeFood(
                                shopUid = item.shop.uid,
                                categoryId = item.categoryId,
                                food = item.food,
                                editingCartItem = item
                            )
                        )
                    },
                    onAddMoreItems = { shop ->
                        popBack()
                        if (backStack.lastOrNull() !is AppDestination.ShopDetail) {
                            navigateTo(AppDestination.ShopDetail(shop.uid))
                        }
                    },
                    onCutleryChange = { viewModel.setCutlery(it) },
                    onApplyVoucherClick = { navigateTo(AppDestination.Vouchers) },
                    onRemoveVoucher = { viewModel.applyVoucher(null) },
                    onProceedToCheckout = { navigateTo(AppDestination.Checkout) }
                )
            }

            is AppDestination.Checkout -> {
                CheckoutScreen(
                    cartItems = cart,
                    selectedAddress = selectedAddress,
                    appliedVoucher = appliedVoucher,
                    onBack = { popBack() },
                    onChangeAddressClick = { navigateTo(AppDestination.Addresses) },
                    onEditInstructionClick = { navigateTo(AppDestination.EditAddress(selectedAddress)) },
                    onPlaceOrder = { paymentMethod, isContactless ->
                        val created = viewModel.placeOrder(paymentMethod, isContactless)
                        if (created != null) {
                            backStack.clear()
                            backStack.add(AppDestination.Home)
                            backStack.add(AppDestination.ActiveOrder)
                        }
                    }
                )
            }

            is AppDestination.ActiveOrder -> {
                val active = currentOrder ?: orderHistory.firstOrNull()
                if (active == null) {
                    popBack()
                } else {
                    OrderScreen(
                        order = active,
                        onClose = { popBack() },
                        onAdvanceStage = { viewModel.advanceCurrentOrderManually() },
                        onCompleteAndDismiss = {
                            viewModel.dismissDeliveredOrder()
                            popBack()
                        },
                        onContactRider = {
                            viewModel.showMessage("Rider ${active.riderName}: On my way with your order!")
                        }
                    )
                }
            }

            is AppDestination.OrderHistory -> {
                OrderHistoryScreen(
                    orders = orderHistory,
                    onBack = { popBack() },
                    onReorder = { order ->
                        viewModel.reorderFromHistory(order)
                        popBack()
                        navigateTo(AppDestination.Cart)
                    }
                )
            }

            is AppDestination.Vouchers -> {
                VoucherScreen(
                    vouchers = vouchers,
                    usedVoucherIds = usedVoucherIds,
                    appliedVoucher = appliedVoucher,
                    onBack = { popBack() },
                    onAddVoucherCode = { viewModel.addVoucherByCode(it) },
                    onSelectVoucher = { voucher ->
                        viewModel.applyVoucher(voucher)
                        popBack()
                    }
                )
            }

            is AppDestination.Addresses -> {
                AddressScreen(
                    addresses = addresses,
                    selectedAddress = selectedAddress,
                    onBack = { popBack() },
                    onSelectAddress = { addr ->
                        viewModel.selectAddress(addr)
                        popBack()
                    },
                    onAddNewAddress = { navigateTo(AppDestination.EditAddress(null)) },
                    onEditAddress = { addr -> navigateTo(AppDestination.EditAddress(addr)) },
                    onDeleteAddress = { id -> viewModel.deleteAddress(id) }
                )
            }

            is AppDestination.EditAddress -> {
                EditAddressScreen(
                    initialAddress = screen.address,
                    onBack = { popBack() },
                    onSave = { updated ->
                        viewModel.saveOrUpdateAddress(updated)
                        popBack()
                    }
                )
            }
        }
    }

    if (showAuthSheet) {
        AuthModalSheet(
            onDismiss = { showAuthSheet = false },
            onSignInSuccess = { name, email, phone ->
                viewModel.signIn(name, email, phone)
                showAuthSheet = false
            }
        )
    }
}

@Composable
private fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodpandaPink),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Foodpanda Logo",
                modifier = Modifier.size(130.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = R.drawable.foodpanda_logo),
                contentDescription = "foodpanda",
                modifier = Modifier
                    .height(42.dp)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}
