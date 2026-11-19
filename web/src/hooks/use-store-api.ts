"use client";

import {
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import { http } from "@/lib/client";
import type {
  Address,
  Cart,
  CustomerProfile,
  Order,
  Product,
  Session,
} from "@/lib/types";
import { queryKeys } from "./query-keys";

export type AddressInput = Omit<Address, "id">;

const data = <T,>(request: Promise<{ data: T }>) => request.then((response) => response.data);

export function useSessionQuery() {
  return useQuery({
    queryKey: queryKeys.session,
    queryFn: () => data<Session>(http.get("/auth/session")),
    staleTime: 60_000,
    retry: false,
  });
}

export function useProductsQuery() {
  return useQuery({
    queryKey: queryKeys.products,
    queryFn: () => data<Product[]>(http.get("/store/products")),
  });
}

export function useProductQuery(id: string) {
  return useQuery({
    queryKey: queryKeys.product(id),
    queryFn: () => data<Product>(http.get(`/store/products/${id}`)),
    enabled: Boolean(id),
  });
}

export function useCartQuery() {
  return useQuery({
    queryKey: queryKeys.cart,
    queryFn: () => data<Cart>(http.get("/proxy/order/cart")),
    staleTime: 5_000,
  });
}

export function useWishlistQuery(enabled = true) {
  return useQuery({
    queryKey: queryKeys.wishlist,
    queryFn: () => data<Array<{ productId: string; addedAt: string }>>(
      http.get("/proxy/customer/customers/me/wishlist"),
    ),
    enabled,
  });
}

export function useProfileQuery(enabled = true) {
  return useQuery({
    queryKey: queryKeys.profile,
    queryFn: () => data<CustomerProfile>(http.get("/proxy/customer/customers/me")),
    enabled,
  });
}

export function useAddressesQuery(enabled = true) {
  return useQuery({
    queryKey: queryKeys.addresses,
    queryFn: () => data<Address[]>(http.get("/proxy/customer/customers/me/addresses")),
    enabled,
  });
}

export function useOrdersQuery(enabled = true) {
  return useQuery({
    queryKey: queryKeys.orders,
    queryFn: () => data<Order[]>(http.get("/proxy/order/orders/me", { params: { limit: 50 } })),
    enabled,
  });
}

export function useOrderQuery(id: string, enabled = true) {
  return useQuery({
    queryKey: queryKeys.order(id),
    queryFn: () => data<Order>(http.get(`/proxy/order/orders/${id}`)),
    enabled: enabled && Boolean(id),
  });
}

function useInvalidateAuthAndCart() {
  const queryClient = useQueryClient();
  return () => Promise.all([
    queryClient.invalidateQueries({ queryKey: queryKeys.session }),
    queryClient.invalidateQueries({ queryKey: queryKeys.cart }),
  ]);
}

export function useLoginMutation() {
  const invalidate = useInvalidateAuthAndCart();
  return useMutation({
    mutationFn: (credentials: { email: string; password: string }) =>
      data(http.post("/auth/login", credentials)),
    onSuccess: invalidate,
  });
}

export function useRegisterMutation() {
  const invalidate = useInvalidateAuthAndCart();
  return useMutation({
    mutationFn: (registration: { fullName: string; email: string; password: string }) =>
      data(http.post("/auth/register", registration)),
    onSuccess: invalidate,
  });
}

export function useLogoutMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () => data(http.post("/auth/logout")),
    onSuccess: async () => {
      queryClient.setQueryData<Session>(queryKeys.session, { authenticated: false });
      queryClient.removeQueries({ queryKey: queryKeys.profile });
      queryClient.removeQueries({ queryKey: queryKeys.addresses });
      queryClient.removeQueries({ queryKey: queryKeys.wishlist });
      queryClient.removeQueries({ queryKey: queryKeys.orders });
      await queryClient.invalidateQueries({ queryKey: queryKeys.cart });
    },
  });
}

export function useAddCartItemMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ productId, quantity = 1 }: { productId: string; quantity?: number }) =>
      data<Cart>(http.post("/proxy/order/cart/items", { productId, quantity })),
    onSuccess: (cart) => queryClient.setQueryData(queryKeys.cart, cart),
  });
}

export function useUpdateCartItemMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ productId, quantity }: { productId: string; quantity: number }) =>
      data<Cart>(http.patch(`/proxy/order/cart/items/${productId}`, { quantity })),
    onSuccess: (cart) => queryClient.setQueryData(queryKeys.cart, cart),
  });
}

export function useRemoveCartItemMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (productId: string) =>
      data<Cart>(http.delete(`/proxy/order/cart/items/${productId}`)),
    onSuccess: (cart) => queryClient.setQueryData(queryKeys.cart, cart),
  });
}

export function useAddWishlistMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (productId: string) =>
      data(http.post("/proxy/customer/customers/me/wishlist", { productId })),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.wishlist }),
  });
}

export function useRemoveWishlistMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (productId: string) =>
      data(http.delete(`/proxy/customer/customers/me/wishlist/${productId}`)),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.wishlist }),
  });
}

export function useUpdateProfileMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (profile: { fullName: FormDataEntryValue | null; phone: FormDataEntryValue | null }) =>
      data<CustomerProfile>(http.patch("/proxy/customer/customers/me", profile)),
    onSuccess: (profile) => {
      queryClient.setQueryData(queryKeys.profile, profile);
      void queryClient.invalidateQueries({ queryKey: queryKeys.session });
    },
  });
}

export function useSaveAddressMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ address, id }: { address: AddressInput; id?: string }) =>
      data<Address>(id
        ? http.put(`/proxy/customer/customers/me/addresses/${id}`, address)
        : http.post("/proxy/customer/customers/me/addresses", address)),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.addresses }),
  });
}

export function useDeleteAddressMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => data(http.delete(`/proxy/customer/customers/me/addresses/${id}`)),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.addresses }),
  });
}

export function useCheckoutMutation() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      shippingAddressId,
      paymentToken,
      idempotencyKey,
    }: {
      shippingAddressId: string;
      paymentToken: string;
      idempotencyKey: string;
    }) => data<Order>(http.post("/proxy/order/orders/checkout", {
      shippingAddressId,
      paymentMethod: { type: "CARD", token: paymentToken },
    }, { headers: { "Idempotency-Key": idempotencyKey } })),
    onSuccess: async (order) => {
      queryClient.setQueryData(queryKeys.order(order.id), order);
      await queryClient.invalidateQueries({ queryKey: queryKeys.orders });
      if (order.status === "PAID") {
        await queryClient.invalidateQueries({ queryKey: queryKeys.cart });
      }
    },
  });
}
