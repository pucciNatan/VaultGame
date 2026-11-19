"use client";

import { Suspense, useMemo, useState } from "react";
import { useSearchParams } from "next/navigation";
import { ProductCard } from "@/components/product-card";
import { useProductsQuery } from "@/hooks/use-store-api";

function Catalog() {
  const params = useSearchParams();
  const { data: products = [], isPending: loading } = useProductsQuery();
  const [query, setQuery] = useState(params.get("q") ?? "");
  const [category, setCategory] = useState(params.get("category") ?? "");
  const [maxPrice, setMaxPrice] = useState("");

  const categories = [...new Set(products.map((product) => product.category))];
  const filtered = useMemo(() => products.filter((product) => {
    const text = `${product.name} ${product.brand} ${product.description}`.toLowerCase();
    return (!query || text.includes(query.toLowerCase()))
      && (!category || product.category === category)
      && (!maxPrice || Number(product.price) <= Number(maxPrice));
  }), [products, query, category, maxPrice]);

  return (
    <div className="container page-shell">
      <div className="page-head"><div><span className="eyebrow">VaultGame Store</span><h1 className="page-title">Catálogo</h1></div><span>{filtered.length} produtos</span></div>
      <div className="filters">
        <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Buscar por nome, marca…" />
        <select value={category} onChange={(event) => setCategory(event.target.value)}>
          <option value="">Todas as categorias</option>
          {categories.map((item) => <option key={item}>{item}</option>)}
        </select>
        <input type="number" min="0" value={maxPrice} onChange={(event) => setMaxPrice(event.target.value)} placeholder="Preço máximo" />
      </div>
      {loading ? <div className="page-state">Carregando produtos…</div> : filtered.length ? (
        <div className="product-grid">{filtered.map((product) => <ProductCard key={product.id} product={product} />)}</div>
      ) : <div className="page-state">Nenhum produto encontrado.</div>}
    </div>
  );
}

export default function ProductsPage() {
  return <Suspense fallback={<div className="page-state">Carregando…</div>}><Catalog /></Suspense>;
}
