import { normalizedError, serviceFetch } from "@/lib/server";
import type { Product } from "@/lib/types";

export async function GET() {
  const catalogResponse = await serviceFetch("catalog", "/api/products");
  if (!catalogResponse.ok) {
    return Response.json(await normalizedError(catalogResponse), { status: catalogResponse.status });
  }
  const products = (await catalogResponse.json()) as Product[];
  const active = products.filter((product) => product.active !== false);
  if (!active.length) return Response.json([]);

  const ids = active.map((product) => product.id).join(",");
  const stockResponse = await serviceFetch(
    "inventory",
    `/inventory?productIds=${encodeURIComponent(ids)}`,
  );
  const stock = stockResponse.ok
    ? (await stockResponse.json()) as Array<{ productId: string; availableQuantity: number }>
    : [];
  const byId = new Map(stock.map((item) => [item.productId, item.availableQuantity]));
  return Response.json(active.map((product) => ({
    ...product,
    availableQuantity: byId.get(product.id) ?? 0,
  })));
}
