package com.github.raphaelfontoura.dscatalog.services;

import com.github.raphaelfontoura.dscatalog.dto.ProductDTO;
import com.github.raphaelfontoura.dscatalog.repositories.ProductRepository;
import com.github.raphaelfontoura.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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

    @Test
    void findAllPagedShouldReturnPageWhenFirstPageAndSize10() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAll(pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPagedShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
