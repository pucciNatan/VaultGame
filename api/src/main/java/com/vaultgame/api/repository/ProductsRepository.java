package com.vaultgame.api.repository;

import com.vaultgame.api.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductsRepository extends MongoRepository<Product, String> {}
