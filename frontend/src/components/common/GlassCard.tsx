import React from 'react';
import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

interface GlassCardProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode;
  glow?: boolean;
  glowColor?: string;
  hoverEffect?: boolean;
}

export const GlassCard: React.FC<GlassCardProps> = ({
  children,
  className,
  glow = false,
  glowColor = 'rgba(255, 94, 30, 0.2)',
  hoverEffect = true,
  ...props
}) => {
  return (
    <div
      className={twMerge(
        'relative overflow-hidden rounded-2xl border border-white/10 bg-white/[0.04] p-5 backdrop-blur-xl',
        hoverEffect && 'glass-panel-hover',
        className
      )}
      {...props}
    >
      {glow && (
        <div
          className="pointer-events-none absolute -top-12 -right-12 h-36 w-36 rounded-full blur-3xl opacity-70"
          style={{ backgroundColor: glowColor }}
        />
      )}
      <div className="relative z-10">{children}</div>
    </div>
  );
};
