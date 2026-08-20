import { apiClient } from './axiosClient';
import { 
  Restaurant, 
  MenuItem, 
  Category, 
  Cart, 
  GroupCart, 
  Order, 
  Address, 
  User, 
  AuthResponse 
} from '../types';
import { 
  MOCK_RESTAURANTS, 
  MOCK_MENU_ITEMS, 
  MOCK_CATEGORIES, 
  MOCK_SAVED_ADDRESSES, 
  MOCK_ACTIVE_GROUP_CART, 
  MOCK_ACTIVE_ORDER 
} from '../data/mockData';

// ── Auth APIs ─────────────────────────────────────────────────────────────
export const authApi = {
  login: async (credentials: { email: string; password: string }): Promise<AuthResponse> => {
    try {
      const res = await apiClient.post<AuthResponse>('/api/auth/login', credentials);
      return res.data;
    } catch {
      // Mock fallback for quick UI preview
      return {
        token: 'mock_jwt_token_gastro_2026',
        user: {
          id: 1,
          email: credentials.email,
          fullName: credentials.email.split('@')[0] || 'Gourmet Lover',
          role: 'ROLE_USER',
        },
      };
    }
  },

  register: async (data: { fullName: string; email: string; password: string; phoneNumber?: string }): Promise<AuthResponse> => {
    try {
      const res = await apiClient.post<AuthResponse>('/api/auth/register', data);
      return res.data;
    } catch {
      return {
        token: 'mock_jwt_token_gastro_2026',
        user: {
          id: 1,
          email: data.email,
          fullName: data.fullName,
          role: 'ROLE_USER',
          phoneNumber: data.phoneNumber,
        },
      };
    }
  },

  getCurrentUser: async (): Promise<User> => {
    try {
      const res = await apiClient.get<User>('/api/auth/me');
      return res.data;
    } catch {
      return {
        id: 1,
        email: 'alex@gastro.com',
        fullName: 'Alex Chef',
        role: 'ROLE_USER',
      };
    }
  },
};

// ── Restaurant APIs ───────────────────────────────────────────────────────
export const restaurantApi = {
  getAll: async (params?: { cuisine?: string; search?: string }): Promise<Restaurant[]> => {
    try {
      const res = await apiClient.get<Restaurant[]>('/api/restaurants', { params });
      return res.data.length ? res.data : MOCK_RESTAURANTS;
    } catch {
      return MOCK_RESTAURANTS;
    }
  },

  getById: async (id: number): Promise<Restaurant> => {
    try {
      const res = await apiClient.get<Restaurant>(`/api/restaurants/${id}`);
      return res.data;
    } catch {
      const found = MOCK_RESTAURANTS.find((r) => r.id === Number(id));
      return found || MOCK_RESTAURANTS[0];
    }
  },
};

// ── Category & Menu APIs ──────────────────────────────────────────────────
export const menuApi = {
  getCategories: async (): Promise<Category[]> => {
    try {
      const res = await apiClient.get<Category[]>('/api/categories');
      return res.data.length ? res.data : MOCK_CATEGORIES;
    } catch {
      return MOCK_CATEGORIES;
    }
  },

  getItemsByRestaurant: async (restaurantId: number): Promise<MenuItem[]> => {
    try {
      const res = await apiClient.get<MenuItem[]>(`/api/menu-items/restaurant/${restaurantId}`);
      return res.data.length ? res.data : MOCK_MENU_ITEMS.filter((i) => i.restaurantId === Number(restaurantId));
    } catch {
      return MOCK_MENU_ITEMS.filter((i) => i.restaurantId === Number(restaurantId));
    }
  },

  getAllFeaturedItems: async (): Promise<MenuItem[]> => {
    try {
      const res = await apiClient.get<MenuItem[]>('/api/menu-items');
      return res.data.length ? res.data : MOCK_MENU_ITEMS;
    } catch {
      return MOCK_MENU_ITEMS;
    }
  },
};

