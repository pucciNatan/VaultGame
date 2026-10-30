package com.vaultgame.api.application.port.out;

import com.vaultgame.api.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(String id);

    List<Product> findAll();

    void deleteById(String id);

    boolean existsByGtin(String gtin, String excludingProductId);
}
