package com.vaultgame.api.infrastructure.web.jackson;

import com.vaultgame.api.domain.entity.product.details.ProductDetails;
import com.vaultgame.api.domain.entity.product.details.specs.AccessorySpecs;
import com.vaultgame.api.domain.entity.product.details.specs.PeripheralSpecs;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductJacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer productPolymorphism() {
        return builder -> builder
                .addMixIn(ProductDetails.class, ProductDetailsMixin.class)
                .addMixIn(AccessorySpecs.class, AccessorySpecsMixin.class)
                .addMixIn(PeripheralSpecs.class, PeripheralSpecsMixin.class);
    }
}
