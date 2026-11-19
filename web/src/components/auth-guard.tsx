"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { useStore } from "./store-provider";

export function AuthGuard({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const { session, sessionLoading } = useStore();

  useEffect(() => {
    if (!sessionLoading && !session.authenticated) router.replace("/entrar");
  }, [sessionLoading, session.authenticated, router]);

  if (sessionLoading || !session.authenticated) {
    return <div className="page-state">Verificando sua sessão…</div>;
  }
  return children;
}
