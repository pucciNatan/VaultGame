import { cookies } from "next/headers";
import { normalizedError, serviceFetch } from "@/lib/server";

export async function POST(request: Request) {
  const body = await request.text();
  const response = await serviceFetch("customer", "/auth/register", {
    method: "POST",
    body,
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    return Response.json(await normalizedError(response), { status: response.status });
  }

  const registration = await response.json();
  const jar = await cookies();
  jar.set("vaultgame_token", registration.tokens.accessToken, {
    httpOnly: true,
    sameSite: "lax",
    secure: process.env.NODE_ENV === "production",
    path: "/",
    maxAge: registration.tokens.expiresIn,
  });
  return Response.json({
    authenticated: true,
    user: {
      id: registration.userId,
      email: registration.email,
      fullName: registration.fullName,
    },
  }, { status: 201 });
}
