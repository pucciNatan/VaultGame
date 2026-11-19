"use client";

import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import { useRegisterMutation } from "@/hooks/use-store-api";

export default function RegisterPage() {
  const router = useRouter();
  const register = useRegisterMutation();
  const [error, setError] = useState("");

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    setError("");
    try {
      await register.mutateAsync({
        fullName: String(data.get("fullName")),
        email: String(data.get("email")),
        password: String(data.get("password")),
      });
      router.push("/conta");
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : "Não foi possível cadastrar.");
    }
  }

  return (
    <div className="container">
      <form className="form-card" onSubmit={submit}>
        <span className="eyebrow">Junte-se à VaultGame</span><h1>Criar conta</h1>
        <p>Leva menos de um minuto.</p>
        {error && <div className="error">{error}</div>}
        <div className="form-group"><label>Nome completo</label><input name="fullName" required /></div>
        <div className="form-group"><label>E-mail</label><input name="email" type="email" required /></div>
        <div className="form-group"><label>Senha</label><input name="password" type="password" minLength={8} maxLength={72} required /></div>
        <button className="primary" disabled={register.isPending}>{register.isPending ? "Criando…" : "Criar minha conta"}</button>
      </form>
    </div>
  );
}
