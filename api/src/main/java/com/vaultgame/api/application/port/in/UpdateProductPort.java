package com.vaultgame.api.application.port.in;

import com.vaultgame.api.domain.model.Product;

public interface UpdateProductPort {

    Product execute(Product product);
}
