"use client";

import { useParams } from "next/navigation";
import { Suspense } from "react";
import { AuthGuard } from "@/components/auth-guard";
import { formatMoney, orderStatusLabel } from "@/lib/client";
import { useOrderQuery } from "@/hooks/use-store-api";

function OrderDetail() {
  const { id } = useParams<{ id: string }>();
  const { data: order, isPending } = useOrderQuery(id);
  if (isPending || !order) return <div className="page-state">Carregando pedido…</div>;
  return (
    <div className="container page-shell">
      <div className="page-head"><div><span className="eyebrow">Detalhes do pedido</span><h1 className="page-title">#{order.id}</h1></div><span className={`status ${order.status}`}>{orderStatusLabel(order.status)}</span></div>
      <div className="cart-layout">
        <div className="panel">{order.lines.map((line) => <div className="cart-row" key={line.productId}>
          <div className="cart-thumb cover-fallback" /><div><b>{line.productName}</b><p>{line.quantity} × {formatMoney(Number(line.unitPrice))}</p></div><span /><b>{formatMoney(Number(line.lineTotal))}</b>
        </div>)}</div>
        <aside className="panel summary"><h2>Resumo</h2><div className="summary-line"><span>Data</span><span>{new Date(order.createdAt).toLocaleDateString("pt-BR")}</span></div><div className="summary-line"><span>Transação</span><span>{order.paymentTransactionId?.slice(0,8) ?? "—"}</span></div><div className="summary-total"><b>Total</b><b>{formatMoney(Number(order.totalAmount), order.currency)}</b></div></aside>
      </div>
    </div>
  );
}

export default function OrderDetailPage() {
  return <AuthGuard><Suspense fallback={<div className="page-state">Carregando pedido…</div>}><OrderDetail /></Suspense></AuthGuard>;
}
