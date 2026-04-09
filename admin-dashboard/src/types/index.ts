export interface User { id: string; email: string; fullName: string; phone?: string; role: string; profileImageUrl?: string; bioAuthEnabled: boolean; createdAt: string; }
export interface Product { id: string; name: string; description?: string; price: number; imageUrl?: string; category?: Category; stock: number; isActive: boolean; createdAt: string; }
export interface Category { id: string; name: string; description?: string; imageUrl?: string; }
export interface Coupon { id: string; code: string; description?: string; discountPercentage: number; maxDiscount?: number; minOrderAmount?: number; expiryDate?: string; isActive: boolean; usageLimit: number; usedCount: number; }
export interface OfferProduct { productId: string; productName: string; productImageUrl?: string; productPrice: number; quantity: number; }
export interface Offer { id: string; title: string; description?: string; bannerImageUrl?: string; discountPercentage: number; originalTotal: number; discountedTotal: number; startDate: string; endDate: string; isActive: boolean; products: OfferProduct[]; }
export interface OrderItem { id: string; productId: string; productName: string; productImageUrl?: string; quantity: number; unitPrice: number; }
export interface Address { id: string; label?: string; street: string; city: string; state?: string; zipCode?: string; }
export interface Order { id: string; subtotal: number; discountAmount: number; finalAmount: number; status: string; notes?: string; coupon?: Coupon; offer?: Offer; items: OrderItem[]; address: Address; customerName: string; customerEmail: string; createdAt: string; }
export interface TopProduct { productId: string; productName: string; totalSold: number; }
export interface DashboardStats { totalCustomers: number; totalProducts: number; totalOffers: number; totalCoupons: number; todayEarnings: number; monthEarnings: number; topSellingProducts: TopProduct[]; topOffers: { offerId: string; offerTitle: string; timesTaken: number }[]; lowStockProducts: Product[]; }
export interface MonthlyEarning { month: number; monthName: string; totalEarnings: number; }
export interface YearlyStats { year: number; monthlyEarnings: MonthlyEarning[]; yearTotal: number; }
export interface ApiResponse<T> { success: boolean; message: string; data: T; }
export interface PageResponse<T> { content: T[]; page: number; size: number; totalElements: number; totalPages: number; }
