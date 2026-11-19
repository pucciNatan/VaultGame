import { cookies } from "next/headers";

export async function POST() {
  const jar = await cookies();
  jar.delete("vaultgame_token");
  return Response.json({ authenticated: false });
}
