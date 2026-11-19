"use client";
/* eslint-disable @next/next/no-img-element -- product URLs may come from arbitrary external CDNs */

import Link from "next/link";
import { useMemo } from "react";
import { formatMoney, productImage } from "@/lib/client";
import type { CartProduct } from "@/lib/types";
import {
  useCartQuery,
  useProductsQuery,
  useRemoveCartItemMutation,
  useUpdateCartItemMutation,
} from "@/hooks/use-store-api";

export default function CartPage() {
  const cartQuery = useCartQuery();
  const productsQuery = useProductsQuery();
  const updateItem = useUpdateCartItemMutation();
  const removeItem = useRemoveCartItemMutation();
  const items = useMemo<CartProduct[]>(() => {
    const products = productsQuery.data ?? [];
    const byId = new Map(products.map((product) => [product.id, product]));
    return (cartQuery.data?.items ?? []).flatMap((item) => {
      const product = byId.get(item.productId);
      return product ? [{ ...product, quantity: item.quantity, lineTotal: Number(product.price) * item.quantity }] : [];
    });
  }, [cartQuery.data, productsQuery.data]);

  async function quantity(productId: string, current: number, delta: number) {
    const next = current + delta;
    if (next < 1) return remove(productId);
    await updateItem.mutateAsync({ productId, quantity: next });
  }

  async function remove(productId: string) {
    await removeItem.mutateAsync(productId);
  }

  const total = useMemo(() => items.reduce((sum, item) => sum + item.lineTotal, 0), [items]);
  if (cartQuery.isPending || productsQuery.isPending) return <div className="page-state">Carregando carrinho…</div>;

  return (
    <div className="container page-shell">
      <div className="page-head"><h1 className="page-title">Meu carrinho</h1><span>{items.length} produto(s)</span></div>
      {!items.length ? <div className="page-state"><p>Seu carrinho está vazio.</p><Link className="primary" href="/produtos">Explorar produtos</Link></div> : (
        <div className="cart-layout">
          <div className="panel">
            {items.map((item) => {
              const image = productImage(item.images);
              return <div className="cart-row" key={item.id}>
                {image ? <img className="cart-thumb" src={image} alt="" /> : <div className="cart-thumb cover-fallback" />}
                <div><b>{item.name}</b><p>{formatMoney(Number(item.price))} cada</p></div>
                <div className="quantity"><button onClick={() => void quantity(item.id, item.quantity, -1)}>−</button><b>{item.quantity}</b><button onClick={() => void quantity(item.id, item.quantity, 1)}>+</button></div>
                <div><b>{formatMoney(item.lineTotal)}</b><br /><button className="link-button" onClick={() => void remove(item.id)}>Remover</button></div>
              </div>;
            })}
          </div>
          <aside className="panel summary">
            <h2>Resumo</h2>
            <div className="summary-line"><span>Subtotal</span><span>{formatMoney(total)}</span></div>
            <div className="summary-line"><span>Entrega</span><span>Grátis</span></div>
            <div className="summary-total"><b>Total</b><b>{formatMoney(total)}</b></div>
            <Link className="primary" href="/checkout">Ir para checkout</Link>
          </aside>
        </div>
      )}
    </div>
  );
}
