import { normalizedError, serviceFetch } from "@/lib/server";

export async function GET(
  _request: Request,
  context: { params: Promise<{ id: string }> },
) {
  const { id } = await context.params;
  const [productResponse, stockResponse] = await Promise.all([
    serviceFetch("catalog", `/api/products/${encodeURIComponent(id)}`),
    serviceFetch("inventory", `/inventory/${encodeURIComponent(id)}`),
  ]);
  if (!productResponse.ok) {
    return Response.json(await normalizedError(productResponse), { status: productResponse.status });
  }
  const product = await productResponse.json();
  const stock = stockResponse.ok ? await stockResponse.json() : { availableQuantity: 0 };
  return Response.json({ ...product, availableQuantity: stock.availableQuantity ?? 0 });
}
