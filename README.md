# Foodpanda (Android - Kotlin & Jetpack Compose)

A native Android food & grocery delivery application built with **Kotlin** and **Jetpack Compose** (Material 3), migrated and upgraded from the original `foodpanda_user` project.

## Features
- **Home & Service Collage**: Quick access to **Food delivery**, **Shops (pandamart & bakery)**, **Pick-up**, and **pandasend**, plus **Popular Restaurants**, **Campaigns & Daily Deals**, and **Favourites** filtering.
- **Navigation Drawer & Auth**: User profile header, **Sign up / Log in** modal (Google, Facebook, and Email options), **Vouchers & offers**, **Favourites**, **Orders & reordering**, **Addresses**, and **Log out**.
- **Shop Details & Sticky Category Tabs**: Restaurant cover header, delivery time pill, discount cards, scrollable category tabs synced with menu sections, and sticky bottom cart bar.
- **Food Customization**: Required single-choice variations (size/portion, sugar/spice level), optional multi-choice toppings/add-ons, special instructions, product availability preferences, and cross-restaurant cart replacement confirmation.
- **Cart & Checkout**: Step progress indicator (`Menu -> Cart -> Checkout`), estimated delivery time card, inline item quantity stepper, cutlery switch, voucher application & discount calculation, delivery address & instructions editor, contactless delivery switch, and Cash/Card payment selection.
- **Live Order Tracking & Order History**: 4-stage animated delivery progress bar, live rider route preview, interactive step simulator to test delivery progression, and one-tap **Reorder** from past orders.

## Tech Stack
- **Language**: Kotlin (JDK 21)
- **UI**: Jetpack Compose + Material Design 3 + Custom `Poppins` Typography
- **Build System**: Gradle 9.3.1 (Kotlin DSL), Android Gradle Plugin 9.1.1, Android SDK 36
- **Architecture**: MVVM (`FoodpandaViewModel` + `StateFlow` + `FoodpandaRepository` with `SharedPreferences` & `kotlinx.serialization`)
