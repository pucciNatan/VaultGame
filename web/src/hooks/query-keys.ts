export const queryKeys = {
  session: ["session"] as const,
  products: ["products"] as const,
  product: (id: string) => ["products", id] as const,
  cart: ["cart"] as const,
  wishlist: ["wishlist"] as const,
  profile: ["profile"] as const,
  addresses: ["addresses"] as const,
  orders: ["orders"] as const,
  order: (id: string) => ["orders", id] as const,
};
