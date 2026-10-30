package com.vaultgame.api.adapter.in.web.controllers;

import com.vaultgame.api.application.port.in.CreateProductPort;
import com.vaultgame.api.application.port.in.DeleteProductPort;
import com.vaultgame.api.application.port.in.GetProductByIdPort;
import com.vaultgame.api.application.port.in.ListProductsPort;
import com.vaultgame.api.application.port.in.UpdateProductPort;
import com.vaultgame.api.adapter.in.web.dto.ProductResponse;
import com.vaultgame.api.adapter.in.web.dto.ProductWebMapper;
import com.vaultgame.api.adapter.in.web.dto.ProductWriteRequest;
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

    private final CreateProductPort createProductPort;
    private final GetProductByIdPort getProductByIdPort;
    private final ListProductsPort listProductsPort;
    private final UpdateProductPort updateProductPort;
    private final DeleteProductPort deleteProductPort;

    public ProductController(
            CreateProductPort createProductPort,
            GetProductByIdPort getProductByIdPort,
            ListProductsPort listProductsPort,
            UpdateProductPort updateProductPort,
            DeleteProductPort deleteProductPort) {
        this.createProductPort = createProductPort;
        this.getProductByIdPort = getProductByIdPort;
        this.listProductsPort = listProductsPort;
        this.updateProductPort = updateProductPort;
        this.deleteProductPort = deleteProductPort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductWriteRequest request) {
        return ProductResponse.from(createProductPort.execute(ProductWebMapper.toNewProduct(request)));
    }

    @GetMapping
    public List<ProductResponse> list() {
        return listProductsPort.execute().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return ProductResponse.from(getProductByIdPort.execute(id));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductWriteRequest request) {
        return ProductResponse.from(
                updateProductPort.execute(ProductWebMapper.toUpdatedProduct(id, request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        deleteProductPort.execute(id);
    }
}
