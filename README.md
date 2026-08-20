# 🍽️ GastroWebApp - Next-Gen Food Delivery Platform

[![React](https://img.shields.io/badge/React-19.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-8.0-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![TailwindCSS](https://img.shields.io/badge/Tailwind_CSS-v4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)

**GastroWebApp** is a modern full-stack food delivery web application. Built with a high-performance **Spring Boot 3.5 (Java 21)** backend, a **React 19 + TypeScript + Vite** frontend, and designed with a **Dark Glassmorphic UI/UX**.

---

## 🌟 Highlighted Core Features

### 🛒 1. Discovery & Dining Experience
- **Interactive Restaurant & Menu Explorer**: Browse gourmet kitchens by cuisine, search dishes in real-time, and filter by dietary preference (Pure Veg vs. Non-Veg).
- **Dynamic Interactive Cart**: Live subtotal calculations, flat delivery fee, GST tax breakdown, and promo coupon system (e.g. `GASTRO50` for 15% off).
- **Persistent Floating Cart HUD**: Bottom-right floating drawer accessible from any page with live item count, member avatars, and quick checkout access.

### 👥 2. Collaborative Real-Time Group Cart
- **Shareable Session Rooms**: Create a group cart room tied to a restaurant and generate unique session codes (e.g., `GASTRO-8821`).
- **Multi-Member Ordering**: Friends or coworkers can join using the session code and independently add their favorite dishes.
- **Automated Live Split-Bill Calculator**: Dynamically calculates each individual member's exact item cost plus their proportional share of delivery fees and taxes.

### 💳 3. Multi-Mode Checkout & Payment
- **Saved Address Selector**: Choose between multiple tagged addresses (Home, Work) or add new delivery locations.
- **Multi-Gateway Payment Support**:
  - ⚡ **Razorpay Gateway**: Integrated online card, netbanking, and wallet payments.
  - 📱 **Instant UPI QR**: Google Pay, PhonePe, Paytm QR simulation.
  - 💳 **Credit / Debit Cards**: Visa, Mastercard, RuPay.
  - 💵 **Cash on Delivery (COD)**: Pay when the meal arrives at your doorstep.

### 🛰️ 4. Live Order Tracking HUD
- **5-Stage Animated Stepper**: Real-time order lifecycle visualization:
  $$\text{PLACED} \longrightarrow \text{CONFIRMED} \longrightarrow \text{PREPARING} \longrightarrow \text{OUT\_FOR\_DELIVERY} \longrightarrow \text{DELIVERED}$$
- **Delivery Valet Profile**: Live driver info, rating, and click-to-call action.
- **Interactive Step Simulator**: Test the entire order fulfillment journey with one click.

### 📜 5. Past Order History & 1-Click Reorder
- **Complete Receipt History**: Access previous orders with detailed dish breakdowns, timestamps, and payment summaries.
- **1-Click Reorder**: Instantly re-populate the active cart with dishes from past orders.

### 👨‍🍳 6. Kitchen & Admin Operations Hub
- **Operations Portal**: Revenue metrics, active orders, operational kitchens, and catalog totals.
- **Live Order Stream**: Track active kitchen orders and trigger delivery valet dispatching.
- **Dish Stock Management**: Toggle items between **In Stock** and **Out of Stock** in real time.

### 🛡️ 7. Enterprise Security & Role-Based Access Control (RBAC)
- **Stateless JWT Authentication**: Spring Security 6 with token expiration and BCrypt password hashing.
- **4 Distinct Roles**: `ROLE_USER`, `ROLE_ADMIN`, `ROLE_RESTAURANT_OWNER`, `ROLE_DELIVERY_PARTNER`.
- **1-Click Demo Accounts**: Switch between `Customer`, `Admin`, and `Restaurant Owner` with a single click.

---

## 📂 Comprehensive File Structure & Descriptions

```
gastrofullstack/
├── pom.xml                                   # Maven dependency build descriptor (Java 21, Spring Boot 3.5, JWT, Razorpay)
├── README.md                                 # Full-stack project documentation and architecture guide
│
├── src/main/java/com/fooddelivery/foodbackend/
│   ├── FoodbackendApplication.java           # Main Spring Boot Application entry point
│   │
│   ├── config/                               # Infrastructure and security beans
│   │   ├── AppConfig.java                    # Global CORS configuration and application beans
│   │   ├── DataInitializer.java              # Database seeder (auto-creates roles, test users, restaurants, menu items)
│   │   ├── ModelMapperConfig.java            # DTO <-> Entity object mapper bean
│   │   ├── PasswordConfig.java               # BCryptPasswordEncoder bean
│   │   ├── RazorpayConfig.java               # Razorpay payment client bean
│   │   ├── SecurityConfig.java               # Spring Security 6 FilterChain, public/protected routes, JWT filter
│   │   └── WebMvcConfig.java                 # Static resource handler configurations
│   │
│   ├── controller/                           # REST API Controllers (11 total)
│   │   ├── AddressController.java            # Saved delivery addresses (CRUD)
│   │   ├── AdminController.java              # Admin analytics, restaurant approval, platform order management
│   │   ├── AuthController.java               # User registration, JWT login, and profile retrieval (/me)
│   │   ├── CartController.java               # User shopping cart operations and cart clearing
│   │   ├── CartItemController.java           # Cart item addition, quantity updates, and deletion
│   │   ├── CategoryController.java           # Food categories management
│   │   ├── GroupCartController.java          # Group session creation, joining, shared item addition, bill split
│   │   ├── MenuItemController.java           # Menu dish catalog, restaurant-specific menus, dietary filters
│   │   ├── OrderController.java              # Order placement, order tracking by ID, customer order history
│   │   ├── PaymentController.java            # Razorpay order generation and webhook/signature verification
│   │   └── RestaurantController.java         # Restaurant discovery, details by ID, and cuisine search
│   │
│   ├── dto/                                  # Data Transfer Objects (DTOs) for request/response encapsulation
│   │   ├── request/                          # LoginRequest, RegisterRequest, OrderRequest, CartItemRequest, etc.
│   │   └── response/                         # AuthResponse, OrderResponse, CartResponse, GroupCartResponse, etc.
│   │
│   ├── entity/                               # Hibernate JPA Entities (13 total)
│   │   ├── Address.java                      # Delivery addresses with street, city, state, postal code, tag
│   │   ├── Cart.java                         # Active user cart with calculated totals
│   │   ├── CartItem.java                     # Individual dish line item in a cart
│   │   ├── Category.java                     # Food category (Burgers, Ramen, Sushi, Desserts, etc.)
│   │   ├── GroupCart.java                    # Collaborative group room session with session code
│   │   ├── GroupCartItem.java                # Item in a group cart attributed to a specific user
│   │   ├── MenuItem.java                     # Dish item (name, price, veg/non-veg, image, preparation time, status)
│   │   ├── Order.java                        # Placed order record with order status lifecycle and billing info
│   │   ├── OrderItem.java                    # Snapshot of dish and price at time of order
│   │   ├── Payment.java                      # Payment transaction record with method and gateway IDs
│   │   ├── Restaurant.java                   # Restaurant profile (name, address, rating, image, open/approved status)
│   │   ├── Role.java                         # User role entity (ROLE_USER, ROLE_ADMIN, etc.)
│   │   ├── User.java                         # User account credentials, contact info, and role mappings
│   │   └── enums/                            # AppRole, OrderStatus, PaymentMethod, PaymentStatus enums
│   │
│   ├── repository/                           # Spring Data JPA interfaces for database operations
│   │   ├── AddressRepository.java            # Address queries
│   │   ├── CartItemRepository.java           # Cart item queries
│   │   ├── CartRepository.java               # Cart queries
│   │   ├── CategoryRepository.java           # Category queries
│   │   ├── GroupCartItemRepository.java      # Group cart item queries
│   │   ├── GroupCartRepository.java          # Group cart queries by session code
│   │   ├── MenuItemRepository.java           # Menu item queries by restaurant/category
│   │   ├── OrderItemRepository.java          # Order item queries
│   │   ├── OrderRepository.java              # Order queries by user and order number
│   │   ├── PaymentRepository.java            # Payment transaction queries
│   │   ├── RestaurantRepository.java         # Restaurant search and filter queries
│   │   ├── RoleRepository.java               # Role lookups
│   │   └── UserRepository.java               # User lookup by email/phone
│   │
│   ├── security/                             # JWT Security Layer
│   │   ├── CustomUserDetails.java            # Spring Security user wrapper
│   │   ├── CustomUserDetailsService.java     # User loading service
│   │   ├── JwtAuthenticationFilter.java      # Request filter intercepting Bearer JWT tokens
│   │   ├── JwtService.java                   # JWT token generation, parsing, and claims validation
│   │   └── SecurityUtils.java                # Helper for getting currently authenticated user
│   │
│   ├── service/                              # Service Interfaces & Business Logic Implementations
│   │   ├── services/                         # Interfaces (AuthService, OrderService, CartService, etc.)
│   │   └── serviceimpl/                      # Concrete Spring @Service implementations
│   │
│   └── util/                                 # Utility classes (file uploads, formatters)
│       └── FileUploadUtil.java               # Image upload handler
│
├── src/main/resources/
│   ├── application.properties                # Spring Boot DB connection, JWT secret, server port, and fees
│   ├── static/                               # Static backend assets
│   └── templates/                            # Server templates
│
└── frontend/                                 # Modern React 19 + TypeScript + Vite Client
    ├── package.json                          # Frontend dependencies (React 19, Lucide, Zustand, Axios, Sonner, Tailwind)
    ├── vite.config.ts                        # Vite build configuration
    ├── tsconfig.json                         # TypeScript compiler configuration
    ├── index.html                            # HTML5 entry with Google Fonts (Outfit & Inter)
    │
    └── src/
        ├── main.tsx                          # React DOM initialization
        ├── App.tsx                           # Root component, React Router routes, global navigation, toasts
        ├── index.css                         # Tailwind CSS imports and base design variables
        │
        ├── api/                              # Networking & REST API Integration
        │   ├── axiosClient.ts                # Axios instance with automatic JWT Authorization header injection
        │   └── endpoints.ts                  # Typed API functions for Auth, Restaurants, Menus, Carts, Orders
        │
        ├── components/
        │   ├── common/
        │   │   ├── AuthModal.tsx             # Universal Sign-in/Sign-up modal with 1-click demo logins
        │   │   ├── GlassCard.tsx             # Glassmorphic card container with backdrop filter
        │   │   └── GlassElements.tsx         # Reusable glass inputs, buttons, and badges
        │   │
        │   ├── food/
        │   │   ├── CategoryPillList.tsx      # Category selector pill bar
        │   │   ├── FoodCard.tsx              # Menu dish card with image, price, veg badge, and quantity controls
        │   │   └── RestaurantCard.tsx        # Restaurant preview card with rating, address, and delivery time
        │   │
        │   ├── layout/
        │   │   ├── FloatingCartWidget.tsx    # Floating bottom-right quick-cart preview with expandable HUD
        │   │   ├── Footer.tsx                # Responsive footer with site links, security badges, app downloads
        │   │   └── GlassNavbar.tsx           # Translucent sticky header with search, active group pill, and auth
        │   │
        │   └── order/
        │       └── LiveOrderStepper.tsx      # 5-stage animated progress timeline HUD
        │
        ├── data/
        │   └── mockData.ts                   # Comprehensive fallback mock data for instant preview & offline resilience
        │
        ├── pages/                            # Full-Page Views
        │   ├── HomePage.tsx                  # Landing hero, demo credentials strip, category filter, restaurant grid
        │   ├── RestaurantDetailPage.tsx      # Restaurant banner, dietary filters, full menu items grid
        │   ├── CartPage.tsx                  # Item list, quantity adjustment, coupon engine, bill breakdown
        │   ├── GroupCartPage.tsx             # Live group session room, member orders, automated bill split
        │   ├── CheckoutPage.tsx              # Address selection, payment method selector (Razorpay, UPI, Card, COD)
        │   ├── OrderTrackingPage.tsx         # Live status stepper HUD, valet details, step simulator
        │   ├── OrderHistoryPage.tsx          # Past orders list, receipts, 1-click reordering
        │   └── AdminDashboardPage.tsx        # Operations KPI cards, live order stream, dish inventory toggles
        │
        ├── store/                            # State Management with Zustand
        │   ├── authStore.ts                  # User authentication state, token persistence, login/logout actions
        │   ├── cartStore.ts                  # Client shopping cart state (items, subtotal, delivery fee, taxes)
        │   └── groupCartStore.ts             # Collaborative group session state (session code, members, items)
        │
        ├── styles/
        │   └── glassmorphism.css             # Glassmorphism design tokens, glowing orbs, blur filters, keyframes
        │
        └── types/
            └── index.ts                      # TypeScript interfaces (Restaurant, MenuItem, Cart, Order, User, etc.)
```

---

## 🔑 Demo Login Accounts

Test all roles immediately with pre-seeded credentials:

| Role | Email | Password | Access & Permissions |
| :--- | :--- | :--- | :--- |
| **Customer** | `customer@gastro.com` | `password123` | Browse menus, add items to cart, join group sessions, checkout, track orders. |
| **Admin** | `admin@gastro.com` | `password123` | Operations dashboard, revenue analytics, kitchen order stream, inventory toggles. |
| **Restaurant Owner** | `owner@gastro.com` | `password123` | Manage restaurant profile, add dishes, toggle stock availability. |

---

## ⚡ Setup & Execution

### 1. Backend (Spring Boot)
1. Ensure MySQL is running on port `3306` with database `food_delivery_db`.
2. Configure credentials in `src/main/resources/application.properties` (or set environment variables):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/food_delivery_db?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
3. Run the backend server:
   ```bash
   ./mvnw spring-boot:run
   ```
   *API will start on `http://localhost:8080`.*

### 2. Frontend (React + Vite)
1. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```
2. Start the Vite dev server:
   ```bash
   npm run dev
   ```
3. Open `http://localhost:5173` in your browser.

---

## 🌐 REST API Endpoints Overview

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register new user account | Public |
| `POST` | `/api/auth/login` | Authenticate user & return JWT token | Public |
| `GET` | `/api/auth/me` | Fetch authenticated user profile | User / Admin |
| `GET` | `/api/restaurants` | List all restaurants (with optional search/cuisine filter) | Public |
| `GET` | `/api/restaurants/{id}` | Get restaurant details by ID | Public |
| `GET` | `/api/menu-items` | Get all featured menu dishes | Public |
| `GET` | `/api/menu-items/restaurant/{id}` | Get menu items for a specific restaurant | Public |
| `GET` | `/api/categories` | List all food categories | Public |
| `GET` | `/api/cart` | Get current user's active cart | User |
| `POST` | `/api/cart/items` | Add item to cart | User |
| `PUT` | `/api/cart/items/{id}` | Update item quantity in cart | User |
| `DELETE`| `/api/cart/items/{id}` | Remove item from cart | User |
| `POST` | `/api/group-carts` | Create new collaborative group cart session | User |
| `GET` | `/api/group-carts/code/{code}` | Get group cart details by session code | User |
| `POST` | `/api/group-carts/join` | Join group cart by session code | User |
| `POST` | `/api/orders` | Place a new order from active/group cart | User |
| `GET` | `/api/orders/{id}` | Get order details & tracking status | User / Admin |
| `GET` | `/api/orders/my-orders` | Get past order history for logged-in user | User |
| `GET` | `/api/admin/metrics` | Get platform KPI operational metrics | Admin |

---

## 📄 License
This project is licensed under the MIT License.
