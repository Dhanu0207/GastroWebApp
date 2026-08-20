import React from 'react';
import { twMerge } from 'tailwind-merge';

interface GlassButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  icon?: React.ReactNode;
}

export const GlassButton: React.FC<GlassButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  icon,
  className,
  ...props
}) => {
  const sizeClasses = {
    sm: 'px-3 py-1.5 text-xs rounded-lg',
    md: 'px-4 py-2 text-sm rounded-xl',
    lg: 'px-6 py-3 text-base rounded-xl font-semibold',
  }[size];

  const variantClasses = {
    primary: 'glass-button-primary text-white font-medium',
    secondary: 'bg-white/10 hover:bg-white/15 text-white border border-white/10 backdrop-blur-md active:scale-95 transition-all',
    ghost: 'bg-transparent hover:bg-white/10 text-slate-300 hover:text-white transition-all',
    danger: 'bg-red-500/20 hover:bg-red-500/30 text-red-300 border border-red-500/30 transition-all',
  }[variant];

  return (
    <button
      className={twMerge(
        'inline-flex items-center justify-center gap-2 transition-all duration-200 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed',
        sizeClasses,
        variantClasses,
        className
      )}
      {...props}
    >
      {icon && <span className="shrink-0">{icon}</span>}
      {children}
    </button>
  );
};

export const GlassBadge: React.FC<{
  children: React.ReactNode;
  variant?: 'orange' | 'green' | 'blue' | 'purple' | 'neutral';
  className?: string;
}> = ({ children, variant = 'orange', className }) => {
  const variantStyles = {
    orange: 'bg-orange-500/15 text-orange-300 border-orange-500/30',
    green: 'bg-emerald-500/15 text-emerald-300 border-emerald-500/30',
    blue: 'bg-blue-500/15 text-blue-300 border-blue-500/30',
    purple: 'bg-purple-500/15 text-purple-300 border-purple-500/30',
    neutral: 'bg-white/10 text-slate-300 border-white/15',
  }[variant];

  return (
    <span
      className={twMerge(
        'inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-semibold rounded-full border backdrop-blur-md',
        variantStyles,
        className
      )}
    >
      {children}
    </span>
  );
};

export const GlassInput: React.FC<React.InputHTMLAttributes<HTMLInputElement> & { icon?: React.ReactNode }> = ({
  icon,
  className,
  ...props
}) => {
  return (
    <div className="relative flex items-center w-full">
      {icon && (
        <span className="absolute left-3.5 text-slate-400 pointer-events-none">
          {icon}
        </span>
      )}
      <input
        className={twMerge(
          'glass-input w-full rounded-xl py-2.5 text-sm text-white placeholder-slate-400',
          icon ? 'pl-10 pr-4' : 'px-4',
          className
        )}
        {...props}
      />
    </div>
  );
};
