package com.vaultgame.api.infrastructure.web.controllers;

import com.vaultgame.api.application.usecases.product.CreateProductUseCase;
import com.vaultgame.api.application.usecases.product.DeleteProductUseCase;
import com.vaultgame.api.application.usecases.product.GetProductByIdUseCase;
import com.vaultgame.api.application.usecases.product.ListProductsUseCase;
import com.vaultgame.api.application.usecases.product.UpdateProductUseCase;
import com.vaultgame.api.infrastructure.web.dto.ProductResponse;
import com.vaultgame.api.infrastructure.web.dto.ProductWebMapper;
import com.vaultgame.api.infrastructure.web.dto.ProductWriteRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * JSON: {@code details.kind} is GAME, BOARD_GAME, HARDWARE, PERIPHERAL, ACCESSORY, FURNITURE,
 * ACTION_FIGURE, MERCHANDISE or DIGITAL_CARD. Accessory specs use {@code specs.type=CABLE};
 * peripheral specs use {@code specs.type} MOUSE, KEYBOARD or HEADSET.
 * {@code gtin} is required for all categories except DIGITAL_CARD (unique in catalog).
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            ListProductsUseCase listProductsUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductWriteRequest request) {
        return ProductResponse.from(createProductUseCase.execute(ProductWebMapper.toNewProduct(request)));
    }

    @GetMapping
    public List<ProductResponse> list() {
        return listProductsUseCase.execute().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return ProductResponse.from(getProductByIdUseCase.execute(id));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductWriteRequest request) {
        return ProductResponse.from(
                updateProductUseCase.execute(ProductWebMapper.toUpdatedProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        deleteProductUseCase.execute(id);
    }
}
