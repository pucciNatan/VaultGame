export type Product = {
  id: string;
  name: string;
  description: string;
  price: number;
  category: string;
  brand: string;
  gtin?: string | null;
  images: string[];
  details?: Record<string, unknown> | null;
  active: boolean;
  availableQuantity?: number;
};

export type Cart = {
  cartKey: string;
  items: Array<{ productId: string; quantity: number }>;
};

export type CartProduct = Product & { quantity: number; lineTotal: number };

export type Address = {
  id: string;
  label: string;
  street: string;
  number: string;
  complement?: string;
  district: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  type: "DELIVERY" | "BILLING";
  isDefault: boolean;
};

export type CustomerProfile = {
  id: string;
  email: string;
  fullName: string;
  phone?: string | null;
};

export type Order = {
  id: string;
  status: "PENDING_PAYMENT" | "PAID" | "PAYMENT_FAILED" | "CANCELLED";
  currency: string;
  totalAmount: number;
  paymentTransactionId?: string | null;
  shippingAddressId: string;
  lines: Array<{
    productId: string;
    productName: string;
    unitPrice: number;
    quantity: number;
    lineTotal: number;
  }>;
  createdAt: string;
};

export type Session = {
  authenticated: boolean;
  user?: { id: string; email: string; role: string; fullName?: string };
};

export type ApiError = { code: string; message: string };
