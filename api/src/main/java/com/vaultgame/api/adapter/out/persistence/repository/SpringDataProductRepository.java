package com.vaultgame.api.adapter.out.persistence.repository;

import com.vaultgame.api.adapter.out.persistence.entity.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataProductRepository extends MongoRepository<ProductDocument, String> {

    boolean existsByGtin(String gtin);

    boolean existsByGtinAndIdNot(String gtin, String id);
}
