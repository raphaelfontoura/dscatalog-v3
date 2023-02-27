package com.github.raphaelfontoura.dscatalog.fixture;

import com.github.raphaelfontoura.dscatalog.dto.ProductDTO;
import com.github.raphaelfontoura.dscatalog.entities.Category;
import com.github.raphaelfontoura.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFixture {

    public static Product createProduct() {
        var product = new Product(1L,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.com/img.png",
                Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(Category.builder().id(2L).name("Electronics").build());
        return product;
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(createProduct());
    }
}
