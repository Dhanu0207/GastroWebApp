import React from 'react';
import { Sparkles, Heart, ShieldCheck, Zap, UtensilsCrossed } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="mt-24 border-t border-white/10 bg-slate-950/60 backdrop-blur-xl">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          
          {/* Brand Column */}
          <div className="space-y-3 md:col-span-1">
            <div className="flex items-center gap-2">
              <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-tr from-orange-500 to-amber-400 text-white font-black shadow-lg shadow-orange-500/30">
                <Sparkles className="h-4 w-4" />
              </div>
              <span className="text-xl font-black tracking-tight text-white">
                GASTRO<span className="text-orange-500">.</span>
              </span>
            </div>
            <p className="text-xs text-slate-400 leading-relaxed">
              Curated artisanal gastronomy, ultra-fast delivery, and real-time collaborative group ordering.
            </p>
          </div>

          {/* Features */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-200 mb-3 flex items-center gap-1.5">
              <Zap className="h-3.5 w-3.5 text-orange-400" /> Key Highlights
            </h4>
            <ul className="space-y-2 text-xs text-slate-400">
              <li>• Real-Time Group Cart Sharing</li>
              <li>• Spring Boot 3 & JWT Security</li>
              <li>• Razorpay Instant Checkout</li>
              <li>• Live Order Tracking Stepper</li>
            </ul>
          </div>

          {/* Cuisines */}
          <div>
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-200 mb-3 flex items-center gap-1.5">
              <UtensilsCrossed className="h-3.5 w-3.5 text-amber-400" /> Cuisines
            </h4>
            <ul className="space-y-2 text-xs text-slate-400">
              <li>• Smoked Burgers & BBQ</li>
              <li>• Woodfired Artisan Pizza</li>
              <li>• Kyoto Nigiri & Ramen</li>
              <li>• Hyderabadi Dum Biryani</li>
            </ul>
          </div>

          {/* Trust Badge */}
          <div className="rounded-2xl border border-white/10 bg-white/[0.03] p-4 backdrop-blur-md">
            <div className="flex items-center gap-2 text-emerald-400 font-semibold text-xs mb-1">
              <ShieldCheck className="h-4 w-4" /> 100% Gourmet Guaranteed
            </div>
            <p className="text-[11px] text-slate-400">
              Prepared in hygienic state-of-the-art kitchens with fresh organic ingredients.
            </p>
          </div>
        </div>

        <div className="mt-8 border-t border-white/5 pt-6 flex flex-col sm:flex-row items-center justify-between text-xs text-slate-400">
          <p>© 2026 Gastro Food Delivery. Designed with Glassmorphism UI & React + TypeScript.</p>
          <p className="flex items-center gap-1 mt-2 sm:mt-0">
            Powered by Spring Boot & React <Heart className="h-3 w-3 text-orange-500 fill-orange-500" />
          </p>
        </div>
      </div>
    </footer>
  );
};
