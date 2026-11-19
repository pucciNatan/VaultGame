"use client";

import { FormEvent, useState } from "react";
import { AuthGuard } from "@/components/auth-guard";
import { AddressForm, type AddressInput } from "@/components/address-form";
import type { Address } from "@/lib/types";
import {
  useAddressesQuery,
  useDeleteAddressMutation,
  useProfileQuery,
  useSaveAddressMutation,
  useUpdateProfileMutation,
} from "@/hooks/use-store-api";

function AccountContent() {
  const [editing, setEditing] = useState<Address | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [message, setMessage] = useState("");
  const profileQuery = useProfileQuery();
  const addressesQuery = useAddressesQuery();
  const updateProfileMutation = useUpdateProfileMutation();
  const saveAddressMutation = useSaveAddressMutation();
  const deleteAddressMutation = useDeleteAddressMutation();
  const profile = profileQuery.data;
  const addresses = addressesQuery.data ?? [];

  async function updateProfile(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    await updateProfileMutation.mutateAsync({
      fullName: data.get("fullName"),
      phone: data.get("phone"),
    });
    setMessage("Perfil atualizado.");
  }

  async function saveAddress(input: AddressInput) {
    await saveAddressMutation.mutateAsync({ address: input, id: editing?.id });
    setShowForm(false); setEditing(null);
  }

  async function removeAddress(id: string) {
    await deleteAddressMutation.mutateAsync(id);
  }

  if (!profile) return <div className="page-state">Carregando sua conta…</div>;
  return (
    <div className="container page-shell">
      <div className="page-head"><div><span className="eyebrow">Área do cliente</span><h1 className="page-title">Minha conta</h1></div></div>
      {message && <div className="success">{message}</div>}
      <form className="form-card wide" onSubmit={updateProfile}>
        <h2>Dados pessoais</h2>
        <div className="form-grid">
          <div className="form-group"><label>Nome completo</label><input name="fullName" defaultValue={profile.fullName} required /></div>
          <div className="form-group"><label>Telefone</label><input name="phone" defaultValue={profile.phone ?? ""} /></div>
          <div className="form-group full"><label>E-mail</label><input value={profile.email} disabled /></div>
        </div>
        <button className="primary">Atualizar perfil</button>
      </form>
      <div className="page-head"><h2>Endereços</h2><button className="primary" onClick={() => { setEditing(null); setShowForm(true); }}>Novo endereço</button></div>
      {(showForm || editing) && <div className="form-card wide"><AddressForm initial={editing ?? undefined} onSubmit={saveAddress} onCancel={() => { setShowForm(false); setEditing(null); }} /></div>}
      <div className="address-grid">{addresses.map((address) => (
        <article className="address-card" key={address.id}>
          <b>{address.label} {address.isDefault && <span className="status">Padrão</span>}</b>
          <p>{address.street}, {address.number} {address.complement} — {address.district}</p>
          <p>{address.city}/{address.state} • {address.postalCode}</p>
          <div className="form-actions"><button className="secondary" onClick={() => setEditing(address)}>Editar</button><button className="secondary danger" onClick={() => void removeAddress(address.id)}>Excluir</button></div>
        </article>
      ))}</div>
    </div>
  );
}

export default function AccountPage() {
  return <AuthGuard><AccountContent /></AuthGuard>;
}
