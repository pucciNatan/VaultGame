"use client";

import { createContext, useCallback, useContext, useMemo } from "react";
import type { Session } from "@/lib/types";
import {
  useAddCartItemMutation,
  useCartQuery,
  useLogoutMutation,
  useSessionQuery,
} from "@/hooks/use-store-api";

type StoreContextValue = {
  session: Session;
  sessionLoading: boolean;
  cartCount: number;
  refreshSession: () => Promise<void>;
  refreshCart: () => Promise<void>;
  addToCart: (productId: string, quantity?: number) => Promise<void>;
  logout: () => Promise<void>;
};

const StoreContext = createContext<StoreContextValue | null>(null);
const ANONYMOUS_SESSION: Session = { authenticated: false };

export function StoreProvider({ children }: { children: React.ReactNode }) {
  const sessionQuery = useSessionQuery();
  const cartQuery = useCartQuery();
  const addCartItem = useAddCartItemMutation();
  const logoutMutation = useLogoutMutation();
  const session = sessionQuery.data ?? ANONYMOUS_SESSION;
  const cartCount = cartQuery.data?.items.reduce((total, item) => total + item.quantity, 0) ?? 0;

  const addToCart = useCallback(async (productId: string, quantity = 1) => {
    await addCartItem.mutateAsync({ productId, quantity });
  }, [addCartItem]);

  const logout = useCallback(async () => {
    await logoutMutation.mutateAsync();
  }, [logoutMutation]);

  const value = useMemo(() => ({
    session,
    sessionLoading: sessionQuery.isPending,
    cartCount,
    refreshSession: async () => { await sessionQuery.refetch(); },
    refreshCart: async () => { await cartQuery.refetch(); },
    addToCart,
    logout,
  }), [session, sessionQuery, cartQuery, cartCount, addToCart, logout]);

  return <StoreContext.Provider value={value}>{children}</StoreContext.Provider>;
}

export function useStore() {
  const context = useContext(StoreContext);
  if (!context) throw new Error("useStore must be used within StoreProvider");
  return context;
}
