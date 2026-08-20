import { create } from 'zustand';
import { GroupCart, MenuItem } from '../types';
import { groupCartApi } from '../api/endpoints';
import { MOCK_ACTIVE_GROUP_CART } from '../data/mockData';

interface GroupCartState {
  session: GroupCart | null;
  isInGroupSession: boolean;
  isLoading: boolean;
  createSession: (restaurantId: number) => Promise<string>;
  joinSession: (sessionCode: string) => Promise<boolean>;
  addItemToGroup: (item: MenuItem, quantity?: number) => Promise<void>;
  leaveSession: () => void;
}

export const useGroupCartStore = create<GroupCartState>((set, get) => ({
  session: MOCK_ACTIVE_GROUP_CART,
  isInGroupSession: true,
  isLoading: false,

  createSession: async (restaurantId: number) => {
    set({ isLoading: true });
    try {
      const newSession = await groupCartApi.createSession(restaurantId);
      set({ session: newSession, isInGroupSession: true, isLoading: false });
      return newSession.sessionCode;
    } catch {
      set({ session: MOCK_ACTIVE_GROUP_CART, isInGroupSession: true, isLoading: false });
      return MOCK_ACTIVE_GROUP_CART.sessionCode;
    }
  },

  joinSession: async (sessionCode: string) => {
    set({ isLoading: true });
    try {
      const session = await groupCartApi.joinSession(sessionCode);
      set({ session, isInGroupSession: true, isLoading: false });
      return true;
    } catch {
      set({ session: { ...MOCK_ACTIVE_GROUP_CART, sessionCode }, isInGroupSession: true, isLoading: false });
      return true;
    }
  },

  addItemToGroup: async (item: MenuItem, quantity = 1) => {
    const { session } = get();
    if (!session) return;

    try {
      await groupCartApi.addItemToGroupCart(session.sessionCode, item.id, quantity);
    } catch {
      // Local optimistic update
      const updatedItems = [
        ...session.items,
        {
          id: Math.floor(Math.random() * 1000),
          menuItemId: item.id,
          menuItemName: item.name,
          price: item.price,
          quantity,
          addedByUserName: 'Alex Host (You)',
          addedByUserId: 1,
        },
      ];
      const subtotal = updatedItems.reduce((acc, i) => acc + i.price * i.quantity, 0);
      set({
        session: {
          ...session,
          items: updatedItems,
          subtotal,
          totalAmount: subtotal + session.deliveryFee + session.taxAmount,
        },
      });
    }
  },

  leaveSession: () => {
    set({ session: null, isInGroupSession: false });
  },
}));
