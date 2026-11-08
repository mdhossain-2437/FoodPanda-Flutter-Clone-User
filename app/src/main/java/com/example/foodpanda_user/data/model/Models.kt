package com.example.foodpanda_user.data.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val uid: String,
    val name: String,
    val email: String,
    val phoneNumber: String = "+855 12 345 678",
    val isSignedIn: Boolean = true
)

@Serializable
data class Address(
    val id: String,
    val houseNumber: String,
    val street: String,
    val area: String,
    val province: String,
    val floor: String = "",
    val deliveryInstruction: String = "",
    val label: String = "Home",
    val latitude: Double = 11.5564,
    val longitude: Double = 104.9282
) {
    val displayTitle: String
        get() = if (label.isNotBlank()) label else "$houseNumber $street".trim()

    val displaySubtitle: String
        get() = "${if (houseNumber.isBlank()) "" else "$houseNumber "}$street, $province".trim()
}

@Serializable
data class Choice(
    val choice: String,
    val price: Double = 0.0
)

@Serializable
data class Customize(
    val id: String,
    val title: String,
    val isRequired: Boolean,
    val isVariation: Boolean,
    val choices: List<Choice>
)

@Serializable
data class Food(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val price: Double,
    val comparePrice: Double,
    val isPublished: Boolean = true,
    val isHotSale: Boolean = false,
    val customizations: List<Customize> = emptyList()
)

@Serializable
data class Category(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val isPublished: Boolean = true,
    val isHotSale: Boolean = false
)

@Serializable
data class Menu(
    val category: Category,
    val foods: List<Food>
)

@Serializable
data class Shop(
    val uid: String,
    val shopName: String,
    val shopDescription: String,
    val remainingTime: Int = 20,
    val deliveryPrice: Double = 0.5,
    @DrawableRes val shopImageRes: Int,
    val rating: Double = 4.5,
    val totalRating: Int = 320,
    val houseNumber: String = "128",
    val street: String = "St. 320",
    val area: String = "Boeung Keng Kang",
    val latitude: Double = 11.5520,
    val longitude: Double = 104.9260,
    val province: String = "Phnom Penh",
    val floor: String = "G",
    val isApproved: Boolean = true,
    val promoBanner: String = "Use code HELLOPANDA for 50% off your first order from $4.50+",
    val discountTitle: String = "30% Discount",
    val discountSubtitle: String = "On the entire menu",
    val categoryTag: String = "Food delivery"
)

@Serializable
data class CartItem(
    val id: String,
    val shop: Shop,
    val food: Food,
    val customizeText: String,
    val unitPrice: Double,
    val quantity: Int,
    val categoryId: String,
    val selectedRequiredChoices: Map<String, Choice> = emptyMap(),
    val selectedOptionalChoices: List<Choice> = emptyList(),
    val specialInstruction: String = "",
    val availabilityOption: String = "Remove it from my order"
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}

@Serializable
data class Voucher(
    val id: String,
    val code: String,
    val name: String,
    val isNewUser: Boolean = false,
    val startingDate: Long,
    val expiredDate: Long,
    val createdDate: Long,
    val minPrice: Double = 0.0,
    val discountPrice: Double? = null,
    val discountPercentage: Double? = null,
    val freeDelivery: Boolean = false
) {
    fun calculateDiscount(subtotal: Double): Double {
        if (subtotal < minPrice) return 0.0
        return when {
            discountPrice != null -> discountPrice.coerceAtMost(subtotal)
            discountPercentage != null -> ((discountPercentage / 100.0) * subtotal)
            else -> 0.0
        }
    }
}

@Serializable
data class BannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val promoCode: String,
    @DrawableRes val imageRes: Int,
    val shopUids: List<String>
)

@Serializable
data class FoodOrder(
    val foodName: String,
    val quantity: Int,
    val foodPrice: Double,
    val customize: String
)

@Serializable
data class Order(
    val id: String,
    val user: UserProfile,
    val address: Address,
    val shop: Shop,
    val foodOrders: List<FoodOrder>,
    val subtotalPrice: Double,
    val deliveryPrice: Double,
    val discountPrice: Double,
    val voucherId: String? = null,
    val paymentMethod: String = "Cash",
    val isContactless: Boolean = false,
    val isPaid: Boolean = false,
    val isShopAccept: Boolean = false,
    val isRiderAccept: Boolean = false,
    val isPickup: Boolean = false,
    val isNear: Boolean = false,
    val isDelivered: Boolean = false,
    val isCancelled: Boolean = false,
    val isCutlery: Boolean = true,
    val time: Long,
    val deliveredTime: Long = 0L,
    val riderName: String = "Sokha Meas"
) {
    val finalTotal: Double
        get() = (subtotalPrice + deliveryPrice - discountPrice).coerceAtLeast(0.0)
}
