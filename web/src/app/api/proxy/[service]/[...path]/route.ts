import { ensureCartCookie, proxyResponse, serviceFetch, type ServiceName } from "@/lib/server";
import type { NextRequest } from "next/server";

const allowed = new Set<ServiceName>(["catalog", "customer", "order", "inventory"]);

async function forward(
  request: NextRequest,
  context: { params: Promise<{ service: string; path: string[] }> },
) {
  const { service, path } = await context.params;
  if (!allowed.has(service as ServiceName)) {
    return Response.json({ code: "INVALID_SERVICE", message: "Serviço inválido." }, { status: 404 });
  }

  await ensureCartCookie();
  const query = request.nextUrl.search;
  const body = ["GET", "HEAD"].includes(request.method)
    ? undefined
    : await request.text();
  const response = await serviceFetch(
    service as ServiceName,
    `/${path.join("/")}${query}`,
    {
      method: request.method,
      body: body || undefined,
      headers: {
        "Content-Type": request.headers.get("content-type") ?? "application/json",
        ...(request.headers.get("idempotency-key")
          ? { "Idempotency-Key": request.headers.get("idempotency-key")! }
          : {}),
      },
    },
  );
  return proxyResponse(response);
}

export const GET = forward;
export const POST = forward;
export const PUT = forward;
export const PATCH = forward;
export const DELETE = forward;
