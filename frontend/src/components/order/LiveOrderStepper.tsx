import React from 'react';
import { CheckCircle2, Clock, ChefHat, Bike, CheckCheck } from 'lucide-react';
import { OrderStatus } from '../../types';

interface LiveOrderStepperProps {
  currentStatus: OrderStatus;
}

const STEPS: { status: OrderStatus; label: string; icon: React.ReactNode; desc: string }[] = [
  { status: 'PLACED', label: 'Order Placed', icon: <Clock className="h-4 w-4" />, desc: 'Sent to kitchen' },
  { status: 'CONFIRMED', label: 'Confirmed', icon: <CheckCircle2 className="h-4 w-4" />, desc: 'Restaurant accepted' },
  { status: 'PREPARING', label: 'Cooking', icon: <ChefHat className="h-4 w-4" />, desc: 'Master chefs at work' },
  { status: 'OUT_FOR_DELIVERY', label: 'On The Way', icon: <Bike className="h-4 w-4" />, desc: 'Valet en route' },
  { status: 'DELIVERED', label: 'Delivered', icon: <CheckCheck className="h-4 w-4" />, desc: 'Enjoy your meal!' },
];

export const LiveOrderStepper: React.FC<LiveOrderStepperProps> = ({ currentStatus }) => {
  const getStepIndex = (status: OrderStatus) => {
    switch (status) {
      case 'PENDING':
      case 'PLACED':
        return 0;
      case 'CONFIRMED':
        return 1;
      case 'PREPARING':
        return 2;
      case 'OUT_FOR_DELIVERY':
        return 3;
      case 'DELIVERED':
        return 4;
      default:
        return 0;
    }
  };

  const currentIndex = getStepIndex(currentStatus);

  return (
    <div className="relative py-6">
      {/* Progress Bar Line */}
      <div className="absolute top-11 left-6 right-6 h-1 bg-white/10 rounded-full">
        <div
          className="h-full bg-gradient-to-r from-orange-500 via-amber-400 to-emerald-400 rounded-full transition-all duration-700 ease-out shadow-lg shadow-orange-500/50"
          style={{ width: `${(currentIndex / (STEPS.length - 1)) * 100}%` }}
        />
      </div>

      {/* Steps Row */}
      <div className="relative z-10 flex items-start justify-between">
        {STEPS.map((step, idx) => {
          const isCompleted = idx <= currentIndex;
          const isCurrent = idx === currentIndex;

          return (
            <div key={step.status} className="flex flex-col items-center text-center max-w-[80px]">
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-2xl border transition-all duration-300 ${
                  isCurrent
                    ? 'border-orange-500 bg-orange-500 text-white shadow-lg shadow-orange-500/50 scale-110 animate-pulse-subtle'
                    : isCompleted
                    ? 'border-emerald-500/60 bg-emerald-500/20 text-emerald-400 backdrop-blur-md'
                    : 'border-white/10 bg-slate-900/80 text-slate-500 backdrop-blur-md'
                }`}
              >
                {step.icon}
              </div>
              <span
                className={`mt-3 text-xs font-bold tracking-tight ${
                  isCurrent ? 'text-orange-400' : isCompleted ? 'text-slate-200' : 'text-slate-500'
                }`}
              >
                {step.label}
              </span>
              <span className="hidden sm:block mt-0.5 text-[10px] text-slate-400 leading-tight">
                {step.desc}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
};
