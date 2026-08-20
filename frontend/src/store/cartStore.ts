import { create } from 'zustand';
import { CartItem, MenuItem } from '../types';

interface CartState {
  items: CartItem[];
  subtotal: number;
  deliveryFee: number;
  taxAmount: number;
  totalAmount: number;
  restaurantId: number | null;
  restaurantName: string | null;
  addItem: (item: MenuItem, quantity?: number) => void;
  removeItem: (menuItemId: number) => void;
  updateQuantity: (menuItemId: number, delta: number) => void;
  clearCart: () => void;
}

const DELIVERY_FEE = 40;
const TAX_AMOUNT = 25;

const calculateTotals = (items: CartItem[]) => {
  const subtotal = items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  const totalAmount = items.length > 0 ? subtotal + DELIVERY_FEE + TAX_AMOUNT : 0;
  return { subtotal, totalAmount };
};

export const useCartStore = create<CartState>((set, get) => ({
  items: [
    {
      menuItemId: 101,
      menuItemName: 'Truffle Wagyu Melt Burger',
      price: 480,
      quantity: 1,
      imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80',
    },
    {
      menuItemId: 108,
      menuItemName: 'Crispy Truffle Parmesan Fries',
      price: 220,
      quantity: 1,
      imageUrl: 'https://images.unsplash.com/photo-1576107232684-1279f3908594?auto=format&fit=crop&w=800&q=80',
    },
  ],
  subtotal: 700,
  deliveryFee: DELIVERY_FEE,
  taxAmount: TAX_AMOUNT,
  totalAmount: 765,
  restaurantId: 1,
  restaurantName: 'Gastro Smokehouse & Grill',

  addItem: (item: MenuItem, quantity = 1) => {
    const { items, restaurantId } = get();

    // Check if adding from a different restaurant
    if (restaurantId && restaurantId !== item.restaurantId && items.length > 0) {
      if (!confirm(`Your cart contains items from another restaurant. Would you like to reset your cart for ${item.restaurantName || 'this restaurant'}?`)) {
        return;
      }
      // Reset cart for new restaurant
      const newItems: CartItem[] = [{
        menuItemId: item.id,
        menuItemName: item.name,
        price: item.price,
        quantity,
        imageUrl: item.imageUrl,
      }];
      const { subtotal, totalAmount } = calculateTotals(newItems);
      set({
        items: newItems,
        subtotal,
        totalAmount,
        restaurantId: item.restaurantId,
        restaurantName: item.restaurantName || 'Selected Restaurant',
      });
      return;
    }

    const existingIndex = items.findIndex((i) => i.menuItemId === item.id);
    let updatedItems: CartItem[];

    if (existingIndex > -1) {
      updatedItems = [...items];
      updatedItems[existingIndex].quantity += quantity;
    } else {
      updatedItems = [
        ...items,
        {
          menuItemId: item.id,
          menuItemName: item.name,
          price: item.price,
          quantity,
          imageUrl: item.imageUrl,
        },
      ];
    }

    const { subtotal, totalAmount } = calculateTotals(updatedItems);
    set({
      items: updatedItems,
      subtotal,
      totalAmount,
      restaurantId: item.restaurantId,
      restaurantName: item.restaurantName || get().restaurantName,
    });
  },

  updateQuantity: (menuItemId: number, delta: number) => {
    const { items } = get();
    const updatedItems = items
      .map((item) => {
        if (item.menuItemId === menuItemId) {
          const newQty = item.quantity + delta;
          return newQty > 0 ? { ...item, quantity: newQty } : null;
        }
        return item;
      })
      .filter(Boolean) as CartItem[];

    const { subtotal, totalAmount } = calculateTotals(updatedItems);
    set({
      items: updatedItems,
      subtotal,
      totalAmount,
      restaurantId: updatedItems.length === 0 ? null : get().restaurantId,
      restaurantName: updatedItems.length === 0 ? null : get().restaurantName,
    });
  },

  removeItem: (menuItemId: number) => {
    const { items } = get();
    const updatedItems = items.filter((i) => i.menuItemId !== menuItemId);
    const { subtotal, totalAmount } = calculateTotals(updatedItems);
    set({
      items: updatedItems,
      subtotal,
      totalAmount,
      restaurantId: updatedItems.length === 0 ? null : get().restaurantId,
      restaurantName: updatedItems.length === 0 ? null : get().restaurantName,
    });
  },

  clearCart: () => {
    set({
      items: [],
      subtotal: 0,
      totalAmount: 0,
      restaurantId: null,
      restaurantName: null,
    });
  },
}));
