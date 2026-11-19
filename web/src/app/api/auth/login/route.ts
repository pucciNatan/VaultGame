import { cookies } from "next/headers";
import { ensureCartCookie, normalizedError, serviceFetch } from "@/lib/server";

export async function POST(request: Request) {
  const body = await request.text();
  const response = await serviceFetch("customer", "/auth/login", {
    method: "POST",
    body,
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    return Response.json(await normalizedError(response), { status: response.status });
  }

  const token = await response.json();
  const jar = await cookies();
  const guestCartId = await ensureCartCookie();
  jar.set("vaultgame_token", token.accessToken, {
    httpOnly: true,
    sameSite: "lax",
    secure: process.env.NODE_ENV === "production",
    path: "/",
    maxAge: token.expiresIn,
  });

  // The cookie is visible to subsequent serviceFetch calls in the same request only
  // through an explicit authorization header.
  await fetch(`${process.env.ORDER_API_URL ?? "http://localhost:8093"}/cart/merge`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token.accessToken}`,
      "X-Cart-Id": guestCartId,
    },
  }).catch(() => undefined);

  return Response.json({ authenticated: true });
}
