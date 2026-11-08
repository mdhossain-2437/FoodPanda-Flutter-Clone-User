package com.example.foodpanda_user.data.repository

import android.content.Context
import com.example.foodpanda_user.R
import com.example.foodpanda_user.data.model.Address
import com.example.foodpanda_user.data.model.BannerItem
import com.example.foodpanda_user.data.model.CartItem
import com.example.foodpanda_user.data.model.Category
import com.example.foodpanda_user.data.model.Choice
import com.example.foodpanda_user.data.model.Customize
import com.example.foodpanda_user.data.model.Food
import com.example.foodpanda_user.data.model.FoodOrder
import com.example.foodpanda_user.data.model.Menu
import com.example.foodpanda_user.data.model.Order
import com.example.foodpanda_user.data.model.Shop
import com.example.foodpanda_user.data.model.UserProfile
import com.example.foodpanda_user.data.model.Voucher
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class FoodpandaRepository(context: Context) {

    private val prefs = context.getSharedPreferences("foodpanda_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val drinkCustomizations = listOf(
        Customize(
            id = "cust_size",
            title = "Variation / Size",
            isRequired = true,
            isVariation = true,
            choices = listOf(
                Choice("Regular (M)", 1.33),
                Choice("Large (L)", 1.75),
                Choice("Jumbo (XL)", 2.15)
            )
        ),
        Customize(
            id = "cust_sugar",
            title = "Sugar Level",
            isRequired = true,
            isVariation = false,
            choices = listOf(
                Choice("Normal Sugar (100%)", 0.0),
                Choice("Less Sugar (70%)", 0.0),
                Choice("Half Sugar (50%)", 0.0),
                Choice("No Sugar (0%)", 0.0)
            )
        ),
        Customize(
            id = "cust_toppings",
            title = "Extra Toppings",
            isRequired = false,
            isVariation = false,
            choices = listOf(
                Choice("Golden Brown Sugar Pearl", 0.35),
                Choice("Cream Cheese Foam", 0.50),
                Choice("Coconut Jelly", 0.30),
                Choice("Grass Jelly", 0.30)
            )
        )
    )

    private val mealCustomizations = listOf(
        Customize(
            id = "cust_portion",
            title = "Choose Portion",
            isRequired = true,
            isVariation = true,
            choices = listOf(
                Choice("A La Carte", 3.50),
                Choice("Regular Combo (Fries + Drink)", 4.90),
                Choice("Upsized Combo (Large Fries + Drink)", 5.60)
            )
        ),
        Customize(
            id = "cust_spice",
            title = "Spice Level",
            isRequired = true,
            isVariation = false,
            choices = listOf(
                Choice("Original (Non-Spicy)", 0.0),
                Choice("Medium Spicy", 0.0),
                Choice("Extra Hot & Crispy", 0.0)
            )
        ),
        Customize(
            id = "cust_addons",
            title = "Add-ons",
            isRequired = false,
            isVariation = false,
            choices = listOf(
                Choice("Extra Cheddar Slice", 0.60),
                Choice("Crispy Bacon Strip", 0.90),
                Choice("Garlic Mayo Dip", 0.35),
                Choice("Sweet Chili Sauce", 0.25)
            )
        )
    )

    val allShops: List<Shop> = listOf(
        Shop(
            uid = "shop_can_caffee",
            shopName = "CAN Caffee (ខេនកាហ្វេតែគុជ)",
            shopDescription = "Bubble Tea • Coffee • Beverages",
            remainingTime = 15,
            deliveryPrice = 0.0,
            shopImageRes = R.drawable.food,
            rating = 4.6,
            totalRating = 331,
            houseNumber = "42",
            street = "St. 310 (Indradevi)",
            area = "Boeung Keng Kang",
            latitude = 11.5558,
            longitude = 104.9275,
            province = "Phnom Penh",
            promoBanner = "ប្រើកូដ HELLOPANDA បញ្ចុះតម្លៃ 50% សម្រាប់ការកុម្ម៉ង់លើកដំបូងចាប់ពី $4.5 ឡើងទៅ",
            discountTitle = "30% Discount",
            discountSubtitle = "On the entire menu",
            categoryTag = "Food delivery"
        ),
        Shop(
            uid = "shop_burger_house",
            shopName = "Smash Burger & Grill House",
            shopDescription = "Burgers • American • Fast Food",
            remainingTime = 20,
            deliveryPrice = 0.45,
            shopImageRes = R.drawable.burger,
            rating = 4.8,
            totalRating = 512,
            houseNumber = "88",
            street = "Preah Monivong Blvd",
            area = "Daun Penh",
            latitude = 11.5620,
            longitude = 104.9220,
            province = "Phnom Penh",
            promoBanner = "Free delivery on orders over $6.00 + 20% off combos",
            discountTitle = "20% OFF Combos",
            discountSubtitle = "Automatic discount at checkout",
            categoryTag = "Food delivery"
        ),
        Shop(
            uid = "shop_pizza_napoli",
            shopName = "Pizza Napoli Artisan Oven",
            shopDescription = "Pizza • Italian • Pasta",
            remainingTime = 25,
            deliveryPrice = 0.60,
            shopImageRes = R.drawable.pizza,
            rating = 4.7,
            totalRating = 428,
            houseNumber = "15",
            street = "St. 240",
            area = "Chaktomuk",
            latitude = 11.5590,
            longitude = 104.9310,
            province = "Phnom Penh",
            promoBanner = "Buy 1 Get 1 Free on Medium & Large Artisan Pizzas",
            discountTitle = "Buy 1 Get 1",
            discountSubtitle = "Selected Classic Pizzas",
            categoryTag = "Pick-up"
        ),
        Shop(
            uid = "shop_crispy_chicken",
            shopName = "Golden Crispy Fried Chicken",
            shopDescription = "Fried Chicken • Korean Wings • Fries",
            remainingTime = 18,
            deliveryPrice = 0.0,
            shopImageRes = R.drawable.chicken,
            rating = 4.5,
            totalRating = 289,
            houseNumber = "204",
            street = "Kampuchea Krom Blvd",
            area = "7 Makara",
            latitude = 11.5665,
            longitude = 104.9140,
            province = "Phnom Penh",
            promoBanner = "Hot & Spicy Wings Bucket 25% OFF today",
            discountTitle = "25% Discount",
            discountSubtitle = "On Family & Party Buckets",
            categoryTag = "Food delivery"
        ),
        Shop(
            uid = "shop_dimsum_wok",
            shopName = "Golden Dragon Dim Sum & Wok",
            shopDescription = "Chinese • Dim Sum • Roast Duck",
            remainingTime = 22,
            deliveryPrice = 0.50,
            shopImageRes = R.drawable.chinese,
            rating = 4.7,
            totalRating = 195,
            houseNumber = "66",
            street = "Mao Tse Toung Blvd",
            area = "Toul Tompoung",
            latitude = 11.5440,
            longitude = 104.9180,
            province = "Phnom Penh",
            promoBanner = "Use code HELLOPANDA for $2.50 off handmade dim sum",
            discountTitle = "15% OFF",
            discountSubtitle = "All Dim Sum Platter Sets",
            categoryTag = "Pick-up"
        ),
        Shop(
            uid = "shop_phnom_penh_noodle",
            shopName = "Master Noodle & Ramen Bar",
            shopDescription = "Noodles • Asian • Soup",
            remainingTime = 15,
            deliveryPrice = 0.30,
            shopImageRes = R.drawable.noddle,
            rating = 4.6,
            totalRating = 374,
            houseNumber = "19",
            street = "St. 51 (Pasteur)",
            area = "Boeung Keng Kang 1",
            latitude = 11.5535,
            longitude = 104.9295,
            province = "Phnom Penh",
            promoBanner = "Hand-pulled Broth Noodles with Free Gyoza Starter",
            discountTitle = "Free Starter",
            discountSubtitle = "With any 2 Signature Bowls",
            categoryTag = "Food delivery"
        ),
        Shop(
            uid = "shop_sweet_patisserie",
            shopName = "La Rose Cake & Patisserie",
            shopDescription = "Bakery • Cakes • Desserts",
            remainingTime = 20,
            deliveryPrice = 0.40,
            shopImageRes = R.drawable.cake,
            rating = 4.9,
            totalRating = 260,
            houseNumber = "102",
            street = "Norodom Blvd",
            area = "Tonle Bassac",
            latitude = 11.5490,
            longitude = 104.9320,
            province = "Phnom Penh",
            promoBanner = "Freshly baked Strawberry Shortcake & Macarons 20% OFF",
            discountTitle = "20% Discount",
            discountSubtitle = "Whole Cakes & Pastry Boxes",
            categoryTag = "Shops"
        ),
        Shop(
            uid = "shop_pandamart",
            shopName = "pandamart Groceries (BKK)",
            shopDescription = "Groceries • Snacks • Household • Fresh Produce",
            remainingTime = 15,
            deliveryPrice = 0.0,
            shopImageRes = R.drawable.pandamart,
            rating = 4.8,
            totalRating = 640,
            houseNumber = "312",
            street = "St. 320",
            area = "Boeung Keng Kang",
            latitude = 11.5560,
            longitude = 104.9280,
            province = "Phnom Penh",
            promoBanner = "25-minute express grocery delivery straight to your door",
            discountTitle = "Free Delivery",
            discountSubtitle = "On all pandamart orders today",
            categoryTag = "Shops"
        )
    )

    private val menusByShop: Map<String, List<Menu>> = mapOf(
        "shop_can_caffee" to listOf(
            Menu(
                category = Category("cat_popular", "ពេញនិយម (Popular)", "លក់ដាច់ជាងគេពេលនេះ (Best sellers right now)", isHotSale = true),
                foods = listOf(
                    Food(
                        id = "food_can_1",
                        name = "សូកូឡាដូងក្រអូប (Fragrant Coconut Cocoa)",
                        description = "Rich Belgian cocoa layered with fresh aromatic young coconut milk and brown sugar pearls.",
                        imageRes = R.drawable.food,
                        price = 1.33,
                        comparePrice = 1.90,
                        isHotSale = true,
                        customizations = drinkCustomizations
                    ),
                    Food(
                        id = "food_can_2",
                        name = "តែគុជស្ករត្នោត (Palm Sugar Boba Milk Tea)",
                        description = "Caramelized Cambodian palm sugar swirled with creamy organic fresh milk and warm chewy tapioca.",
                        imageRes = R.drawable.cake,
                        price = 1.50,
                        comparePrice = 2.10,
                        isHotSale = true,
                        customizations = drinkCustomizations
                    ),
                    Food(
                        id = "food_can_3",
                        name = "កាហ្វេទឹកដោះគោទឹកកក (Iced Condensed Milk Coffee)",
                        description = "Bold slow-dripped Mondulkiri roast coffee over sweet condensed milk.",
                        imageRes = R.drawable.food,
                        price = 1.25,
                        comparePrice = 1.75,
                        isHotSale = false,
                        customizations = drinkCustomizations
                    )
                )
            ),
            Menu(
                category = Category("cat_bogo", "ទិញ១ថែម១ (Buy 1 Get 1)", "រងចាំយ៉ាងតិច 20នាទី (Freshly brewed)"),
                foods = listOf(
                    Food(
                        id = "food_can_4",
                        name = "តែបៃតងម៉ាត់ឆាឡាតេ (Uji Matcha Latte Duo)",
                        description = "Two cups of whisked ceremonial grade Uji matcha over chilled milk.",
                        imageRes = R.drawable.food,
                        price = 2.40,
                        comparePrice = 3.80,
                        isHotSale = true,
                        customizations = drinkCustomizations
                    ),
                    Food(
                        id = "food_can_5",
                        name = "តែផាសិនផ្លែឈើស្រស់ (Tropical Passion Fruit Tea)",
                        description = "Refreshing jasmine green tea infused with fresh passion fruit seeds and lime slices.",
                        imageRes = R.drawable.cake,
                        price = 1.45,
                        comparePrice = 2.00,
                        isHotSale = false,
                        customizations = drinkCustomizations
                    )
                )
            ),
            Menu(
                category = Category("cat_milktea", "តែទឹកដោះគោ (Milk Tea)", "លក់ដាច់ជាងគេពេលនេះ"),
                foods = listOf(
                    Food(
                        id = "food_can_6",
                        name = "តែថៃក្រហមឈីសហ្វូម (Thai Red Tea Cheese Foam)",
                        description = "Aromatic Ceylon & Thai red tea topped with whipped salted mascarpone cheese foam.",
                        imageRes = R.drawable.food,
                        price = 1.65,
                        comparePrice = 2.20,
                        isHotSale = true,
                        customizations = drinkCustomizations
                    ),
                    Food(
                        id = "food_can_7",
                        name = "ត្រាវទឹកដោះគោស្រស់ (Fresh Taro Cream Milk)",
                        description = "Hand-mashed real taro paste blended with silky fresh milk.",
                        imageRes = R.drawable.cake,
                        price = 1.60,
                        comparePrice = 2.15,
                        isHotSale = false,
                        customizations = drinkCustomizations
                    )
                )
            )
        ),
        "shop_burger_house" to listOf(
            Menu(
                category = Category("cat_burger_pop", "Popular Smash Burgers", "Most ordered right now", isHotSale = true),
                foods = listOf(
                    Food(
                        id = "food_bg_1",
                        name = "Double Truffle Angus Smash Burger",
                        description = "Two seared Australian Angus beef patties, double aged cheddar, caramelized onions, and black truffle aioli on brioche.",
                        imageRes = R.drawable.burger,
                        price = 4.50,
                        comparePrice = 5.90,
                        isHotSale = true,
                        customizations = mealCustomizations
                    ),
                    Food(
                        id = "food_bg_2",
                        name = "Crispy Nashville Hot Chicken Burger",
                        description = "Buttermilk fried chicken thigh glazed in cayenne chili oil, house pickles, and ranch slaw.",
                        imageRes = R.drawable.chicken,
                        price = 3.95,
                        comparePrice = 4.80,
                        isHotSale = true,
                        customizations = mealCustomizations
                    ),
                    Food(
                        id = "food_bg_3",
                        name = "Classic Smokehouse BBQ Bacon Burger",
                        description = "Smoked streaky bacon, crispy onion rings, melted American cheese, and hickory BBQ glaze.",
                        imageRes = R.drawable.burger,
                        price = 4.25,
                        comparePrice = 5.40,
                        isHotSale = false,
                        customizations = mealCustomizations
                    )
                )
            ),
            Menu(
                category = Category("cat_burger_sides", "Loaded Fries & Sides", "Crispy golden sides"),
                foods = listOf(
                    Food(
                        id = "food_bg_4",
                        name = "Cajun Parmesan Truffle Fries",
                        description = "Skin-on russet fries tossed in Cajun seasoning, grated Parmigiano, and parsley.",
                        imageRes = R.drawable.burger,
                        price = 2.20,
                        comparePrice = 2.90,
                        isHotSale = false,
                        customizations = mealCustomizations
                    ),
                    Food(
                        id = "food_bg_5",
                        name = "6pc Golden Mozzarella Sticks",
                        description = "Served warm with slow-simmered San Marzano marinara dip.",
                        imageRes = R.drawable.pizza,
                        price = 2.80,
                        comparePrice = 3.50,
                        isHotSale = true,
                        customizations = mealCustomizations
                    )
                )
            )
        )
    )

    fun getMenusForShop(shop: Shop): List<Menu> {
        menusByShop[shop.uid]?.let { return it }
        return listOf(
            Menu(
                category = Category("cat_sig_${shop.uid}", "Signature Popular", "Chef's best-selling favorites", isHotSale = true),
                foods = listOf(
                    Food(
                        id = "${shop.uid}_f1",
                        name = "${shop.shopName.substringBefore(" ")} Special Platter",
                        description = "Freshly prepared signature dish from ${shop.shopName} with house special sauce.",
                        imageRes = shop.shopImageRes,
                        price = 3.50,
                        comparePrice = 4.80,
                        isHotSale = true,
                        customizations = mealCustomizations
                    ),
                    Food(
                        id = "${shop.uid}_f2",
                        name = "Family Combo Set for 2",
                        description = "Includes 2 main specialties, 2 sides, and 2 chilled drinks.",
                        imageRes = R.drawable.food,
                        price = 6.90,
                        comparePrice = 9.20,
                        isHotSale = true,
                        customizations = mealCustomizations
                    ),
                    Food(
                        id = "${shop.uid}_f3",
                        name = "Chef's Crispy Appetizer Box",
                        description = "Golden crispy bites served with tangy house dipping sauce.",
                        imageRes = R.drawable.chicken,
                        price = 2.60,
                        comparePrice = 3.40,
                        isHotSale = false,
                        customizations = mealCustomizations
                    )
                )
            ),
            Menu(
                category = Category("cat_drinks_${shop.uid}", "Drinks & Desserts", "Sweet treats & refreshments"),
                foods = listOf(
                    Food(
                        id = "${shop.uid}_f4",
                        name = "Signature Iced Milk Tea",
                        description = "Brewed premium tea with creamy milk and brown sugar pearls.",
                        imageRes = R.drawable.cake,
                        price = 1.50,
                        comparePrice = 2.00,
                        isHotSale = false,
                        customizations = drinkCustomizations
                    )
                )
            )
        )
    }

    val banners: List<BannerItem> = listOf(
        BannerItem(
            id = "banner_1",
            title = "50% OFF First Order",
            subtitle = "Use code HELLOPANDA on orders from $4.50+",
            promoCode = "HELLOPANDA",
            imageRes = R.drawable.burger,
            shopUids = listOf("shop_can_caffee", "shop_burger_house", "shop_crispy_chicken")
        ),
        BannerItem(
            id = "banner_2",
            title = "Local Hero Deals • Up to 30% OFF",
            subtitle = "Support top-rated local favorites with zero delivery fee",
            promoCode = "PANDA30",
            imageRes = R.drawable.pizza,
            shopUids = listOf("shop_pizza_napoli", "shop_dimsum_wok", "shop_phnom_penh_noodle")
        ),
        BannerItem(
            id = "banner_3",
            title = "pandamart Express Groceries",
            subtitle = "Fresh produce, snacks & bakery in 15 mins",
            promoCode = "FREEDEL",
            imageRes = R.drawable.pandamart,
            shopUids = listOf("shop_pandamart", "shop_sweet_patisserie")
        )
    )

    private val defaultVouchers: List<Voucher> = listOf(
        Voucher(
            id = "v_hellopanda",
            code = "HELLOPANDA",
            name = "Welcome 50% OFF Deal",
            isNewUser = true,
            startingDate = 1767225600000L,
            expiredDate = 1893456000000L,
            createdDate = 1767225600000L,
            minPrice = 4.50,
            discountPercentage = 50.0,
            freeDelivery = true
        ),
        Voucher(
            id = "v_panda30",
            code = "PANDA30",
            name = "30% OFF Menu Favorites",
            isNewUser = false,
            startingDate = 1767225600000L,
            expiredDate = 1861920000000L,
            createdDate = 1767312000000L,
            minPrice = 3.00,
            discountPercentage = 30.0,
            freeDelivery = false
        ),
        Voucher(
            id = "v_save2",
            code = "SAVE2",
            name = "$2.00 Instant Discount",
            isNewUser = false,
            startingDate = 1767225600000L,
            expiredDate = 1830297600000L,
            createdDate = 1767398400000L,
            minPrice = 5.00,
            discountPrice = 2.00,
            freeDelivery = false
        )
    )

    private val defaultAddresses: List<Address> = listOf(
        Address(
            id = "addr_home",
            houseNumber = "320",
            street = "St. 320",
            area = "Boeung Keng Kang 1",
            province = "Phnom Penh",
            floor = "2nd Floor",
            deliveryInstruction = "Call when arriving at the front gate",
            label = "Home",
            latitude = 11.5564,
            longitude = 104.9282
        ),
        Address(
            id = "addr_work",
            houseNumber = "85",
            street = "Preah Norodom Blvd",
            area = "Daun Penh",
            province = "Phnom Penh",
            floor = "Tower B, 5th Floor",
            deliveryInstruction = "Leave with reception desk",
            label = "Work",
            latitude = 11.5680,
            longitude = 104.9265
        ),
        Address(
            id = "addr_partner",
            houseNumber = "14",
            street = "Russian Federation Blvd",
            area = "Toul Kork",
            province = "Phnom Penh",
            floor = "",
            deliveryInstruction = "",
            label = "Partner",
            latitude = 11.5710,
            longitude = 104.8980
        )
    )

    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val p = 0.017453292519943295
        val a = 0.5 - cos((lat2 - lat1) * p) / 2 +
            cos(lat1 * p) * cos(lat2 * p) * (1 - cos((lon2 - lon1) * p)) / 2
        val km = 12742 * atan2(sqrt(a), sqrt(1 - a))
        return ((km * 100.0).roundToInt()) / 100.0
    }

    fun calculateDeliveryPrice(distanceKm: Double): Double {
        val raw = when {
            distanceKm <= 0.5 -> 0.0
            distanceKm <= 1.0 -> 0.30
            else -> distanceKm * 0.30
        }
        return ((raw * 100.0).roundToInt()) / 100.0
    }

    fun loadUser(): UserProfile {
        val raw = prefs.getString("user_profile", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<UserProfile>(raw) }
        }
        return UserProfile(
            uid = "user_panda_01",
            name = "Sokha Chan",
            email = "sokha.chan@foodpanda.kh",
            phoneNumber = "+855 12 889 900",
            isSignedIn = true
        )
    }

    fun saveUser(user: UserProfile) {
        prefs.edit().putString("user_profile", json.encodeToString(user)).apply()
    }

    fun loadAddresses(): List<Address> {
        val raw = prefs.getString("addresses_list", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<List<Address>>(raw) }
        }
        return defaultAddresses
    }

    fun saveAddresses(list: List<Address>) {
        prefs.edit().putString("addresses_list", json.encodeToString(list)).apply()
    }

    fun loadCart(): List<CartItem> {
        val raw = prefs.getString("cart_items", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<List<CartItem>>(raw) }
        }
        return emptyList()
    }

    fun saveCart(items: List<CartItem>) {
        prefs.edit().putString("cart_items", json.encodeToString(items)).apply()
    }

    fun loadVouchers(): List<Voucher> {
        val raw = prefs.getString("vouchers_list", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<List<Voucher>>(raw) }
        }
        return defaultVouchers
    }

    fun saveVouchers(list: List<Voucher>) {
        prefs.edit().putString("vouchers_list", json.encodeToString(list)).apply()
    }

    fun loadOrderHistory(): List<Order> {
        val raw = prefs.getString("order_history", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<List<Order>>(raw) }
        }
        val sampleOrder = Order(
            id = "#fp-84920",
            user = loadUser(),
            address = defaultAddresses.first(),
            shop = allShops.first(),
            foodOrders = listOf(
                FoodOrder("សូកូឡាដូងក្រអូប (Fragrant Coconut Cocoa)", 2, 1.33, "Regular (M), Less Sugar (70%)"),
                FoodOrder("តែគុជស្ករត្នោត (Palm Sugar Boba Milk Tea)", 1, 1.85, "Large (L), Golden Brown Sugar Pearl")
            ),
            subtotalPrice = 4.51,
            deliveryPrice = 0.0,
            discountPrice = 0.0,
            isPaid = true,
            isShopAccept = true,
            isRiderAccept = true,
            isPickup = true,
            isNear = true,
            isDelivered = true,
            time = System.currentTimeMillis() - 86_400_000L,
            deliveredTime = System.currentTimeMillis() - 85_000_000L
        )
        return listOf(sampleOrder)
    }

    fun saveOrderHistory(orders: List<Order>) {
        prefs.edit().putString("order_history", json.encodeToString(orders)).apply()
    }

    fun loadRecentSearches(): List<String> {
        val raw = prefs.getString("recent_searches", null)
        if (raw != null) {
            runCatching { return json.decodeFromString<List<String>>(raw) }
        }
        return listOf("Bubble Tea", "Burger", "Pizza", "Dim Sum", "Crispy Chicken")
    }

    fun saveRecentSearches(searches: List<String>) {
        prefs.edit().putString("recent_searches", json.encodeToString(searches)).apply()
    }
}
