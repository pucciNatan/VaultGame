import { describe, expect, it } from "vitest";
import { ApiClientError, formatMoney, orderStatusLabel, productImage } from "./client";
import { queryKeys } from "../hooks/query-keys";

describe("storefront helpers", () => {
  it("formats BRL prices", () => {
    expect(formatMoney(59.9)).toContain("59,90");
  });

  it("accepts only remote product images", () => {
    expect(productImage(["invalid", "https://cdn.test/game.jpg"]))
      .toBe("https://cdn.test/game.jpg");
    expect(productImage([])).toBeNull();
  });

  it("translates order statuses", () => {
    expect(orderStatusLabel("PAID")).toBe("Pagamento aprovado");
    expect(orderStatusLabel("PAYMENT_FAILED")).toBe("Pagamento recusado");
  });

  it("exposes structured client errors", () => {
    const error = new ApiClientError("Não autorizado", "UNAUTHORIZED", 401);
    expect(error).toMatchObject({
      message: "Não autorizado",
      code: "UNAUTHORIZED",
      status: 401,
    });
  });

  it("keeps resource query keys stable", () => {
    expect(queryKeys.products).toEqual(["products"]);
    expect(queryKeys.product("game-1")).toEqual(["products", "game-1"]);
    expect(queryKeys.order("order-1")).toEqual(["orders", "order-1"]);
  });
});
