"use client";

import Link from "next/link";
import { useMemo, useRef, useState } from "react";
import { AuthGuard } from "@/components/auth-guard";
import { formatMoney, orderStatusLabel } from "@/lib/client";
import type { Order } from "@/lib/types";
import {
  useAddressesQuery,
  useCartQuery,
  useCheckoutMutation,
  useProductsQuery,
} from "@/hooks/use-store-api";

function CheckoutContent() {
  const [addressId, setAddressId] = useState("");
  const [token, setToken] = useState("test-approved");
  const [error, setError] = useState("");
  const [order, setOrder] = useState<Order | null>(null);
  const idempotencyKey = useRef(crypto.randomUUID());
  const addressesQuery = useAddressesQuery();
  const cartQuery = useCartQuery();
  const productsQuery = useProductsQuery();
  const checkoutMutation = useCheckoutMutation();
  const addresses = addressesQuery.data ?? [];
  const cart = cartQuery.data;
  const selectedAddressId = addressId
    || addresses.find((item) => item.isDefault)?.id
    || addresses[0]?.id
    || "";

  const total = useMemo(() => {
    const products = productsQuery.data ?? [];
    const byId = new Map(products.map((product) => [product.id, Number(product.price)]));
    return cart?.items.reduce((sum, item) => sum + (byId.get(item.productId) ?? 0) * item.quantity, 0) ?? 0;
  }, [cart, productsQuery.data]);

  async function checkout() {
    if (!selectedAddressId) return setError("Cadastre e selecione um endereço.");
    setError("");
    try {
      const result = await checkoutMutation.mutateAsync({
        shippingAddressId: selectedAddressId,
        paymentToken: token,
        idempotencyKey: idempotencyKey.current,
      });
      setOrder(result);
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : "Falha no checkout.");
    }
  }

  if (order) return (
    <div className="container page-shell"><div className="form-card wide">
      <span className="eyebrow">Pedido recebido</span><h1>{orderStatusLabel(order.status)}</h1>
      <p>Pedido <b>#{order.id}</b></p><div className={`status ${order.status}`}>{order.status}</div>
      <p>Total: <b>{formatMoney(Number(order.totalAmount), order.currency)}</b></p>
      {order.status === "PAID" && <div className="success">Pagamento aprovado. O e-mail de confirmação será enviado pelo evento <code>vaultgame.order.paid</code>.</div>}
      <div className="form-actions"><Link className="primary" href={`/pedidos/${order.id}`}>Ver pedido</Link><Link className="secondary" href="/produtos">Continuar comprando</Link></div>
    </div></div>
  );

  return (
    <div className="container page-shell">
      <div className="page-head"><h1 className="page-title">Finalizar compra</h1></div>
      {error && <div className="error">{error}</div>}
      <div className="cart-layout">
        <div>
          <h2>1. Endereço de entrega</h2>
          <div className="address-grid">{addresses.map((address) => <button type="button" className={`address-card ${selectedAddressId === address.id ? "selected" : ""}`} key={address.id} onClick={() => setAddressId(address.id)} style={{textAlign:"left", color:"inherit"}}>
            <b>{address.label}</b><p>{address.street}, {address.number} — {address.city}/{address.state}</p>
          </button>)}</div>
          {!addresses.length && <p className="error">Você ainda não possui endereço. <Link href="/conta">Cadastrar endereço</Link></p>}
          <h2 style={{marginTop:35}}>2. Pagamento de teste</h2>
          <div className="form-group"><label>Resultado simulado</label><select value={token} onChange={(event) => setToken(event.target.value)}>
            <option value="test-approved">Aprovar pagamento</option>
            <option value="test-declined">Recusar pagamento</option>
            <option value="test-pending">Deixar pendente</option>
            <option value="test-error">Simular erro</option>
          </select></div>
        </div>
        <aside className="panel summary"><h2>Seu pedido</h2><div className="summary-line"><span>Itens</span><span>{cart?.items.reduce((sum,item) => sum + item.quantity,0) ?? 0}</span></div><div className="summary-total"><b>Total</b><b>{formatMoney(total)}</b></div><button className="primary" disabled={checkoutMutation.isPending || !cart?.items.length} onClick={() => void checkout()}>{checkoutMutation.isPending ? "Processando saga…" : "Confirmar compra"}</button></aside>
      </div>
    </div>
  );
}

export default function CheckoutPage() {
  return <AuthGuard><CheckoutContent /></AuthGuard>;
}
