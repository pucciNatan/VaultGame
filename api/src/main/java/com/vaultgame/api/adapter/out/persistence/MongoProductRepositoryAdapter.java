package com.vaultgame.api.adapter.out.persistence;

import com.vaultgame.api.adapter.out.persistence.entity.ProductDocument;
import com.vaultgame.api.adapter.out.persistence.entity.ProductDocumentMapper;
import com.vaultgame.api.adapter.out.persistence.repository.SpringDataProductRepository;
import com.vaultgame.api.application.port.out.ProductRepositoryPort;
import com.vaultgame.api.domain.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * MongoDB adapter for {@link ProductRepositoryPort}. Persistence only — existence checks and business
 * rules belong in application use cases.
 */
@Component
public class MongoProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository repository;

    public MongoProductRepositoryAdapter(SpringDataProductRepository repository) {
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

    @Override
    public boolean existsByGtin(String gtin, String excludingProductId) {
        if (excludingProductId == null) {
            return repository.existsByGtin(gtin);
        }
        return repository.existsByGtinAndIdNot(gtin, excludingProductId);
    }
}
