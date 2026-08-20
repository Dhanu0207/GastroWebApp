import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'sonner';
import { GlassNavbar } from './components/layout/GlassNavbar';
import { Footer } from './components/layout/Footer';
import { FloatingCartWidget } from './components/layout/FloatingCartWidget';

import { HomePage } from './pages/HomePage';
import { RestaurantDetailPage } from './pages/RestaurantDetailPage';
import { CartPage } from './pages/CartPage';
import { GroupCartPage } from './pages/GroupCartPage';
import { CheckoutPage } from './pages/CheckoutPage';
import { OrderTrackingPage } from './pages/OrderTrackingPage';
import { OrderHistoryPage } from './pages/OrderHistoryPage';
import { AdminDashboardPage } from './pages/AdminDashboardPage';

export function App() {
  return (
    <Router>
      <div className="relative min-h-screen flex flex-col justify-between selection:bg-orange-500 selection:text-white">
        
        {/* Sleek Dark Toast Notifications */}
        <Toaster 
          position="top-right" 
          theme="dark" 
          toastOptions={{
            style: {
              background: 'rgba(15, 23, 42, 0.85)',
              backdropFilter: 'blur(16px)',
              border: '1px solid rgba(255, 255, 255, 0.15)',
              color: '#ffffff',
            },
          }}
        />

        {/* Global Glass Navbar */}
        <GlassNavbar />

        {/* Main Content Area */}
        <main className="mx-auto w-full max-w-7xl px-4 pt-8 sm:px-6 lg:px-8 flex-1">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/restaurant/:id" element={<RestaurantDetailPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/group-cart" element={<GroupCartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/order-tracking/:orderId" element={<OrderTrackingPage />} />
            <Route path="/orders" element={<OrderHistoryPage />} />
            <Route path="/admin" element={<AdminDashboardPage />} />
          </Routes>
        </main>

        {/* Floating Glass Cart Widget (Bottom Right HUD) */}
        <FloatingCartWidget />

        {/* Global Footer */}
        <Footer />

      </div>
    </Router>
  );
}

export default App;
