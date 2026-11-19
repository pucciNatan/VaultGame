"use client";

import { AuthGuard } from "@/components/auth-guard";
import { ProductCard } from "@/components/product-card";
import {
  useProductsQuery,
  useRemoveWishlistMutation,
  useWishlistQuery,
} from "@/hooks/use-store-api";

function WishlistContent() {
  const wishlistQuery = useWishlistQuery();
  const productsQuery = useProductsQuery();
  const removeWishlist = useRemoveWishlistMutation();
  const ids = new Set(wishlistQuery.data?.map((item) => item.productId) ?? []);
  const products = productsQuery.data?.filter((product) => ids.has(product.id)) ?? [];
  const loading = wishlistQuery.isPending || productsQuery.isPending;

  return (
    <div className="container page-shell">
      <div className="page-head"><h1 className="page-title">Lista de desejos</h1><span>{products.length} item(ns)</span></div>
      {loading ? <div className="page-state">Carregando…</div> : products.length ? (
        <>
          <div className="product-grid">{products.map((product) => <ProductCard key={product.id} product={product} />)}</div>
          <div className="form-actions">{products.map((product) => <button key={product.id} className="link-button" onClick={async () => {
            await removeWishlist.mutateAsync(product.id);
          }}>Remover {product.name}</button>)}</div>
        </>
      ) : <div className="page-state">Sua lista de desejos está vazia.</div>}
    </div>
  );
}

export default function WishlistPage() {
  return <AuthGuard><WishlistContent /></AuthGuard>;
}
