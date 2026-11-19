"use client";

import Link from "next/link";
import { ProductCard } from "@/components/product-card";
import { useProductsQuery } from "@/hooks/use-store-api";

export default function Home() {
  const { data: products = [] } = useProductsQuery();

  return (
    <>
      <section className="hero">
        <div className="container hero-grid">
          <div>
            <span className="eyebrow">Bem-vindo ao seu próximo universo</span>
            <h1>Jogue mais.<br /><em>Pague menos.</em></h1>
            <p>Os maiores jogos, gift cards e equipamentos em uma experiência rápida, segura e feita para gamers.</p>
            <Link className="primary" href="/produtos">Explorar catálogo</Link>
          </div>
          <div className="hero-art"><div className="hero-orb" /></div>
        </div>
      </section>
      <section className="section">
        <div className="container">
          <div className="section-heading"><div><span className="eyebrow">Em destaque</span><h2>Mais procurados</h2></div><Link href="/produtos">Ver todos →</Link></div>
          {products.length ? <div className="product-grid">{products.slice(0, 8).map((product) => <ProductCard key={product.id} product={product} />)}</div> : <div className="page-state">Carregando catálogo ou aguardando o serviço de produtos…</div>}
        </div>
      </section>
      <section className="section alt">
        <div className="container benefits">
          <div className="benefit"><b>Entrega digital</b><p>Produtos digitais disponíveis assim que o pagamento for aprovado.</p></div>
          <div className="benefit"><b>Checkout via Kafka</b><p>Uma saga distribuída coordena catálogo, estoque e pagamento.</p></div>
          <div className="benefit"><b>Suporte local</b><p>Ambiente completo para testar todos os microsserviços VaultGame.</p></div>
        </div>
      </section>
    </>
  );
}
