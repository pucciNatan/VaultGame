"use client";

import Link from "next/link";
import { AuthGuard } from "@/components/auth-guard";
import { formatMoney, orderStatusLabel } from "@/lib/client";
import { useOrdersQuery } from "@/hooks/use-store-api";

function OrdersContent() {
  const { data: orders, isPending } = useOrdersQuery();
  if (isPending || !orders) return <div className="page-state">Carregando pedidos…</div>;
  return (
    <div className="container page-shell">
      <div className="page-head"><h1 className="page-title">Meus pedidos</h1><span>{orders.length} pedido(s)</span></div>
      <div className="order-list">{orders.map((order) => <Link href={`/pedidos/${order.id}`} className="order-card" key={order.id}>
        <div><b>Pedido #{order.id.slice(0, 8)}</b><p>{new Date(order.createdAt).toLocaleString("pt-BR")} • {order.lines.length} item(ns)</p></div>
        <div style={{textAlign:"right"}}><span className={`status ${order.status}`}>{orderStatusLabel(order.status)}</span><p><b>{formatMoney(Number(order.totalAmount), order.currency)}</b></p></div>
      </Link>)}</div>
      {!orders.length && <div className="page-state">Você ainda não fez nenhum pedido.</div>}
    </div>
  );
}

export default function OrdersPage() {
  return <AuthGuard><OrdersContent /></AuthGuard>;
}
