package com.example.foodpanda_user.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.BannerItem
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Choice
import com.example.foodpanda_user.data.model.Food
import com.example.foodpanda_user.data.model.FoodOrder
import com.example.foodpanda_user.data.model.Menu
import com.example.foodpanda_user.data.model.Order
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.data.model.UserProfile
import com.example.foodpanda_user.data.model.Voucher
import com.example.foodpanda_user.data.repository.FoodpandaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt

class FoodpandaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FoodpandaRepository(application.applicationContext)

    private val _user = MutableStateFlow(repository.loadUser())
    val user: StateFlow<UserProfile> = _user.asStateFlow()

    private val _addresses = MutableStateFlow(repository.loadAddresses())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _selectedAddress = MutableStateFlow(_addresses.value.first())
    val selectedAddress: StateFlow<Address> = _selectedAddress.asStateFlow()

    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops: StateFlow<List<Shop>> = _shops.asStateFlow()

    val banners: List<BannerItem> = repository.banners

    private val _cart = MutableStateFlow(repository.loadCart())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _isCutlery = MutableStateFlow(true)
    val isCutlery: StateFlow<Boolean> = _isCutlery.asStateFlow()

    private val _appliedVoucher = MutableStateFlow<Voucher?>(null)
    val appliedVoucher: StateFlow<Voucher?> = _appliedVoucher.asStateFlow()

    private val _vouchers = MutableStateFlow(repository.loadVouchers())
    val vouchers: StateFlow<List<Voucher>> = _vouchers.asStateFlow()

    private val _usedVoucherIds = MutableStateFlow<Set<String>>(emptySet())
    val usedVoucherIds: StateFlow<Set<String>> = _usedVoucherIds.asStateFlow()

    private val _currentOrder = MutableStateFlow<Order?>(null)
    val currentOrder: StateFlow<Order?> = _currentOrder.asStateFlow()

    private val _orderHistory = MutableStateFlow(repository.loadOrderHistory())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _recentSearches = MutableStateFlow(repository.loadRecentSearches())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _favoriteShopIds = MutableStateFlow<Set<String>>(setOf("shop_can_caffee", "shop_burger_house"))
    val favoriteShopIds: StateFlow<Set<String>> = _favoriteShopIds.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        refreshShopsForAddress(_selectedAddress.value)
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    private fun refreshShopsForAddress(address: Address) {
        val updated = repository.allShops.map { shop ->
            val distance = repository.calculateDistanceKm(
                address.latitude,
                address.longitude,
                shop.latitude,
                shop.longitude
            )
            val delivPrice = if (shop.deliveryPrice == 0.0) 0.0 else repository.calculateDeliveryPrice(distance)
            val estTime = when {
                distance < 1.0 -> 12
                distance < 2.0 -> 18
                distance < 3.0 -> 22
                else -> 28
            }
            shop.copy(deliveryPrice = delivPrice, remainingTime = estTime)
        }
        _shops.value = updated
    }

    fun getShopById(shopId: String): Shop {
        return _shops.value.find { it.uid == shopId } ?: repository.allShops.first()
    }

    fun getMenusForShop(shopId: String): List<Menu> {
        return repository.getMenusForShop(getShopById(shopId))
    }

    fun getFoodById(shopId: String, foodId: String): Food? {
        return getMenusForShop(shopId).flatMap { it.foods }.find { it.id == foodId }
    }

    fun toggleFavoriteShop(shopId: String) {
        val current = _favoriteShopIds.value.toMutableSet()
        if (current.contains(shopId)) {
            current.remove(shopId)
            showMessage("Removed from Favourites")
        } else {
            current.add(shopId)
            showMessage("Added to Favourites")
        }
        _favoriteShopIds.value = current
    }

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
        refreshShopsForAddress(address)
    }

    fun saveOrUpdateAddress(address: Address) {
        val list = _addresses.value.toMutableList()
        val idx = list.indexOfFirst { it.id == address.id }
        if (idx >= 0) {
            list[idx] = address
        } else {
            list.add(0, address)
        }
        _addresses.value = list
        repository.saveAddresses(list)
        _selectedAddress.value = address
        refreshShopsForAddress(address)
        showMessage("Address saved")
    }

    fun deleteAddress(addressId: String) {
        if (_addresses.value.size <= 1) {
            showMessage("You must keep at least one delivery address")
            return
        }
        val updated = _addresses.value.filterNot { it.id == addressId }
        _addresses.value = updated
        repository.saveAddresses(updated)
        if (_selectedAddress.value.id == addressId) {
            _selectedAddress.value = updated.first()
            refreshShopsForAddress(updated.first())
        }
    }

    fun hasDifferentShopInCart(shopUid: String): Boolean {
        return _cart.value.isNotEmpty() && _cart.value.first().shop.uid != shopUid
    }

    fun addOrUpdateCartItem(
        shop: Shop,
        food: Food,
        categoryId: String,
        quantity: Int,
        requiredChoices: Map<String, Choice>,
        optionalChoices: List<Choice>,
        specialInstruction: String,
        availabilityOption: String,
        editingCartItemId: String? = null,
        clearExistingFirst: Boolean = false
    ) {
        val currentList = if (clearExistingFirst) mutableListOf() else _cart.value.toMutableList()
        val hasVariation = food.customizations.any { it.isVariation && requiredChoices.containsKey(it.id) }
        val basePrice = if (hasVariation) 0.0 else food.price
        val reqSum = requiredChoices.values.sumOf { it.price }
        val optSum = optionalChoices.sumOf { it.price }
        val unitPrice = (((basePrice + reqSum + optSum) * 100.0).roundToInt()) / 100.0

        val captions = buildList {
            requiredChoices.values.forEach { add(it.choice) }
            optionalChoices.forEach { add(it.choice) }
            if (specialInstruction.isNotBlank()) add("Note: $specialInstruction")
        }.joinToString(", ")

        val newItem = CartItem(
            id = editingCartItemId ?: UUID.randomUUID().toString(),
            shop = shop,
            food = food,
            customizeText = captions,
            unitPrice = unitPrice,
            quantity = quantity,
            categoryId = categoryId,
            selectedRequiredChoices = requiredChoices,
            selectedOptionalChoices = optionalChoices,
            specialInstruction = specialInstruction,
            availabilityOption = availabilityOption
        )

        if (editingCartItemId != null) {
            val idx = currentList.indexOfFirst { it.id == editingCartItemId }
            if (idx >= 0) {
                currentList[idx] = newItem
            } else {
                currentList.add(newItem)
            }
        } else {
            val existingIdx = currentList.indexOfFirst {
                it.food.id == food.id && it.customizeText == captions
            }
            if (existingIdx >= 0) {
                val existing = currentList[existingIdx]
                currentList[existingIdx] = existing.copy(quantity = existing.quantity + quantity)
            } else {
                currentList.add(newItem)
            }
        }

        _cart.value = currentList
        repository.saveCart(currentList)
        showMessage("Added ${food.name} to cart")
    }

    fun updateCartItemQuantity(cartItemId: String, newQuantity: Int) {
        val currentList = _cart.value.toMutableList()
        val idx = currentList.indexOfFirst { it.id == cartItemId }
        if (idx >= 0) {
            if (newQuantity <= 0) {
                currentList.removeAt(idx)
            } else {
                currentList[idx] = currentList[idx].copy(quantity = newQuantity)
            }
            _cart.value = currentList
            repository.saveCart(currentList)
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
        _appliedVoucher.value = null
        repository.saveCart(emptyList())
    }

    fun setCutlery(enabled: Boolean) {
        _isCutlery.value = enabled
    }

    fun applyVoucher(voucher: Voucher?) {
        _appliedVoucher.value = voucher
        if (voucher != null) {
            showMessage("Voucher ${voucher.code} applied!")
        }
    }

    fun addVoucherByCode(codeInput: String): Boolean {
        val normalized = codeInput.trim().uppercase()
        if (normalized.isBlank()) return false
        val existing = _vouchers.value.find { it.code.equals(normalized, ignoreCase = true) }
        if (existing != null) {
            _appliedVoucher.value = existing
            showMessage("Voucher ${existing.code} selected!")
            return true
        }
        val newVoucher = Voucher(
            id = "v_${normalized.lowercase()}",
            code = normalized,
            name = "Promo Code $normalized ($1.50 OFF)",
            isNewUser = false,
            startingDate = System.currentTimeMillis(),
            expiredDate = System.currentTimeMillis() + 30L * 86_400_000L,
            createdDate = System.currentTimeMillis(),
            minPrice = 3.0,
            discountPrice = 1.50,
            freeDelivery = false
        )
        val updated = listOf(newVoucher) + _vouchers.value
        _vouchers.value = updated
        repository.saveVouchers(updated)
        showMessage("Voucher $normalized added to your wallet!")
        return true
    }

    fun placeOrder(paymentMethod: String = "Cash", isContactless: Boolean = false): Order? {
        val items = _cart.value
        if (items.isEmpty()) return null
        if (_currentOrder.value != null && _currentOrder.value?.isDelivered == false) {
            showMessage("Please wait for the current order to finish!")
            return null
        }

        val shop = items.first().shop
        val subtotal = (((items.sumOf { it.totalPrice }) * 100.0).roundToInt()) / 100.0
        val voucher = _appliedVoucher.value
        val discount = voucher?.calculateDiscount(subtotal)?.let {
            ((it * 100.0).roundToInt()) / 100.0
        } ?: 0.0
        val deliveryFee = if (voucher?.freeDelivery == true) 0.0 else shop.deliveryPrice

        val foodOrders = items.map {
            FoodOrder(
                foodName = it.food.name,
                quantity = it.quantity,
                foodPrice = it.unitPrice,
                customize = it.customizeText
            )
        }

        val newOrder = Order(
            id = "#fp-${(10000..99999).random()}",
            user = _user.value,
            address = _selectedAddress.value,
            shop = shop,
            foodOrders = foodOrders,
            subtotalPrice = subtotal,
            deliveryPrice = deliveryFee,
            discountPrice = discount,
            voucherId = voucher?.id,
            paymentMethod = paymentMethod,
            isContactless = isContactless,
            isPaid = paymentMethod != "Cash",
            isShopAccept = false,
            isRiderAccept = false,
            isPickup = false,
            isNear = false,
            isDelivered = false,
            isCutlery = _isCutlery.value,
            time = System.currentTimeMillis()
        )

        if (voucher != null) {
            _usedVoucherIds.value = _usedVoucherIds.value + voucher.id
        }

        _currentOrder.value = newOrder
        clearCart()
        simulateOrderProgress(newOrder.id)
        return newOrder
    }

    private fun simulateOrderProgress(orderId: String) {
        viewModelScope.launch {
            delay(6000)
            advanceOrderStageIfMatches(orderId, step = 1)
            delay(8000)
            advanceOrderStageIfMatches(orderId, step = 2)
            delay(8000)
            advanceOrderStageIfMatches(orderId, step = 3)
        }
    }

    private fun advanceOrderStageIfMatches(orderId: String, step: Int) {
        val current = _currentOrder.value ?: return
        if (current.id != orderId || current.isDelivered) return
        _currentOrder.value = when (step) {
            1 -> current.copy(isShopAccept = true)
            2 -> current.copy(isShopAccept = true, isRiderAccept = true, isPickup = true)
            3 -> current.copy(isShopAccept = true, isRiderAccept = true, isPickup = true, isNear = true)
            else -> current
        }
    }

    fun advanceCurrentOrderManually() {
        val current = _currentOrder.value ?: return
        val next = when {
            !current.isShopAccept -> current.copy(isShopAccept = true)
            !current.isPickup -> current.copy(isShopAccept = true, isRiderAccept = true, isPickup = true)
            !current.isNear -> current.copy(isShopAccept = true, isRiderAccept = true, isPickup = true, isNear = true)
            !current.isDelivered -> {
                val delivered = current.copy(
                    isShopAccept = true,
                    isRiderAccept = true,
                    isPickup = true,
                    isNear = true,
                    isDelivered = true,
                    deliveredTime = System.currentTimeMillis()
                )
                val updatedHistory = listOf(delivered) + _orderHistory.value.filterNot { it.id == delivered.id }
                _orderHistory.value = updatedHistory
                repository.saveOrderHistory(updatedHistory)
                delivered
            }
            else -> current
        }
        _currentOrder.value = next
    }

    fun dismissDeliveredOrder() {
        val current = _currentOrder.value ?: return
        if (!current.isDelivered) {
            val delivered = current.copy(
                isDelivered = true,
                deliveredTime = System.currentTimeMillis()
            )
            val updatedHistory = listOf(delivered) + _orderHistory.value.filterNot { it.id == delivered.id }
            _orderHistory.value = updatedHistory
            repository.saveOrderHistory(updatedHistory)
        }
        _currentOrder.value = null
    }

    fun reorderFromHistory(order: Order) {
        val shop = getShopById(order.shop.uid)
        val shopFoods = getMenusForShop(shop.uid).flatMap { it.foods }
        val newCart = order.foodOrders.mapIndexed { idx, fo ->
            val matchedFood = shopFoods.find { it.name == fo.foodName } ?: Food(
                id = "reorder_${idx}",
                name = fo.foodName,
                description = order.shop.shopDescription,
                imageRes = shop.shopImageRes,
                price = fo.foodPrice,
                comparePrice = fo.foodPrice * 1.2
            )
            CartItem(
                id = UUID.randomUUID().toString(),
                shop = shop,
                food = matchedFood,
                customizeText = fo.customize,
                unitPrice = fo.foodPrice,
                quantity = fo.quantity,
                categoryId = "cat_reorder"
            )
        }
        _cart.value = newCart
        repository.saveCart(newCart)
        showMessage("Items from ${order.shop.shopName} added to your cart!")
    }

    fun addSearchQuery(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return
        val updated = (listOf(trimmed) + _recentSearches.value.filterNot { it.equals(trimmed, ignoreCase = true) }).take(8)
        _recentSearches.value = updated
        repository.saveRecentSearches(updated)
    }

    fun removeRecentSearch(query: String) {
        val updated = _recentSearches.value.filterNot { it == query }
        _recentSearches.value = updated
        repository.saveRecentSearches(updated)
    }

    fun signIn(name: String, email: String, phone: String) {
        val updated = UserProfile(
            uid = "user_${email.hashCode()}",
            name = name.ifBlank { "Foodpanda User" },
            email = email.ifBlank { "user@foodpanda.kh" },
            phoneNumber = phone.ifBlank { "+855 12 345 678" },
            isSignedIn = true
        )
        _user.value = updated
        repository.saveUser(updated)
        showMessage("Welcome back, ${updated.name}!")
    }

    fun signOut() {
        val signedOut = _user.value.copy(isSignedIn = false)
        _user.value = signedOut
        repository.saveUser(signedOut)
        showMessage("Signed out")
    }
}
