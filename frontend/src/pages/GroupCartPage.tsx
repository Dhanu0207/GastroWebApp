import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Users, 
  Copy, 
  Check, 
  Plus, 
  ShoppingBag, 
  Calculator, 
  ArrowRight, 
  ShieldCheck,
  UserPlus
} from 'lucide-react';
import { useGroupCartStore } from '../store/groupCartStore';
import { useCartStore } from '../store/cartStore';
import { toast } from 'sonner';

export const GroupCartPage: React.FC = () => {
  const navigate = useNavigate();
  const { session, isInGroupSession, createSession, joinSession, leaveSession } = useGroupCartStore();
  const { addItem } = useCartStore();

  const [copied, setCopied] = useState(false);
  const [joinInput, setJoinInput] = useState('');
  const [newMemberName, setNewMemberName] = useState('');

  const handleCopyCode = () => {
    if (!session) return;
    navigator.clipboard.writeText(session.sessionCode);
    setCopied(true);
    toast.success('Session code copied to clipboard! Share it with friends.');
    setTimeout(() => setCopied(false), 2000);
  };

  const handleJoin = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!joinInput.trim()) return;
    const ok = await joinSession(joinInput.trim().toUpperCase());
    if (ok) {
      toast.success(`Joined room ${joinInput.toUpperCase()}`);
    }
  };

  const handleCreateNew = async () => {
    const code = await createSession(1);
    toast.success(`New group session started: ${code}`);
  };

  // Group items by member
  const memberItemMap = (session?.members || []).map((member) => {
    const items = (session?.items || []).filter(
      (i) => i.addedByUserId === member.userId || i.addedByUserName === member.userName
    );
    const memberSubtotal = items.reduce((acc, i) => acc + i.price * i.quantity, 0);
    // Proportionate share of tax + delivery
    const totalGroupSubtotal = session?.subtotal || 1;
    const shareOfFees = totalGroupSubtotal > 0 
      ? Math.round(((session?.deliveryFee || 40) + (session?.taxAmount || 25)) * (memberSubtotal / totalGroupSubtotal))
      : 0;

    return {
      ...member,
      items,
      memberSubtotal,
      totalShare: memberSubtotal + shareOfFees,
    };
  });

  const handleCheckoutGroup = () => {
    if (!session || session.items.length === 0) {
      toast.error('Add some items before checking out!');
      return;
    }
    // Sync items to cart store
    session.items.forEach((item) => {
      addItem({
        id: item.menuItemId,
        restaurantId: session.restaurantId,
        name: item.menuItemName,
        description: 'Group item',
        price: item.price,
        imageUrl: item.imageUrl || '',
        isVegetarian: false,
        isAvailable: true,
      }, item.quantity);
    });

    toast.success('Group cart synchronized. Proceeding to checkout!');
    navigate('/checkout');
  };

  return (
    <div className="space-y-10 pb-20 max-w-6xl mx-auto">
      
      {/* Header Banner */}
      <div className="relative overflow-hidden rounded-3xl border border-emerald-500/30 bg-emerald-950/20 p-6 sm:p-8 backdrop-blur-2xl shadow-2xl">
        <div className="pointer-events-none absolute -top-20 -right-20 h-64 w-64 rounded-full bg-emerald-500/20 blur-3xl" />

        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="space-y-2">
            <div className="inline-flex items-center gap-2 rounded-full border border-emerald-500/40 bg-emerald-500/10 px-3 py-1 text-xs font-semibold text-emerald-300">
              <Users className="h-3.5 w-3.5" /> Collaborative Live Room
            </div>
            <h1 className="text-3xl sm:text-4xl font-extrabold text-white tracking-tight">
              Group Cart Session
            </h1>
            <p className="text-xs text-slate-300">
              Restaurant: <span className="text-white font-semibold">{session?.restaurantName || 'Gastro Smokehouse & Grill'}</span>
            </p>
          </div>

          {/* Session Code Pill & Actions */}
          {session ? (
            <div className="flex flex-wrap items-center gap-3">
              <div className="flex items-center gap-2 rounded-2xl border border-white/15 bg-white/10 px-4 py-2.5 backdrop-blur-lg">
                <span className="text-xs text-slate-400">Share Code:</span>
                <span className="font-mono text-base font-bold text-emerald-300 tracking-wider">
                  {session.sessionCode}
                </span>
                <button
                  onClick={handleCopyCode}
                  className="p-1.5 rounded-lg text-slate-300 hover:text-white hover:bg-white/10 transition"
                  title="Copy session code"
                >
                  {copied ? <Check className="h-4 w-4 text-emerald-400" /> : <Copy className="h-4 w-4" />}
                </button>
              </div>

              <button
                onClick={leaveSession}
                className="rounded-2xl border border-red-500/30 bg-red-500/10 px-4 py-2.5 text-xs font-semibold text-red-300 hover:bg-red-500/20 transition"
              >
                Leave Room
              </button>
            </div>
          ) : (
            <div className="flex items-center gap-3">
              <button
                onClick={handleCreateNew}
                className="rounded-2xl bg-emerald-500 px-5 py-3 text-xs font-bold text-slate-950 hover:bg-emerald-400 transition shadow-lg shadow-emerald-500/30"
              >
                Create New Session
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Main Grid: Active Members & Split Bill */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Members & Items (2 Cols) */}
        <div className="lg:col-span-2 space-y-6">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-bold text-white flex items-center gap-2">
              <Users className="h-5 w-5 text-emerald-400" />
              Room Members ({session?.members.length || 0})
            </h2>
            <button
              onClick={() => navigate('/')}
              className="flex items-center gap-1.5 text-xs font-semibold text-orange-400 hover:text-orange-300 transition"
            >
              <Plus className="h-4 w-4" /> Add More Dishes from Menu
            </button>
          </div>

          <div className="space-y-4">
            {memberItemMap.map((member) => (
              <div
                key={member.userId}
                className="overflow-hidden rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl transition hover:border-white/20"
              >
                {/* Member Header */}
                <div className="flex items-center justify-between border-b border-white/10 pb-3">
                  <div className="flex items-center gap-3">
                    <div className="h-9 w-9 rounded-xl bg-gradient-to-tr from-emerald-500 to-teal-400 text-slate-950 flex items-center justify-center font-bold text-sm shadow-md">
                      {member.userName.charAt(0)}
                    </div>
                    <div>
                      <h4 className="text-sm font-bold text-white flex items-center gap-2">
                        {member.userName}
                        {member.isHost && (
                          <span className="text-[10px] uppercase font-bold bg-amber-500/20 text-amber-300 px-2 py-0.5 rounded-full border border-amber-500/30">
                            Host
                          </span>
                        )}
                      </h4>
                      <p className="text-[11px] text-slate-400">
                        {member.items.length} item{member.items.length !== 1 ? 's' : ''} added
                      </p>
                    </div>
                  </div>

                  <div className="text-right">
                    <span className="text-[10px] text-slate-400 uppercase font-semibold">Their Total</span>
                    <p className="text-base font-bold text-white font-mono">₹{member.totalShare}</p>
                  </div>
                </div>

                {/* Member's Item List */}
                <div className="mt-3 space-y-2">
                  {member.items.length > 0 ? (
                    member.items.map((item, idx) => (
                      <div key={idx} className="flex items-center justify-between text-xs py-1">
                        <span className="text-slate-300">{item.menuItemName}</span>
                        <div className="flex items-center gap-3">
                          <span className="text-slate-400 font-mono">x{item.quantity}</span>
                          <span className="text-white font-mono font-semibold">₹{item.price * item.quantity}</span>
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="text-xs text-slate-500 italic py-2">No items added yet by {member.userName}...</p>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Live Split-Bill Summary (1 Col) */}
        <div className="space-y-6">
          <div className="relative overflow-hidden rounded-3xl border border-white/15 bg-slate-950/60 p-6 backdrop-blur-2xl shadow-2xl">
            <div className="pointer-events-none absolute -top-12 -right-12 h-32 w-32 rounded-full bg-emerald-500/20 blur-3xl" />

            <div className="flex items-center gap-2 border-b border-white/10 pb-3">
              <Calculator className="h-5 w-5 text-emerald-400" />
              <h3 className="text-base font-bold text-white">Live Bill Split</h3>
            </div>

            {/* Split Breakdown */}
            <div className="py-4 space-y-3">
              {memberItemMap.map((member) => (
                <div key={member.userId} className="flex items-center justify-between text-xs">
                  <span className="text-slate-300">{member.userName}</span>
                  <span className="font-mono font-bold text-emerald-300">₹{member.totalShare}</span>
                </div>
              ))}

              <div className="border-t border-white/10 pt-3 space-y-1.5 text-xs text-slate-400">
                <div className="flex justify-between">
                  <span>Combined Subtotal</span>
                  <span className="font-mono text-white font-semibold">₹{session?.subtotal || 0}</span>
                </div>
                <div className="flex justify-between">
                  <span>Delivery & Taxes</span>
                  <span className="font-mono text-white font-semibold">₹{(session?.deliveryFee || 40) + (session?.taxAmount || 25)}</span>
                </div>
              </div>

              <div className="border-t border-white/10 pt-3 flex justify-between items-baseline">
                <span className="font-bold text-white text-sm">Combined Total</span>
                <span className="text-2xl font-black text-emerald-400 font-mono">₹{session?.totalAmount || 0}</span>
              </div>
            </div>

            {/* Checkout Button */}
            <button
              onClick={handleCheckoutGroup}
              className="w-full flex items-center justify-center gap-2 rounded-2xl bg-gradient-to-r from-emerald-500 to-teal-500 py-3.5 text-sm font-bold text-slate-950 shadow-xl shadow-emerald-500/30 hover:scale-105 active:scale-95 transition-all cursor-pointer"
            >
              Checkout Group Order <ArrowRight className="h-4 w-4" />
            </button>

            <div className="mt-4 flex items-center justify-center gap-2 text-[11px] text-slate-400">
              <ShieldCheck className="h-3.5 w-3.5 text-emerald-400" />
              <span>Real-time sync enabled</span>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};
