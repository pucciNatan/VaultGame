"use client";
/* eslint-disable @next/next/no-img-element -- product URLs may come from arbitrary external CDNs */

import Link from "next/link";
import { useState } from "react";
import { formatMoney, productImage } from "@/lib/client";
import type { Product } from "@/lib/types";
import { useStore } from "./store-provider";

export function ProductCard({ product }: { product: Product }) {
  const { addToCart } = useStore();
  const [busy, setBusy] = useState(false);
  const image = productImage(product.images);

  async function add() {
    setBusy(true);
    try {
      await addToCart(product.id);
    } finally {
      setBusy(false);
    }
  }

  return (
    <article className="product-card">
      <Link href={`/produtos/${product.id}`} className="product-cover">
        {image ? <img src={image} alt={product.name} /> : (
          <div className="cover-fallback"><span>VG</span></div>
        )}
        {product.availableQuantity === 0 && <span className="stock-badge">Indisponível</span>}
      </Link>
      <div className="product-info">
        <span className="eyebrow">{product.category.replaceAll("_", " ")}</span>
        <Link href={`/produtos/${product.id}`}><h3>{product.name}</h3></Link>
        <p>{product.brand}</p>
        <div className="product-buy">
          <strong>{formatMoney(Number(product.price))}</strong>
          <button
            className="icon-button"
            disabled={busy || product.availableQuantity === 0}
            onClick={() => void add()}
            aria-label={`Adicionar ${product.name} ao carrinho`}
          >
            {busy ? "…" : "+"}
          </button>
        </div>
      </div>
    </article>
  );
}
