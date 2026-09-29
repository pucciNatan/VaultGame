package com.vaultgame.api.infrastructure.persistence.product;

import com.vaultgame.api.domain.entity.Product;
import com.vaultgame.api.domain.gateway.ProductGateway;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MongoProductGateway implements ProductGateway {

    private final SpringDataProductRepository repository;

    public MongoProductGateway(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        ProductDocument saved = repository.save(ProductDocumentMapper.toDocument(product));
        return ProductDocumentMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(String id) {
        return repository.findById(id).map(ProductDocumentMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(ProductDocumentMapper::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
