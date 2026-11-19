"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import { useLoginMutation } from "@/hooks/use-store-api";

export default function LoginPage() {
  const router = useRouter();
  const login = useLoginMutation();
  const [error, setError] = useState("");

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    const data = new FormData(event.currentTarget);
    try {
      await login.mutateAsync({
        email: String(data.get("email")),
        password: String(data.get("password")),
      });
      router.push("/");
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : "Não foi possível entrar.");
    }
  }

  return (
    <div className="container">
      <form className="form-card" onSubmit={submit}>
        <span className="eyebrow">Sua conta</span><h1>Entrar</h1>
        <p>Acesse seus pedidos, wishlist e checkout.</p>
        {error && <div className="error">{error}</div>}
        <div className="form-group"><label>E-mail</label><input name="email" type="email" required autoComplete="email" /></div>
        <div className="form-group"><label>Senha</label><input name="password" type="password" required minLength={8} autoComplete="current-password" /></div>
        <button className="primary" disabled={login.isPending}>{login.isPending ? "Entrando…" : "Entrar"}</button>
        <p>Ainda não tem conta? <Link href="/cadastro" style={{color:"var(--green)"}}>Cadastre-se</Link></p>
      </form>
    </div>
  );
}
