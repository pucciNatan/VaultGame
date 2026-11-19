"use client";

import { FormEvent } from "react";
import type { Address } from "@/lib/types";

export type AddressInput = Omit<Address, "id">;

export function AddressForm({
  initial,
  onSubmit,
  onCancel,
}: {
  initial?: Address;
  onSubmit: (address: AddressInput) => Promise<void>;
  onCancel?: () => void;
}) {
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    await onSubmit({
      label: String(data.get("label")),
      street: String(data.get("street")),
      number: String(data.get("number")),
      complement: String(data.get("complement") ?? ""),
      district: String(data.get("district")),
      city: String(data.get("city")),
      state: String(data.get("state")),
      postalCode: String(data.get("postalCode")),
      country: "BR",
      type: data.get("type") as "DELIVERY" | "BILLING",
      isDefault: data.get("isDefault") === "on",
    });
    if (!initial) event.currentTarget.reset();
  }

  return (
    <form className="form-grid" onSubmit={submit}>
      <div className="form-group"><label>Identificação</label><input name="label" defaultValue={initial?.label} placeholder="Casa" required /></div>
      <div className="form-group"><label>CEP</label><input name="postalCode" defaultValue={initial?.postalCode} required /></div>
      <div className="form-group full"><label>Rua</label><input name="street" defaultValue={initial?.street} required /></div>
      <div className="form-group"><label>Número</label><input name="number" defaultValue={initial?.number} required /></div>
      <div className="form-group"><label>Complemento</label><input name="complement" defaultValue={initial?.complement} /></div>
      <div className="form-group"><label>Bairro</label><input name="district" defaultValue={initial?.district} required /></div>
      <div className="form-group"><label>Cidade</label><input name="city" defaultValue={initial?.city} required /></div>
      <div className="form-group"><label>Estado</label><input name="state" defaultValue={initial?.state} required /></div>
      <div className="form-group"><label>Tipo</label><select name="type" defaultValue={initial?.type ?? "DELIVERY"}><option value="DELIVERY">Entrega</option><option value="BILLING">Cobrança</option></select></div>
      <div className="form-group full"><label><input type="checkbox" name="isDefault" defaultChecked={initial?.isDefault} /> Endereço padrão</label></div>
      <div className="form-actions full"><button className="primary">Salvar endereço</button>{onCancel && <button type="button" className="secondary" onClick={onCancel}>Cancelar</button>}</div>
    </form>
  );
}
