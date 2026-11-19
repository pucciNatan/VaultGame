import { cookies } from "next/headers";

const serviceUrls = {
  catalog: process.env.CATALOG_API_URL ?? "http://localhost:8080",
  customer: process.env.CUSTOMER_API_URL ?? "http://localhost:8091",
  order: process.env.ORDER_API_URL ?? "http://localhost:8093",
  inventory: process.env.INVENTORY_API_URL ?? "http://localhost:8094",
} as const;

export type ServiceName = keyof typeof serviceUrls;

export async function serviceFetch(
  service: ServiceName,
  path: string,
  init: RequestInit = {},
) {
  const jar = await cookies();
  const token = jar.get("vaultgame_token")?.value;
  const cartId = jar.get("vaultgame_cart_id")?.value;
  const headers = new Headers(init.headers);

  if (token) headers.set("Authorization", `Bearer ${token}`);
  if (cartId) headers.set("X-Cart-Id", cartId);
  if (init.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  return fetch(`${serviceUrls[service]}${path}`, {
    ...init,
    headers,
    cache: "no-store",
  });
}

export async function ensureCartCookie() {
  const jar = await cookies();
  let cartId = jar.get("vaultgame_cart_id")?.value;
  if (!cartId) {
    cartId = crypto.randomUUID();
    jar.set("vaultgame_cart_id", cartId, {
      httpOnly: true,
      sameSite: "lax",
      secure: process.env.NODE_ENV === "production",
      path: "/",
      maxAge: 60 * 60 * 24 * 30,
    });
  }
  return cartId;
}

export async function normalizedError(response: Response) {
  let body: Record<string, unknown> = {};
  try {
    body = await response.json();
  } catch {
    // upstream may return an empty or non-JSON error
  }
  return {
    code: String(body.code ?? body.error ?? `HTTP_${response.status}`),
    message: String(body.message ?? "Não foi possível concluir a operação."),
  };
}

export async function proxyResponse(response: Response) {
  if (response.status === 204) return new Response(null, { status: 204 });
  const text = await response.text();
  return new Response(text, {
    status: response.status,
    headers: { "Content-Type": response.headers.get("content-type") ?? "application/json" },
  });
}
