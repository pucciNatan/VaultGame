package com.vaultgame.api.application.port.in;

import com.vaultgame.api.domain.model.Product;

public interface CreateProductPort {

    Product execute(Product product);
}
