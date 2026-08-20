// Types mirroring the Spring Boot backend DTOs & Entities

export type Role = 'ROLE_USER' | 'ROLE_RESTAURANT_OWNER' | 'ROLE_ADMIN';

export interface User {
  id: number;
  email: string;
  fullName: string;
  role: Role;
  phoneNumber?: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Restaurant {
  id: number;
  name: string;
  description: string;
  address: string;
  phoneNumber: string;
  cuisineType: string;
  imageUrl: string;
  rating?: number;
  active: boolean;
  deliveryTime?: string;
  priceForTwo?: number;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  icon?: string;
}

export interface MenuItem {
  id: number;
  restaurantId: number;
  restaurantName?: string;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  categoryName?: string;
  categoryId?: number;
  isVegetarian: boolean;
  isAvailable: boolean;
  preparationTimeMinutes?: number;
  rating?: number;
}

export interface CartItem {
  id?: number;
  menuItemId: number;
  menuItemName: string;
  price: number;
  quantity: number;
  imageUrl?: string;
  addedByUserName?: string;
  addedByUserId?: number;
}

export interface Cart {
  id?: number;
  userId?: number;
  items: CartItem[];
  subtotal: number;
  deliveryFee: number;
  taxAmount: number;
  totalAmount: number;
}

export interface GroupCartMember {
  userId: number;
  userName: string;
  avatarUrl?: string;
  isHost?: boolean;
}

export interface GroupCart {
  id: number;
  sessionCode: string;
  hostUserId: number;
  hostUserName: string;
  restaurantId: number;
  restaurantName: string;
  status: 'ACTIVE' | 'LOCKED' | 'ORDERED' | 'EXPIRED';
  members: GroupCartMember[];
  items: CartItem[];
  subtotal: number;
  deliveryFee: number;
  taxAmount: number;
  totalAmount: number;
}

export type OrderStatus = 
  | 'PENDING'
  | 'PLACED'
  | 'CONFIRMED'
  | 'PREPARING'
  | 'OUT_FOR_DELIVERY'
  | 'DELIVERED'
  | 'CANCELLED';

export interface OrderItem {
  id: number;
  menuItemId: number;
  menuItemName: string;
  quantity: number;
  price: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  restaurantId: number;
  restaurantName: string;
  orderStatus: OrderStatus;
  subtotal: number;
  deliveryFee: number;
  taxAmount: number;
  totalAmount: number;
  deliveryAddress: Address;
  items: OrderItem[];
  paymentStatus: 'PENDING' | 'COMPLETED' | 'FAILED';
  paymentMethod: 'ONLINE_RAZORPAY' | 'CARD' | 'UPI' | 'COD';
  createdAt: string;
  estimatedDeliveryTime?: string;
}

export interface Address {
  id?: number;
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  isDefault?: boolean;
  tag?: 'Home' | 'Work' | 'Other';
}

export interface PaymentResponse {
  orderId: string;
  razorpayOrderId?: string;
  amount: number;
  currency: string;
  status: string;
}
