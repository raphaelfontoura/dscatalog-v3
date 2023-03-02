package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.repositories.ProductRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceIT {
    
    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;
    
    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists() {
        service.deleteProduct(existingId);
        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProduct(nonExistingId));
        assertEquals(countTotalProducts, repository.count());
    }
}
