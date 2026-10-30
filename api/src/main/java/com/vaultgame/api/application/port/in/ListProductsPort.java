package com.vaultgame.api.application.port.in;

import com.vaultgame.api.domain.model.Product;
import java.util.List;

public interface ListProductsPort {

    List<Product> execute();
}
