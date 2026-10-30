package com.vaultgame.order.application.port.out;

import com.vaultgame.order.application.port.out.model.CatalogProduct;

import java.util.Collection;
import java.util.List;

public interface CatalogPort {

    List<CatalogProduct> getProducts(Collection<String> productIds);
}
