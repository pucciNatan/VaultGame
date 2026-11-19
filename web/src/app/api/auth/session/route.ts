import { cookies } from "next/headers";
import { serviceFetch } from "@/lib/server";

export async function GET() {
  const jar = await cookies();
  if (!jar.get("vaultgame_token")) {
    return Response.json({ authenticated: false });
  }
  const response = await serviceFetch("customer", "/customers/me");
  if (!response.ok) {
    jar.delete("vaultgame_token");
    return Response.json({ authenticated: false });
  }
  const profile = await response.json();
  return Response.json({
    authenticated: true,
    user: {
      id: profile.id,
      email: profile.email,
      fullName: profile.fullName,
      role: "CUSTOMER",
    },
  });
}
