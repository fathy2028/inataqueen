import axios from 'axios';
import type { ApiResponse, PageResponse, Product, Category, Coupon, Offer, Order, User, DashboardStats, YearlyStats } from '../types';

// Runtime config injected by Docker, falls back to localhost for development
const BACKEND_URL = (window as any).__RUNTIME_CONFIG__?.BACKEND_URL || 'https://back.queeny-pharmacy.online';
const api = axios.create({ baseURL: `${BACKEND_URL}/api` });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use((res) => res, (error) => {
  if (error.response?.status === 401) { localStorage.removeItem('token'); window.location.href = '/login'; }
  return Promise.reject(error);
});

const unwrap = <T>(res: { data: ApiResponse<T> }) => res.data.data;

export const auth = {
  login: (email: string, password: string) => api.post('/auth/login', { email, password }).then(unwrap),
};

export const products = {
  getAll: (page = 0, size = 20) => api.get<ApiResponse<PageResponse<Product>>>(`/products?page=${page}&size=${size}`).then(unwrap),
  create: (data: Partial<Product> & { categoryId?: string }) => api.post('/products', data).then(unwrap),
  update: (id: string, data: Partial<Product> & { categoryId?: string }) => api.put(`/products/${id}`, data).then(unwrap),
  delete: (id: string) => api.delete(`/products/${id}`).then(unwrap),
  getLowStock: () => api.get<ApiResponse<Product[]>>('/products/low-stock').then(unwrap),
};

export const categories = {
  getAll: () => api.get<ApiResponse<Category[]>>('/categories').then(unwrap),
  create: (data: Partial<Category>) => api.post('/categories', data).then(unwrap),
  update: (id: string, data: Partial<Category>) => api.put(`/categories/${id}`, data).then(unwrap),
  delete: (id: string) => api.delete(`/categories/${id}`).then(unwrap),
};

export const coupons = {
  getAll: () => api.get<ApiResponse<Coupon[]>>('/coupons').then(unwrap),
  create: (data: Partial<Coupon>) => api.post('/coupons', data).then(unwrap),
  update: (id: string, data: Partial<Coupon>) => api.put(`/coupons/${id}`, data).then(unwrap),
  delete: (id: string) => api.delete(`/coupons/${id}`).then(unwrap),
};

export const offers = {
  getAll: () => api.get<ApiResponse<Offer[]>>('/offers').then(unwrap),
  create: (data: unknown) => api.post('/offers', data).then(unwrap),
  update: (id: string, data: unknown) => api.put(`/offers/${id}`, data).then(unwrap),
  delete: (id: string) => api.delete(`/offers/${id}`).then(unwrap),
};

export const orders = {
  getAll: (page = 0, size = 20, status?: string) => api.get<ApiResponse<PageResponse<Order>>>(`/orders/all?page=${page}&size=${size}${status ? `&status=${status}` : ''}`).then(unwrap),
  getById: (id: string) => api.get<ApiResponse<Order>>(`/orders/${id}`).then(unwrap),
  updateStatus: (id: string, status: string) => api.put(`/orders/${id}/status`, { status }).then(unwrap),
};

export const users = {
  getAll: (page = 0, size = 20) => api.get<ApiResponse<PageResponse<User>>>(`/users?page=${page}&size=${size}`).then(unwrap),
  delete: (id: string) => api.delete(`/users/${id}`).then(unwrap),
};

export const statistics = {
  getDashboard: () => api.get<ApiResponse<DashboardStats>>('/statistics/dashboard').then(unwrap),
  getYearly: (year: number) => api.get<ApiResponse<YearlyStats>>(`/statistics/yearly?year=${year}`).then(unwrap),
};

export const notifications = {
  send: (data: { title: string; body: string; type: string; targetUserIds?: string[] }) => api.post('/notifications/send', data).then(unwrap),
};
