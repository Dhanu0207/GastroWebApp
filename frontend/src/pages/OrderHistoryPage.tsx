import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Clock, ArrowRight, RotateCcw, CheckCircle2, ChevronLeft } from 'lucide-react';
import { Order } from '../types';
import { orderApi } from '../api/endpoints';
import { useCartStore } from '../store/cartStore';
import { toast } from 'sonner';

export const OrderHistoryPage: React.FC = () => {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<Order[]>([]);
  const { addItem } = useCartStore();

  useEffect(() => {
    const fetchOrders = async () => {
      const data = await orderApi.getUserOrders();
      setOrders(data);
    };
    fetchOrders();
  }, []);

  const handleReorder = (order: Order) => {
    order.items.forEach((item) => {
      addItem({
        id: item.menuItemId,
        restaurantId: order.restaurantId,
        restaurantName: order.restaurantName,
        name: item.menuItemName,
        description: 'Reordered item',
        price: item.price,
        imageUrl: '',
        isVegetarian: false,
        isAvailable: true,
      }, item.quantity);
    });

    toast.success('Dishes added to cart!');
    navigate('/cart');
  };

  return (
    <div className="space-y-8 pb-20 max-w-4xl mx-auto">
      
      <button
        onClick={() => navigate('/')}
        className="flex items-center gap-1.5 text-xs font-semibold text-slate-400 hover:text-white transition"
      >
        <ChevronLeft className="h-4 w-4" /> Back to Discovery
      </button>

      <div>
        <h1 className="text-3xl font-extrabold text-white tracking-tight flex items-center gap-2">
          <Clock className="h-7 w-7 text-amber-400" />
          Past Orders & History
        </h1>
        <p className="text-xs text-slate-400 mt-1">
          Review your previous culinary orders or re-order your favorites with one click
        </p>
      </div>

      <div className="space-y-4">
        {orders.map((order) => (
          <div
            key={order.id}
            className="overflow-hidden rounded-3xl border border-white/10 bg-slate-950/40 p-6 backdrop-blur-xl transition hover:border-white/20"
          >
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 border-b border-white/10 pb-4">
              <div>
                <div className="flex items-center gap-2">
                  <span className="font-bold text-white text-base">{order.restaurantName}</span>
                  <span className="text-xs rounded-full bg-emerald-500/20 text-emerald-300 px-2.5 py-0.5 border border-emerald-500/30 flex items-center gap-1">
                    <CheckCircle2 className="h-3 w-3" /> {order.orderStatus}
                  </span>
                </div>
                <p className="text-xs text-slate-400 mt-1">
                  Order #{order.orderNumber} • {new Date(order.createdAt).toLocaleDateString()}
                </p>
              </div>

              <div className="flex items-center gap-3">
                <button
                  onClick={() => navigate(`/order-tracking/${order.orderNumber}`)}
                  className="rounded-xl border border-white/10 bg-white/5 px-3 py-1.5 text-xs font-semibold text-slate-200 hover:bg-white/10 transition"
                >
                  Track Live HUD
                </button>
                <button
                  onClick={() => handleReorder(order)}
                  className="flex items-center gap-1 rounded-xl bg-orange-500 px-3.5 py-1.5 text-xs font-semibold text-white shadow-md shadow-orange-500/30 hover:bg-orange-600 transition"
                >
                  <RotateCcw className="h-3.5 w-3.5" /> Reorder
                </button>
              </div>
            </div>

            {/* Items list */}
            <div className="py-3 space-y-1.5 text-xs">
              {order.items.map((i, idx) => (
                <div key={idx} className="flex justify-between text-slate-300">
                  <span>{i.menuItemName} x{i.quantity}</span>
                  <span className="font-mono text-white">₹{i.price * i.quantity}</span>
                </div>
              ))}
            </div>

            <div className="border-t border-white/10 pt-3 flex justify-between items-baseline">
              <span className="text-xs text-slate-400">Total Billed</span>
              <span className="text-base font-bold text-orange-400 font-mono">₹{order.totalAmount}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