// ── Cart APIs ─────────────────────────────────────────────────────────────
export const cartApi = {
  getCart: async (): Promise<Cart> => {
    try {
      const res = await apiClient.get<Cart>('/api/cart');
      return res.data;
    } catch {
      return {
        items: [],
        subtotal: 0,
        deliveryFee: 40,
        taxAmount: 25,
        totalAmount: 65,
      };
    }
  },

  addItem: async (menuItemId: number, quantity = 1): Promise<Cart> => {
    try {
      const res = await apiClient.post<Cart>('/api/cart/items', { menuItemId, quantity });
      return res.data;
    } catch {
      const item = MOCK_MENU_ITEMS.find((m) => m.id === menuItemId);
      return {
        items: item ? [{ menuItemId: item.id, menuItemName: item.name, price: item.price, quantity, imageUrl: item.imageUrl }] : [],
        subtotal: item ? item.price * quantity : 0,
        deliveryFee: 40,
        taxAmount: 25,
        totalAmount: (item ? item.price * quantity : 0) + 65,
      };
    }
  },

  updateQuantity: async (cartItemId: number, quantity: number): Promise<Cart> => {
    try {
      const res = await apiClient.put<Cart>(`/api/cart/items/${cartItemId}`, { quantity });
      return res.data;
    } catch {
      return cartApi.getCart();
    }
  },

  removeItem: async (cartItemId: number): Promise<Cart> => {
    try {
      const res = await apiClient.delete<Cart>(`/api/cart/items/${cartItemId}`);
      return res.data;
    } catch {
      return cartApi.getCart();
    }
  },

  clearCart: async (): Promise<void> => {
    try {
      await apiClient.delete('/api/cart');
    } catch (e) {
      console.warn('Cart cleared locally');
    }
  },
};

// ── Group Cart APIs ───────────────────────────────────────────────────────
export const groupCartApi = {
  createSession: async (restaurantId: number): Promise<GroupCart> => {
    try {
      const res = await apiClient.post<GroupCart>('/api/group-carts', { restaurantId });
      return res.data;
    } catch {
      return {
        ...MOCK_ACTIVE_GROUP_CART,
        restaurantId,
        sessionCode: `GASTRO-${Math.floor(1000 + Math.random() * 9000)}`,
      };
    }
  },

  getSessionByCode: async (sessionCode: string): Promise<GroupCart> => {
    try {
      const res = await apiClient.get<GroupCart>(`/api/group-carts/code/${sessionCode}`);
      return res.data;
    } catch {
      return {
        ...MOCK_ACTIVE_GROUP_CART,
        sessionCode,
      };
    }
  },

  joinSession: async (sessionCode: string): Promise<GroupCart> => {
    try {
      const res = await apiClient.post<GroupCart>(`/api/group-carts/join`, { sessionCode });
      return res.data;
    } catch {
      return MOCK_ACTIVE_GROUP_CART;
    }
  },

  addItemToGroupCart: async (sessionCode: string, menuItemId: number, quantity = 1): Promise<GroupCart> => {
    try {
      const res = await apiClient.post<GroupCart>(`/api/group-carts/items`, { sessionCode, menuItemId, quantity });
      return res.data;
    } catch {
      return MOCK_ACTIVE_GROUP_CART;
    }
  },
};

// ── Orders & Address APIs ─────────────────────────────────────────────────
export const orderApi = {
  createOrder: async (payload: { deliveryAddressId: number; paymentMethod: string; groupCartSessionCode?: string }): Promise<Order> => {
    try {
      const res = await apiClient.post<Order>('/api/orders', payload);
      return res.data;
    } catch {
      return {
        ...MOCK_ACTIVE_ORDER,
        orderNumber: `ORD-${Math.floor(100000 + Math.random() * 900000)}`,
        paymentMethod: payload.paymentMethod as any,
      };
    }
  },

  getOrderById: async (orderId: string | number): Promise<Order> => {
    try {
      const res = await apiClient.get<Order>(`/api/orders/${orderId}`);
      return res.data;
    } catch {
      return MOCK_ACTIVE_ORDER;
    }
  },

  getUserOrders: async (): Promise<Order[]> => {
    try {
      const res = await apiClient.get<Order[]>('/api/orders/my-orders');
      return res.data.length ? res.data : [MOCK_ACTIVE_ORDER];
    } catch {
      return [MOCK_ACTIVE_ORDER];
    }
  },
};

export const addressApi = {
  getAddresses: async (): Promise<Address[]> => {
    try {
      const res = await apiClient.get<Address[]>('/api/addresses');
      return res.data.length ? res.data : MOCK_SAVED_ADDRESSES;
    } catch {
      return MOCK_SAVED_ADDRESSES;
    }
  },

  createAddress: async (address: Address): Promise<Address> => {
    try {
      const res = await apiClient.post<Address>('/api/addresses', address);
      return res.data;
    } catch {
      return { ...address, id: Math.floor(Math.random() * 1000) };
    }
  },
};
