import axios, { AxiosError } from "axios";
import type { ApiError } from "./types";

export class ApiClientError extends Error {
  constructor(
    message: string,
    public readonly code: string,
    public readonly status?: number,
  ) {
    super(message);
    this.name = "ApiClientError";
  }
}

export const http = axios.create({
  baseURL: "/api",
  timeout: 15_000,
  headers: { "Content-Type": "application/json" },
});

http.interceptors.response.use(
  (response) => response,
  (reason: AxiosError<ApiError>) => {
    const status = reason.response?.status;
    const payload = reason.response?.data;
    return Promise.reject(new ApiClientError(
      payload?.message ?? (reason.code === "ECONNABORTED"
        ? "O serviço demorou demais para responder."
        : "Não foi possível concluir a operação."),
      payload?.code ?? reason.code ?? `HTTP_${status ?? 500}`,
      status,
    ));
  },
);

export function formatMoney(value: number, currency = "BRL") {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency,
  }).format(value);
}

export function productImage(images?: string[]) {
  return images?.find((image) => /^https?:\/\//.test(image)) ?? null;
}

export function orderStatusLabel(status: string) {
  return {
    PAID: "Pagamento aprovado",
    PAYMENT_FAILED: "Pagamento recusado",
    PENDING_PAYMENT: "Pagamento pendente",
    CANCELLED: "Cancelado",
  }[status] ?? status;
}
