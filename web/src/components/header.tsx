"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import { useStore } from "./store-provider";

export function Header() {
  const router = useRouter();
  const { session, cartCount, logout } = useStore();
  const [query, setQuery] = useState("");

  function search(event: FormEvent) {
    event.preventDefault();
    router.push(`/produtos?q=${encodeURIComponent(query)}`);
  }

  return (
    <>
      <div className="promo-bar">Entrega digital rápida • Pagamento seguro • Ofertas toda semana</div>
      <header className="site-header">
        <div className="header-main container">
          <Link href="/" className="brand"><span>V</span>VaultGame</Link>
          <form className="search" onSubmit={search}>
            <input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="O que você está procurando?"
              aria-label="Buscar produtos"
            />
            <button type="submit">Buscar</button>
          </form>
          <nav className="header-actions">
            {session.authenticated ? (
              <>
                <Link href="/conta">Olá, {session.user?.fullName?.split(" ")[0] ?? "jogador"}</Link>
                <button className="link-button" onClick={() => void logout()}>Sair</button>
              </>
            ) : <Link href="/entrar">Entrar</Link>}
            <Link href="/wishlist" aria-label="Wishlist">♡</Link>
            <Link href="/carrinho" className="cart-link">Carrinho <b>{cartCount}</b></Link>
          </nav>
        </div>
        <nav className="category-nav container">
          <Link href="/produtos">Todos os jogos</Link>
          <Link href="/produtos?category=GAME">PC</Link>
          <Link href="/produtos?category=DIGITAL_CARD">Gift Cards</Link>
          <Link href="/produtos?category=HARDWARE">Hardware</Link>
          <Link href="/wishlist">Lista de desejos</Link>
          {session.authenticated && <Link href="/pedidos">Meus pedidos</Link>}
        </nav>
      </header>
    </>
  );
}
