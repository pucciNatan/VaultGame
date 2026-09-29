package com.vaultgame.api.domain.gateway;

import com.vaultgame.api.domain.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductGateway {

    Product save(Product product);

    Optional<Product> findById(String id);

    List<Product> findAll();

    void deleteById(String id);
}
