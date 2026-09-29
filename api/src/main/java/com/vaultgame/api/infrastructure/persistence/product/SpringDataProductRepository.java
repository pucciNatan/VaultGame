package com.vaultgame.api.infrastructure.persistence.product;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataProductRepository extends MongoRepository<ProductDocument, String> {

    boolean existsByGtin(String gtin);

    boolean existsByGtinAndIdNot(String gtin, String id);
}
