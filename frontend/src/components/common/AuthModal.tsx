import React, { useState } from 'react';
import { X, Mail, Lock, User, Phone, Sparkles, Shield, Store, UserCheck } from 'lucide-react';
import { useAuthStore } from '../../store/authStore';
import { GlassButton, GlassInput } from './GlassElements';
import { toast } from 'sonner';

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('customer@gastro.com');
  const [password, setPassword] = useState('password123');
  const [fullName, setFullName] = useState('Alex Chef');
  const [phoneNumber, setPhoneNumber] = useState('+91 98765 43210');

  const { login, register, isLoading } = useAuthStore();

  if (!isOpen) return null;

  const handleDemoFill = (demoEmail: string, demoRole: string) => {
    setEmail(demoEmail);
    setPassword('password123');
    setIsLogin(true);
    toast.info(`Loaded ${demoRole} Demo Credentials!`);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (isLogin) {
      const success = await login(email, password);
      if (success) {
        toast.success(`Welcome back, ${email.split('@')[0]}!`);
        onClose();
      } else {
        toast.error('Invalid credentials. Please try again.');
      }
    } else {
      const success = await register(fullName, email, password, phoneNumber);
      if (success) {
        toast.success('Account created successfully!');
        onClose();
      } else {
        toast.error('Registration failed. Try again.');
      }
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      {/* Dark backdrop */}
      <div 
        className="fixed inset-0 bg-black/75 backdrop-blur-md transition-opacity" 
        onClick={onClose} 
      />

      {/* Modal Container */}
      <div className="relative w-full max-w-md overflow-hidden rounded-3xl border border-white/20 bg-slate-950/90 p-6 sm:p-8 shadow-2xl backdrop-blur-2xl">
        {/* Glowing orb background */}
        <div className="pointer-events-none absolute -top-24 -left-24 h-48 w-48 rounded-full bg-orange-500/30 blur-3xl" />
        <div className="pointer-events-none absolute -bottom-24 -right-24 h-48 w-48 rounded-full bg-purple-500/20 blur-3xl" />

        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 rounded-full p-2 text-slate-400 hover:bg-white/10 hover:text-white transition"
        >
          <X className="h-5 w-5" />
        </button>

        {/* Header */}
        <div className="text-center mb-5">
          <div className="inline-flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-tr from-orange-500 to-amber-400 text-white shadow-lg shadow-orange-500/30 mb-2">
            <Sparkles className="h-6 w-6" />
          </div>
          <h2 className="text-2xl font-bold text-white tracking-tight">
            {isLogin ? 'Welcome to Gastro' : 'Create an Account'}
          </h2>
          <p className="text-xs text-slate-400 mt-1">
            {isLogin ? 'Sign in to access your cart and group orders' : 'Join to order artisanal food and share carts'}
          </p>
        </div>

        {/* ── 1-Click Demo Accounts Selector ─────────────────────────────── */}
        {isLogin && (
          <div className="mb-5 rounded-2xl border border-white/10 bg-white/[0.04] p-3 backdrop-blur-md">
            <span className="block text-[10px] uppercase font-bold tracking-wider text-orange-400 mb-2">
              ⚡ 1-Click Demo Logins:
            </span>
            <div className="grid grid-cols-3 gap-1.5">
              <button
                type="button"
                onClick={() => handleDemoFill('customer@gastro.com', 'Customer')}
                className="flex flex-col items-center justify-center py-1.5 px-2 rounded-xl bg-white/5 hover:bg-orange-500/20 border border-white/10 hover:border-orange-500/40 text-[11px] font-semibold text-slate-200 hover:text-orange-300 transition"
              >
                <UserCheck className="h-3.5 w-3.5 mb-0.5 text-orange-400" />
                Customer
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('admin@gastro.com', 'Admin')}
                className="flex flex-col items-center justify-center py-1.5 px-2 rounded-xl bg-white/5 hover:bg-purple-500/20 border border-white/10 hover:border-purple-500/40 text-[11px] font-semibold text-slate-200 hover:text-purple-300 transition"
              >
                <Shield className="h-3.5 w-3.5 mb-0.5 text-purple-400" />
                Admin
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('owner@gastro.com', 'Restaurant Owner')}
                className="flex flex-col items-center justify-center py-1.5 px-2 rounded-xl bg-white/5 hover:bg-emerald-500/20 border border-white/10 hover:border-emerald-500/40 text-[11px] font-semibold text-slate-200 hover:text-emerald-300 transition"
              >
                <Store className="h-3.5 w-3.5 mb-0.5 text-emerald-400" />
                Owner
              </button>
            </div>
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-3.5">
          {!isLogin && (
            <>
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">Full Name</label>
                <GlassInput
                  type="text"
                  required
                  placeholder="Enter your name"
                  icon={<User className="h-4 w-4" />}
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                />
              </div>
              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">Phone Number</label>
                <GlassInput
                  type="tel"
                  placeholder="+91 98765 43210"
                  icon={<Phone className="h-4 w-4" />}
                  value={phoneNumber}
                  onChange={(e) => setPhoneNumber(e.target.value)}
                />
              </div>
            </>
          )}

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Email Address</label>
            <GlassInput
              type="email"
              required
              placeholder="you@example.com"
              icon={<Mail className="h-4 w-4" />}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-slate-300 mb-1">Password</label>
            <GlassInput
              type="password"
              required
              placeholder="••••••••"
              icon={<Lock className="h-4 w-4" />}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <GlassButton
            type="submit"
            className="w-full mt-2 py-3"
            disabled={isLoading}
          >
            {isLoading ? 'Authenticating...' : isLogin ? 'Sign In' : 'Create Account'}
          </GlassButton>
        </form>

        {/* Switch Login / Register */}
        <div className="mt-5 text-center text-xs text-slate-400">
          {isLogin ? "Don't have an account? " : 'Already have an account? '}
          <button
            type="button"
            onClick={() => setIsLogin(!isLogin)}
            className="font-semibold text-orange-400 hover:text-orange-300 transition underline underline-offset-4"
          >
            {isLogin ? 'Sign Up' : 'Log In'}
          </button>
        </div>
      </div>
    </div>
  );
};
