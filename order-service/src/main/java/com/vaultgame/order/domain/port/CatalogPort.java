package com.vaultgame.order.domain.port;

import com.vaultgame.order.domain.port.model.CatalogProduct;

import java.util.Collection;
import java.util.List;

public interface CatalogPort {

    List<CatalogProduct> getProducts(Collection<String> productIds);
}
