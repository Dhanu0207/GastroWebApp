# 🍽️ GastroFullStack - Next-Gen Food Delivery Platform

[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-8.0-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-v4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)

A modern, full-stack food delivery application built with **Spring Boot (Java 21)** backend, **React 19 + TypeScript + Vite** frontend, and styled with a sleek **Dark Glassmorphism Design System**.

---

## ✨ Features

### 🛒 Client & Discovery
- **Restaurant & Menu Explorer**: Multi-category filter (Burgers, Ramen, Sushi, Desserts), search, and Veg/Non-Veg filters.
- **Dynamic Interactive Cart**: Live subtotal, flat delivery fee, GST tax breakdown, and promo coupon system (`GASTRO50`).
- **👥 Collaborative Group Cart**: Real-time room creation with shareable session codes, multi-user ordering, and **automated per-person split bill calculator**.
- **Checkout & Multi-Mode Payments**: Delivery address management, Razorpay gateway support, instant UPI QR, and Cash on Delivery (COD).
- **🛰️ Live Order Tracking HUD**: Real-time 5-stage animated progress stepper (`PLACED` ➔ `CONFIRMED` ➔ `PREPARING` ➔ `OUT_FOR_DELIVERY` ➔ `DELIVERED`), driver contact action, and step simulator.
- **Past Order History**: Full receipt history and 1-click **Reorder** functionality.

### 🛡️ Security & Role-Based Access Control (RBAC)
- **JWT Stateless Authentication**: Secure token verification via `JwtAuthenticationFilter`.
- **4 App Roles**: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_RESTAURANT_OWNER`, `ROLE_DELIVERY_PARTNER`.
- **1-Click Demo Accounts**: Pre-seeded demo credentials for instant testing.

### 👨‍🍳 Kitchen & Admin Dashboard
- **Live Order Stream**: Real-time incoming orders with kitchen dispatch actions.
- **Menu Inventory**: Dynamic in-stock / out-of-stock toggles for dishes.
- **Platform Analytics**: Revenue, operational kitchens, and average preparation time KPIs.

---

## 🏗️ System Architecture

```
gastrofullstack/
├── src/main/java/com/fooddelivery/foodbackend/
│   ├── config/              # SecurityConfig, DataInitializer, RazorpayConfig
│   ├── controller/          # 11 REST Controllers (Auth, Menu, Orders, GroupCart, Admin, etc.)
│   ├── dto/                 # Data transfer objects & request/response payloads
│   ├── entity/              # 13 JPA Entities (User, Restaurant, MenuItem, Order, Cart, etc.)
│   ├── repository/          # Spring Data JPA repositories
│   ├── security/            # JWT Service & Custom UserDetails
│   ├── service/             # Service interfaces & implementations
│   └── util/                # File uploads & helpers
│
└── frontend/
    ├── src/
    │   ├── api/             # Axios client & REST endpoints with offline fallbacks
    │   ├── components/      # GlassNavbar, FloatingCartWidget, LiveOrderStepper, FoodCards
    │   ├── pages/           # HomePage, RestaurantDetailPage, CartPage, GroupCartPage,
    │   │                    # CheckoutPage, OrderTrackingPage, OrderHistoryPage, AdminDashboardPage
    │   ├── store/           # Zustand state management (authStore, cartStore, groupCartStore)
    │   └── styles/          # Glassmorphism design tokens and animations
```

---

## ⚡ Quick Start Guide

### 1. Prerequisites
- **Java 21** & **Maven**
- **Node.js 18+** & **npm**
- **MySQL Database** running locally on port `3306`

### 2. Backend Setup
1. Configure database credentials in `src/main/resources/application.properties` (or set environment variables).
2. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The application will automatically run `DataInitializer` to seed demo users, restaurants, and menu items.*

### 3. Frontend Setup
1. Navigate to the frontend folder and install dependencies:
   ```bash
   cd frontend
   npm install
   ```
2. Start the development server:
   ```bash
   npm run dev
   ```
3. Open your browser at `http://localhost:5173`.

---

## 🔑 Demo Login Credentials

| Role | Email | Password |
| :--- | :--- | :--- |
| **Customer** | `customer@gastro.com` | `password123` |
| **Admin** | `admin@gastro.com` | `password123` |
| **Restaurant Owner** | `owner@gastro.com` | `password123` |

---

## 📄 License
This project is open-source and available under the MIT License.
