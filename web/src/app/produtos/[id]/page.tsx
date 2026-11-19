"use client";
/* eslint-disable @next/next/no-img-element -- product URLs may come from arbitrary external CDNs */

import { useParams, useRouter } from "next/navigation";
import { Suspense, useState } from "react";
import { formatMoney, productImage } from "@/lib/client";
import { useStore } from "@/components/store-provider";
import { useAddWishlistMutation, useProductQuery } from "@/hooks/use-store-api";

function ProductDetail() {
  const { id } = useParams<{ id: string }>();
  const router = useRouter();
  const { addToCart, session } = useStore();
  const [message, setMessage] = useState("");
  const { data: product, isPending } = useProductQuery(id);
  const addWishlist = useAddWishlistMutation();

  if (isPending || !product) return <div className="page-state">Carregando produto…</div>;
  const image = productImage(product.images);

  async function wishlist() {
    if (!session.authenticated) return router.push("/entrar");
    await addWishlist.mutateAsync(id);
    setMessage("Produto adicionado à sua lista de desejos.");
  }

  return (
    <div className="container page-shell">
      <div className="detail-grid">
        <div className="detail-image">{image ? <img src={image} alt={product.name} /> : <div className="cover-fallback"><span>VG</span></div>}</div>
        <div className="detail-copy">
          <span className="eyebrow">{product.category}</span>
          <h1>{product.name}</h1>
          <p className="description">{product.description}</p>
          <div className="detail-price">{formatMoney(Number(product.price))}</div>
          <p>{product.availableQuantity ? `${product.availableQuantity} unidade(s) disponível(is)` : "Produto indisponível"}</p>
          <div className="detail-actions">
            <button className="primary" disabled={!product.availableQuantity} onClick={() => void addToCart(product.id)}>Adicionar ao carrinho</button>
            <button className="secondary" onClick={() => void wishlist()}>♡ Lista de desejos</button>
          </div>
          {message && <p className="success">{message}</p>}
          <div className="meta-list">
            <span>Marca: <b>{product.brand}</b></span>
            <span>Categoria: <b>{product.category}</b></span>
            {Object.entries(product.details ?? {}).slice(0, 6).map(([key, value]) => (
              <span key={key}>{key}: <b>{String(value ?? "-")}</b></span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default function ProductDetailPage() {
  return <Suspense fallback={<div className="page-state">Carregando produto…</div>}><ProductDetail /></Suspense>;
}
